package pt.tecnico.bubbledocs.exception;

public class InvalidBinaryFunctionInputException extends BubbleDocsException {

	private static final long serialVersionUID = 7053285576327270464L;
	
	String function;
	public InvalidBinaryFunctionInputException() {
		super(
				"The function input is invalid. Must have the form =FUNCTION(reference/literal,reference/literal)");
	}
	
	public InvalidBinaryFunctionInputException(String function) {
		this.function = function;
	}
	
	@Override
	public String getMessage(){
		return "The function" + function + " is invalid."
				+ "Must have the form =FUNCTION(reference/literal,reference/literal)";
	}
	
	public String getFunction(){
		return function;
	}
}
