package pt.tecnico.bubbledocs.exception;

public class InvalidReferenceException extends BubbleDocsException {

	private static final long serialVersionUID = -9059333428638651761L;
	String reference;
	
	public String reference() {
		return this.reference;
	}
	
	public InvalidReferenceException(String ref) {
		this.reference = ref;
	}
	
	public InvalidReferenceException() {
		super("The reference is invalid");
	}
	
	@Override
	public String getMessage() {
		return  this.reference + "is not a valid reference.\n";
	}
}
