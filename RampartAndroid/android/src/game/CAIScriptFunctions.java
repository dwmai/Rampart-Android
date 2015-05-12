package game;


import java.util.Map;
import java.util.Vector;


/**
 * Created by Qingwei Zeng on 2/27/2015.
 */

/* Class containing all of the AI related functions*/
public class CAIScriptFunctions {

    /* Builds the string that will be used to call Lua functions.
     * scriptName - the name of the script containing wanted function without the ".lua"
     * funcName - name of the function you want to call (same as the name of the function in script
     * Parameters - vector containing the function's argument value as strings
     */
    public static String buildCallString(String scriptName, String funcName, Vector<String> Parameters){
        StringBuilder sb = new StringBuilder();

        //Create string to evaluate function and assign result to a unique return variable
        sb.append(scriptName + "_return_value = " + funcName + "(");

        for (String temp : Parameters) {
            sb.append(temp);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        sb.append(")");


        return sb.toString();
    }

    /* Adds parameter to vector.*/
    public static void addParameter(Object param, Vector<String> paramV){
        if(param instanceof Integer){
            paramV.add(param.toString());
        }
        else if(param instanceof Boolean){
            if(((Boolean) param).booleanValue() == true)
                paramV.add("true");
            else
                paramV.add("false");
        }
    }

    /* Function from castle_select.lua. Determines which castle location (denoted by an int) the AI
     * will choose.
     * numCastleLocations - maximum number of castles
     */
    public static int getCastleLocation(int numCastleLocations){
        Vector param = new Vector();
        //param.add(Integer.toString(numCastleLocations));
        addParameter(new Integer(numCastleLocations), param);

        String pseudoScript = buildCallString("castle_select","getCastleLocation", param);
        CKahluaInterface.getInstance().runStringScript(pseudoScript);

        if(CKahluaInterface.getInstance().getEnvironment().rawget("castle_select_return_value") == null)
            return -1;
        else
            return ((Double)CKahluaInterface.getInstance().getEnvironment().rawget("castle_select_return_value")).intValue();
    }

    /* Function from cannon_placement_ai.lua. NOT DONE*/
    public static boolean getCannonLocs(int colorIndex, int xTile, int yTile, boolean xDir, boolean yDir, int dMapWidth, int dMapHeight, int numCannons){
        Vector param = new Vector();
        addParameter(new Integer(colorIndex), param);
        addParameter(new Integer(xTile), param);
        addParameter(new Integer(yTile), param);
        addParameter(new Boolean(xDir), param);
        addParameter(new Boolean(yDir), param);
        addParameter(new Integer(dMapWidth), param);
        addParameter(new Integer(dMapHeight), param);
        addParameter(new Integer(numCannons), param);

        String pseudoScript = buildCallString("cannon_placement_ai", "getCannonLocs", param);
        CKahluaInterface.getInstance().runStringScript(pseudoScript);

        if((Boolean)CKahluaInterface.getInstance().getEnvironment().rawget("cannon_placement_ai_return_value") == true)
            return true;
        else if((Boolean)CKahluaInterface.getInstance().getEnvironment().rawget("cannon_placement_ai_return_value") == false)
            return false;
        else {
            return false;
        }
    }

    public static Map<String, Double> getxy(int colorIndex){
        Vector param = new Vector();
        addParameter(new Integer(colorIndex), param);

        String pseudoScript = buildCallString("battle_ai", "getxy", param);
        CKahluaInterface.getInstance().runStringScript(pseudoScript);

        Map<String, Double> return_val = CKahluaInterface.getInstance().getConverterManager().fromLuaToJava(CKahluaInterface.getInstance().getEnvironment().rawget("battle_ai_return_value"), Map.class);

        return return_val;
    }

    public static Map<String, Double> findBestPlacement(int colorIndex, int XTile, int YTile){
        Vector param = new Vector();
        addParameter(new Integer(colorIndex), param);
        addParameter(new Integer(XTile), param);
        addParameter(new Integer(YTile), param);

        String pseudoScript = buildCallString("rebuild_ai", "findBestPlacement", param);
        CKahluaInterface.getInstance().runStringScript(pseudoScript);

        Map<String, Double> result_val = CKahluaInterface.getInstance().getConverterManager().fromLuaToJava(CKahluaInterface.getInstance().getEnvironment().rawget("rebuild_ai_return_value"), Map.class);
        return result_val;
    }
}