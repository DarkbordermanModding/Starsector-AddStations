package data.addstations;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;

import org.json.JSONObject;

import java.util.*;


public class Utilities {
    @SuppressWarnings("unchecked")
    public static Map<String, Number> getCost(String type){
        Map<String, Number> costs = new HashMap<>();
        try {
            JSONObject consumed = Global.getSettings().getJSONObject("addstation").getJSONObject(type);
            Iterator<String> keys = consumed.keys();
            while(keys.hasNext()){
                String key = keys.next();
                costs.put(key, (float)consumed.getDouble(key));
            }
        } catch (Exception e) {}
        return costs;
    }

    public static void setCostPanel(InteractionDialogAPI dialog, Object[] displays){
        // Due to display limit, the display will cut each 3 display items(9 length array)
        int i = 0;
        while(i < displays.length){
            if( i + 9 > displays.length){
                dialog.getTextPanel().addCostPanel("", Arrays.copyOfRange(displays, i, displays.length));
            }
            else{
                dialog.getTextPanel().addCostPanel("", Arrays.copyOfRange(displays, i, i + 9));
            }
            i += 9;
        }
    }

    public static boolean canBuildStation(){
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
