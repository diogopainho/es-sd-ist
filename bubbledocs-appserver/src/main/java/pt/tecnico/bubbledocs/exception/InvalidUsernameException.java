package pt.tecnico.bubbledocs.exception;

public class InvalidUsernameException extends BubbleDocsException {
	
	private static final long serialVersionUID = -6286126574091886098L;
	private String username;

	public String getUsername() {
		return this.username;
	}
	
	public InvalidUsernameException(String username) {
		this.username = username;
	}
	
	public InvalidUsernameException() {
		super("The username must have between 3 and 8 characters");
	}

	@Override
	public String getMessage() {
		return "Username " + this.username + " is invalid. Must have between 3 and 8 characters\n";
	}
}
