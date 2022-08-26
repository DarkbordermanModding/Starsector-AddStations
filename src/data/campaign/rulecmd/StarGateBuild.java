package data.campaign.rulecmd;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.ids.Entities;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc;

import data.addstations.Utilities;

import java.util.List;
import java.util.Map;

public class StarGateBuild extends BaseCommandPlugin
{
    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap)
    {
        if(dialog == null) return false;

        SectorEntityToken token = dialog.getInteractionTarget();
        build(token);
        if(!Global.getSettings().isDevMode()) removeBuildCosts();
        dialog.dismiss();
        return true;
    }

    public SectorEntityToken build(SectorEntityToken token){
        LocationAPI loc = token.getContainingLocation();

        SectorEntityToken built = loc.addCustomEntity(
            "gate_" + token.getStarSystem().getId(),
            token.getStarSystem().getNameWithTypeShort() + " Gate",
            Entities.INACTIVE_GATE,
            token.getFaction().getId()
        );
        if (token.getOrbit() != null) built.setOrbit(token.getOrbit().makeCopy());
        built.setLocation(token.getLocation().x, token.getLocation().y);
        built.getMemoryWithoutUpdate().set("$originalStableLocation", built);
        loc.removeEntity(token);

        return built;
    }

    public void removeBuildCosts()
    {
        CargoAPI cargo = Global.getSector().getPlayerFleet().getCargo();
        for(Map.Entry<String, Number> cost : Utilities.getCost("station", "consumed").entrySet()){
            cargo.removeCommodity((String)cost.getKey(), (float)cost.getValue());
        }
    }
}