package pt.tecnico.bubbledocs.exception;

public class UnauthorizedOperationException extends BubbleDocsException{

	private static final long serialVersionUID = 3132841209064497692L;
	
	public UnauthorizedOperationException() {
		super("Unauthorized Operation");
	}
	
	@Override
	public String getMessage() {
		return "A operação não autorizada.\n";
	}
	
}
