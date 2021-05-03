package data.campaign.rulecmd;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc;

import data.addstations.Utilities;

import java.util.*;
import java.util.List;

public class CheckStationBuild extends BaseCommandPlugin
{
    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap)
    {
        if(Global.getSettings().isDevMode()) return true;
        for(Map.Entry<String, Number> cost : Utilities.getCost("required").entrySet()){
            CargoAPI cargo = Global.getSector().getPlayerFleet().getCargo();
            if (cargo.getCommodityQuantity(cost.getKey()) < (float)cost.getValue()) return false;
        }
        for(Map.Entry<String, Number> cost : Utilities.getCost("consumed").entrySet()){
            CargoAPI cargo = Global.getSector().getPlayerFleet().getCargo();
            if (cargo.getCommodityQuantity(cost.getKey()) < (float)cost.getValue()) return false;
        }
        return true;
    }
}