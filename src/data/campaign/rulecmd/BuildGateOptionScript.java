package data.campaign.rulecmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc;

import data.addstations.Utilities;


public class BuildGateOptionScript extends BaseCommandPlugin{

    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap)
    {
        TextPanelAPI text = dialog.getTextPanel();
        OptionPanelAPI opts = dialog.getOptionPanel();
        opts.clearOptions();

        Map<String, Number> required = Utilities.getCost("gate", "required");
        if(required.size() == 0){
            text.addPara("Create a gate does not require any objective.");
        }
        else{
            text.addPara("Create a gate requires objective below:");
            ArrayList<Object> requiredCostPanel = new ArrayList<Object>();
            for(Map.Entry<String, Number> cost : required.entrySet()){
                requiredCostPanel.add(cost.getKey());
                requiredCostPanel.add((int)(float)cost.getValue());
                requiredCostPanel.add(false);
            }
            Utilities.setCostPanel(dialog, requiredCostPanel.toArray());
        }

        Map<String, Number> consumed = Utilities.getCost("gate", "consumed");
        if(consumed.size() == 0){
            text.addPara("Create a gate does not consume any objective.");
        }
        else{
            text.addPara("Create a gate consumes objective below:");
            ArrayList<Object> consumedCostPanel = new ArrayList<Object>();
            for(Map.Entry<String, Number> cost : consumed.entrySet()){
                consumedCostPanel.add(cost.getKey());
                consumedCostPanel.add((int)(float)cost.getValue());
                consumedCostPanel.add(true);
            }
            Utilities.setCostPanel(dialog, consumedCostPanel.toArray());
        }
        opts.addOption("Proceed", "AddBuildGateProceedOption");
        opts.setEnabled("AddBuildGateProceedOption", Utilities.canBuildGate());
        opts.addOption("Back", "SL_cancelBuild");

        return true;
    }
}
