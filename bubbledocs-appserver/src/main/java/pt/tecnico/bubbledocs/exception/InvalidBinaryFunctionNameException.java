package pt.tecnico.bubbledocs.exception;

public class InvalidBinaryFunctionNameException extends BubbleDocsException {

	private static final long serialVersionUID = 4424964352433604465L;
	String name;

	public InvalidBinaryFunctionNameException() {
		super(
				"The function name is invalid. Must be ADD, SUB, MUL or DIV");
	}
	
	public InvalidBinaryFunctionNameException(String name) {
		this.name = name;
	}
	
	@Override
	public String getMessage(){
		return "The function name " + name + " is invalid."
				+ "Must be ADD, SUB, MUL or DIV";
	}
}