package data.campaign.rulecmd;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketConditionAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.ids.Entities;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.impl.campaign.submarkets.StoragePlugin;
import com.fs.starfarer.api.util.Misc;

import data.addstations.DarkMagicThread;
import data.addstations.Utilities;

import org.json.JSONArray;

import java.util.*;
import java.util.List;

public class StarGateBuild extends BaseCommandPlugin
{
    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap)
    {
        if(dialog == null) return false;

        // Can be improved by selecting many kind of stations
        SectorEntityToken token = dialog.getInteractionTarget();
        String constructionType = "station_side03";
        SectorEntityToken built = build(constructionType, token);
        if(!Global.getSettings().isDevMode()) removeBuildCosts();
        dialog.dismiss();
        // BIG WARNING
        // If the player doesn't view the colony management screen within a few days of market creation
        // There can be a bug related to population growth (Instantly grow to maximum)
        // The dialog will be closed and can't spawn a new dialog
        // use a thread to open another dialog
        Thread t = new Thread(new DarkMagicThread(built));
        t.start();
        return true;
    }

    public SectorEntityToken build(String type, SectorEntityToken token){
        LocationAPI loc = token.getContainingLocation();

        // Identity suffix
        CampaignClockAPI clock = Global.getSector().getClock();
        String suffix = clock.getCycle() + "_" + clock.getMonth() + "_" + clock.getDay() + "_" + clock.getHour();

        SectorEntityToken built = loc.addCustomEntity("gateA", "NAme", Entities.INACTIVE_GATE, token.getFaction().getId());
        if (token.getOrbit() != null) built.setOrbit(token.getOrbit().makeCopy());
        built.setLocation(token.getLocation().x, token.getLocation().y);
        built.getMemoryWithoutUpdate().set("$originalStableLocation", built);
        loc.removeEntity(token);

        /*
        SectorEntityToken built = loc.addCustomEntity(
            "station_" + suffix,
            "Side station",
            type,
            token.getFaction().getId()
        );
        if (token.getOrbit() != null) built.setOrbit(token.getOrbit().makeCopy());

        // Replace original token with built object
        built.setLocation(token.getLocation().x, token.getLocation().y);
        built.getMemoryWithoutUpdate().set("$originalStableLocation", built);
        loc.removeEntity(token);

        // Create market
        MarketAPI market = Global.getFactory().createMarket(
            "AddStation_" + suffix,
            "Side station",
            3
        );
        market.setSize(3);

        market.setSurveyLevel(MarketAPI.SurveyLevel.FULL);
        market.setPrimaryEntity(built);

        market.setFactionId(Global.getSector().getPlayerFleet().getFaction().getId());
        market.setPlayerOwned(true);

        JSONArray industries = new JSONArray();
        JSONArray conditions = new JSONArray();
        boolean freePort = false;
        try {
            industries = Global.getSettings().getJSONObject("addstation").getJSONArray("industries");
            conditions = Global.getSettings().getJSONObject("addstation").getJSONArray("conditions");
            for(int i = 0; i < conditions.length(); i++){
                market.addCondition(conditions.getString(i));
            }
            for(int i = 0; i < industries.length(); i++){
                market.addIndustry(industries.getString(i));
            }
            freePort = Global.getSettings().getJSONObject("addstation").getBoolean("free_port");
            market.setFreePort(freePort);
        } catch (Exception e) {}

        market.addSubmarket("storage");
        StoragePlugin storage = (StoragePlugin)market.getSubmarket("storage").getPlugin();
        storage.setPlayerPaidToUnlock(true);
        market.addSubmarket("local_resources");

        built.setMarket(market);
        Global.getSector().getEconomy().addMarket(market, true);
        built.setFaction(Global.getSector().getPlayerFleet().getFaction().getId());

        // Update survey and industries
        for (MarketConditionAPI condition: market.getConditions()){
            condition.setSurveyed(true);
        }
        for (Industry industry: market.getIndustries()){
            industry.doPreSaveCleanup();
            industry.doPostSaveRestore();
        }
        */
        return built;
    }

    public void removeBuildCosts()
    {
        CargoAPI cargo = Global.getSector().getPlayerFleet().getCargo();
        for(Map.Entry<String, Number> cost : Utilities.getCost("consumed").entrySet()){
            cargo.removeCommodity((String)cost.getKey(), (float)cost.getValue());
        }
    }
}