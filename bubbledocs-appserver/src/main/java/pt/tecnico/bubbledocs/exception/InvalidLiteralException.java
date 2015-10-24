package pt.tecnico.bubbledocs.exception;

public class InvalidLiteralException extends BubbleDocsException {

	private static final long serialVersionUID = 8294723183180989635L;
	String literal;
	
	public String getLiteral() {
		return this.literal;
	}
	
	public InvalidLiteralException(String lit) {
		this.literal = lit;
	}
	
	public InvalidLiteralException() {
		super("The literal is invalid");
	}
	
	@Override
	public String getMessage() {
		return  this.literal + "is not a valid literal.\n";
	}
	
}