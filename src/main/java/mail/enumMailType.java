package mail;

public enum enumMailType {

	JOIN("1"),
	LOST_PASSWORD("2"),
	SLEEP("3");
	
	String kind;
	
	private enumMailType(String str){
		
		kind = str;
	}
	
	@Override
	public String toString(){
		return kind;
	}
	
	
}
