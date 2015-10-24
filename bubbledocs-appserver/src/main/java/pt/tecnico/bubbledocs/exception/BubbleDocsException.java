package pt.tecnico.bubbledocs.exception;

public abstract class BubbleDocsException extends RuntimeException {

	private static final long serialVersionUID = -508971844748003405L;

	public BubbleDocsException() {
	}

	public BubbleDocsException(String msg) {
		super(msg);
	}

}