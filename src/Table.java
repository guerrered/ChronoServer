import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Table {
	List<Player> table;
	static HashMap<Integer, Player> map;
	Gson g;
	
	public Table(){
		g = new Gson();
		map = new HashMap<>();
	}
	
	/**
	 * 
	 * @param s takes string s which is a JSON string
	 * that will contain the extra player information
	 * This info will then update whatever currently exists in 
	 * the map
	 */
	public void refreshTable(String json){
		for(Player p: map.values()){//"clear" times and DNFs if they existed
			p.totalTime = 999999999;
			p.DNF = false;
		}
		List<Player> tempList = g.fromJson(json,new TypeToken<List<Player>>(){}.getType());
		for(Player p: tempList){
			int key = p.ID;
			if(map.containsKey(key)){
				Player toMod = map.get(key);
				toMod.totalTime = p.totalTime;//update the time value
				toMod.DNF = p.DNF;
				toMod.cancel = p.cancel;
				if(p.cancel){
					toMod.totalTime =999999998;
				}
				if(p.DNF){
					toMod.totalTime = 999999998;//larger time than possible on chronotimer guarantees DNFs are sent to end
				}
			}
			else{
				p.firstInitial = "N/A";
				p.lastName ="N/A";
				if(p.cancel){
					p.totalTime = 999999998;
				}
				if(p.DNF){
					p.totalTime = 999999998;//larger time than possible on chronotimer guarantees DNFs are sent to end
				}
				map.put(key, p);//adds new player to list no name only time and bibNum
				
			}
		}
	}
	
	/**
	 * 
	 * @param P Player that will be added to the map
	 * if a previous player exists with the same bib number they
	 * will be replaced by p
	 */

	public void addToTable(Player p){
		Integer key = p.ID;//adds player to map
		if(map.containsKey(key)){
			map.remove(key);
		}
		map.put(key, p);
	}
	
	/**
	 * goes through the map and sort the Players by their time
	 * and returns a string in with HTML table formatting;
	 * otherwise if map was empty it returns empty String
	 */
	public static String getListAsHTML(){
		
		String ret = "";
		List<Player> mainList = new ArrayList<>();
		for(Player p: map.values()){
			mainList.add(p);
		}
		if(mainList.isEmpty()){
			ret  = "";
		}
		else{
			List<Player> sorted = sort(mainList);
			for(int i=0; i<sorted.size(); i++){
				//ret = ret + sorted.get(i) + "\n";
			}
			for(Player p: sorted){
				ret +=("<tr><td>" + p.firstInitial + "</td>"
				+ "<td>"+ p.lastName + "</td>" 
				+ "<td>"+ p.ID+ "</td>" );
				if(p.totalTime == 999999999){//not participated 
						ret += "<td>Has not Participated</td></tr>";
					
				}
				else{
					if(p.DNF){//dnf get 999999998 so they get sorted before any not participated
						ret +="<td>DNF</td></tr>";
					}
					else if(p.cancel){
						ret +="<td>Cancelled</td></tr>";
					}
					else{
						ret += "<td>"+ timeFormat(p.totalTime) + "</td></tr>";
					}
				}
			}
		}
		return ret;
	}
	

	 /**
		 * timeFormat() converts milliseconds into hours minutes seconds and milliseconds. Helper method for export.
		 * @param duration - In milliseconds
		 * @return properly formatted time
		 */
	public static String timeFormat( long duration ) {
		    final TimeUnit scale = TimeUnit.MILLISECONDS;
		    
		    long days = scale.toDays(duration);
		    duration -= TimeUnit.HOURS.toMillis(days);
		    long hours = scale.toHours( duration );
		    duration -= TimeUnit.HOURS.toMillis( hours );
		    long minutes = scale.toMinutes( duration );
		    duration -= TimeUnit.MINUTES.toMillis( minutes );
		    long seconds = scale.toSeconds( duration );
		    duration -= TimeUnit.SECONDS.toMillis( seconds );
		    long millis = scale.toMillis( duration );
		    
		    return String.format("%d:%02d:%02d:%03d",hours, minutes, seconds, millis);
	 }
	@SuppressWarnings("unchecked")
	public static List<Player> sort(List<Player> mainList){
		List<Player>sorted= new ArrayList<>();
		for(int i =0; i<mainList.size(); i++){
			sorted.add(mainList.get(i));
		}
		sorted.sort(c);
		return sorted;
	}
	
	public static Comparator<Player> c = new Comparator<Player>(){
		public int compare(Player p1,Player p2){
			return Long.compare(p1.totalTime, p2.totalTime);
			
		}
	};
	
	
	
	
	
	
}
