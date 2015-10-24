package pt.tecnico.bubbledocs.exception;

public class InvalidEmailException extends BubbleDocsException {
	private static final long serialVersionUID = -6286126574091886098L;
	private String email;

	public String getEmail() {
		return this.email;
	}

	public InvalidEmailException(String email) {
		this.email = email;
	}

	public InvalidEmailException() {
		super("The email must have a pattern like random@email.com");
	}

	@Override
	public String getMessage() {
		return "Email " + this.email
				+ " is invalid. Must have a pattern like random@email.com\n";
	}
}
