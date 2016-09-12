package property.enums.widget;

public enum enumWidgetEvaluation {

	
	Pass("1"),
	Evaluating("2"),
	Unallowance("3");
	
	private String state;
	
	enumWidgetEvaluation(String str){
		state = str;
	}
	
	public String getString(){
		return state;
	}
	
	
	
}
