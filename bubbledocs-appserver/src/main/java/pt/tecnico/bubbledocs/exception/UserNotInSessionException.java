package pt.tecnico.bubbledocs.exception;

public class UserNotInSessionException extends BubbleDocsException {
	
	private static final long serialVersionUID = -5926564068923974610L;
	private String userName;
	
	public String getPersonName() {
		return this.userName;
	}

	public UserNotInSessionException(String personName) {
		this.userName = personName;
	}
	
	public UserNotInSessionException() {
		super("The user is not in session");
	}
	
	@Override
	public String getMessage() {
		return "O utilizador com o username " + this.userName + " não está em sessão\n";
	}
	
}
