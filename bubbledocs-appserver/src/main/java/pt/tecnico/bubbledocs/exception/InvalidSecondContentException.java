package pt.tecnico.bubbledocs.exception;

public class InvalidSecondContentException extends BubbleDocsException{

	private static final long serialVersionUID = 418104775543837173L;
	private String content;

	public String getContent() {
		return this.content;
	}

	public InvalidSecondContentException(String content) {
		this.content = content;
	}

	public InvalidSecondContentException() {
		super("The first content is invalid. Must be a reference or an integer.");
	}

	@Override
	public String getMessage() {
		return "Second content " + this.content
				+ " is invalid. Must be a reference or an integer.";
	}
}