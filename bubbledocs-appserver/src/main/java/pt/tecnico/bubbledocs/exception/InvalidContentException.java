package pt.tecnico.bubbledocs.exception;

public class InvalidContentException extends BubbleDocsException {

	private static final long serialVersionUID = 1835712100606249532L;

	public InvalidContentException() {
		super("The content is invalid");
	}
}
