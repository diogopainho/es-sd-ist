package pt.tecnico.bubbledocs.exception;

public class DuplicateEmailException extends BubbleDocsException {

	private static final long serialVersionUID = -1844577437229018077L;
	private String email;
	
	public String getEmail() {
		return this.email;
	}

	public DuplicateEmailException(String email) {
		this.email = email;
	}
	
	public DuplicateEmailException() {
		super("Email is duplicated");
	}
	
	@Override
	public String getMessage() {
		return "O email  " + this.email + " já existe\n";
	}
}
