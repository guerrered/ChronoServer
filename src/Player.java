
public class Player {
	
	public int ID;
	public long startTime, endTime, totalTime;
	public boolean DNF = false;
	public boolean ran = false;
	public boolean running = false;
	public boolean cancel =false;
	String lastName;
	String firstInitial;
	
	public String zOffset = "";
	
	/*String bibNumber;
	String lastName;
	String firstInitial;
	String time;
	boolean DNF = false;*/
	
	public Player(int bibNum, String lastName, String firstInitial, long time){
		this.ID = bibNum;
		this.lastName = lastName;
		this.firstInitial = firstInitial;
		this.totalTime = time;
	}
	
	public String toString(){
		String str="";
		str = str  + ID + " " + lastName + " "  + firstInitial  + ". " + totalTime; 
		return str;
	}
	
	public long getTime(){
		return totalTime;
	}
}
