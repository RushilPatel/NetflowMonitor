import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import com.eclipsesource.json.JsonObject;

public class GeoLocate
{
	private HashMap<String, Location> locationMap;
	public GeoLocate(){
		locationMap = new HashMap<String, Location>();
	}
	
	public Location getLocation(String ip){
		Location location = locationMap.get(ip);
		if(location!= null){
			return location;
		}else{
			try{
				URL uri = new URL("http://10.10.22.184:8080/json/" + ip);
				BufferedReader in = new BufferedReader(new InputStreamReader(uri.openStream()));
		        String inputLine;
		        String data = "";
		        while ((inputLine = in.readLine()) != null) {
		            data += inputLine;
		        }
		        in.close();
		        if(data.contains("NOT FOUND")){
					return null;
		        }else{
		            return parseLocation(data);
		        }
			}catch(IOException e){
				e.printStackTrace();
				return null;
			}
		}
	}
	
	private Location parseLocation(String data){

		Location location = new Location();
		JsonObject jsonObject = JsonObject.readFrom(data);
//		location.setArea_code(jsonObject.get("area_code").asString());
		location.setCity(jsonObject.get("city").asString());
		location.setCountry_code(jsonObject.get("country_code").asString());
		location.setCountry_name(jsonObject.get("country_name").asString());
		location.setIp(jsonObject.get("ip").asString());
		location.setLatitude(jsonObject.get("latitude").asFloat());
		location.setLongitude(jsonObject.get("longitude").asFloat());
//		location.setMetro_code(jsonObject.get("metro_code").asString());
		location.setRegion_code(jsonObject.get("region_code").asString());
		location.setRegion_name(jsonObject.get("region_name").asString());
		location.setZipcode(jsonObject.get("zip_code").asString());
		locationMap.put(location.getIp(), location);
		return location;
	}
}