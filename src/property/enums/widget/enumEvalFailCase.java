package property.enums.widget;

public enum enumEvalFailCase {
	
	
	Manual("1"),
	Auto("2"),
	NoFile("3");
	
	private String UnallowanceReason;
	
	enumEvalFailCase(String str){
		UnallowanceReason = str;
	}
	
	public String getString(){
		return UnallowanceReason;
	}
}
