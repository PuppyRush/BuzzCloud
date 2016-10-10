package mail;

public enum enumMailKind {

	JOIN("1"),
	LOST_PASSWORD("2"),
	SLEEP("3");
	
	String kind;
	
	private enumMailKind(String str){
		
		kind = str;
	}
	
	@Override
	public String toString(){
		return kind;
	}
	
	
}
