package pt.tecnico.bubbledocs.exception;

public class DuplicateUsernameException extends BubbleDocsException{

	private static final long serialVersionUID = -3771979839725574543L;
	private String userName;
	
	public String getPersonName() {
		return this.userName;
	}

	public DuplicateUsernameException(String personName) {
		this.userName = personName;
	}
	
	public DuplicateUsernameException() {
		super("Username is duplicated");
	}
	
	@Override
	public String getMessage() {
		return "O utilizador com o username " + this.userName + " já existe\n";
	}
	
}
