package pt.tecnico.bubbledocs.exception;

public class LoginBubbleDocsException extends BubbleDocsException {
	
	private static final long serialVersionUID = -311100569546730381L;
	private String username;

	public String getUsername() {
		return this.username;
	}
	
	public LoginBubbleDocsException(String username) {
		this.username = username;
	}
	
	public LoginBubbleDocsException() {
		super("The username is unkown or password is incorrect");
	}

	@Override
	public String getMessage() {
		return "Username " + this.username + " is unkown or password is incorrect\n";
	}
}
