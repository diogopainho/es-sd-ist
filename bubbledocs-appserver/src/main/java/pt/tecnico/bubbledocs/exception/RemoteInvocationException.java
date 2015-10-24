package pt.tecnico.bubbledocs.exception;

public class RemoteInvocationException extends BubbleDocsException {

	private static final long serialVersionUID = 7744264671978526975L;
	
	public RemoteInvocationException() {
		super("Could not make remote invocation");
	}

}
