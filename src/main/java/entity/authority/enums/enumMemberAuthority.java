package entity.authority.enums;

public enum enumMemberAuthority {

	SUPER_OWNER("1"),
	OWNER("2"),
	MEMBER("3"),
	VIEWER("4");
	
	private String str;
	
	private enumMemberAuthority(String str){
		this.str = str;
	}
	
	@Override
	public String toString(){
		return str;
	}
}
