package co.com.uniandes.arquitectura.utils;

import java.util.HashMap;
import java.util.Map;

public class Countries {

	private static Map<String, Integer> countries;
	private static Map<Integer, String> inverseCountries;
	
    public static Map<String, Integer> getCountries()
    {
        if (countries == null) {
        	countries = new HashMap<String, Integer>();
        	countries.put("Colombia", 57);
        	countries.put("Ecuador", 62);
        	countries.put("Venezuela", 53);
        }
        return countries;
    }
    
    public static Map<Integer, String> getInverseCountries()
    {
        if (inverseCountries == null) {
        	inverseCountries = new HashMap<Integer, String>();
        	inverseCountries.put(57,"Colombia");
        	inverseCountries.put(62,"Ecuador");
        	inverseCountries.put(53,"Venezuela");
        }
        return inverseCountries;
    }
}
