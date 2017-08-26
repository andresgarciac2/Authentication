package co.com.uniandes.arquitectura.utils;

import java.util.HashMap;
import java.util.Map;

public class DniTypes {

	private static Map<String, Integer> types;
	
    public static Map<String, Integer> getTypes()
    {
        if (types == null) {
        	types = new HashMap<String, Integer>();
        	types.put("CC", 0);
        	types.put("TI", 1);
        	types.put("CE", 2);
        }
        return types;
    }
}
