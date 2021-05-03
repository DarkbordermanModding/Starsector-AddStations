package data.campaign.rulecmd;

import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc;

import data.addstations.Utilities;

import java.util.*;

public class PrintStationCost extends BaseCommandPlugin
{
    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap)
    {
        if(dialog == null) return false;

        TextPanelAPI text = dialog.getTextPanel();

        Map<String, Number> required = Utilities.getCost("required");
        if(required.size() == 0){
            text.addPara("Create a station does not require any objective.");
        }
        else{
            text.addPara("Create a station requires objective below:");
            ArrayList<Object> requiredCostPanel = new ArrayList<Object>();
            for(Map.Entry<String, Number> cost : required.entrySet()){
                requiredCostPanel.add(cost.getKey());
                requiredCostPanel.add((int)(float)cost.getValue());
                requiredCostPanel.add(false);
            }
            Utilities.setCostPanel(dialog, requiredCostPanel.toArray());
        }

        Map<String, Number> consumed = Utilities.getCost("consumed");
        if(consumed.size() == 0){
            text.addPara("Create a station does not consume any objective.");
        }
        else{
            text.addPara("Create a station consumes objective below:");
            ArrayList<Object> consumedCostPanel = new ArrayList<Object>();
            for(Map.Entry<String, Number> cost : consumed.entrySet()){
                consumedCostPanel.add(cost.getKey());
                consumedCostPanel.add((int)(float)cost.getValue());
                consumedCostPanel.add(true);
            }
            Utilities.setCostPanel(dialog, consumedCostPanel.toArray());
        }

        return true;
    }
}
