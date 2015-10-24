package pt.tecnico.bubbledocs.exception;

public class InvalidFirstContentException extends BubbleDocsException{

	private static final long serialVersionUID = -2339845896135838937L;
	
	private String content;

	public String getContent() {
		return this.content;
	}

	public InvalidFirstContentException(String content) {
		this.content = content;
	}

	public InvalidFirstContentException() {
		super("The first content is invalid. Must be a reference or an integer.");
	}

	@Override
	public String getMessage() {
		return "First content " + this.content
				+ " is invalid. Must be a reference or an integer.";
	}
}
