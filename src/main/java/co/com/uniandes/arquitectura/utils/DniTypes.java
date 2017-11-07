package co.com.uniandes.arquitectura.utils;

import java.util.HashMap;
import java.util.Map;

public class DniTypes {

	private static Map<String, Integer> types;
	private static Map<Integer, String> inverseTypes;
	
    public static Map<String, Integer> getTypes()
    {
        if (types == null) {
        	types = new HashMap<String, Integer>();
        	types.put("CC", 0);
        	types.put("TI", 1);
        	types.put("CE", 2);
        	types.put("NIT", 3);
        }
        return types;
    }
    
    public static Map<Integer, String> getInverseTypes()
    {
        if (inverseTypes == null) {
        	inverseTypes = new HashMap<Integer, String>();
        	inverseTypes.put(0,"CC");
        	inverseTypes.put(1,"TI");
        	inverseTypes.put(2,"CE");
        	inverseTypes.put(3,"NIT");
        }
        return inverseTypes;
    }
}
