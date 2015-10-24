package pt.tecnico.bubbledocs.exception;

public class UserHasNoPermissionException extends BubbleDocsException{

	private static final long serialVersionUID = 1005865989506684922L;
	private String userName;
	
	public String getPersonName() {
		return this.userName;
	}

	public UserHasNoPermissionException(String personName) {
		this.userName = personName;
	}
	
	public UserHasNoPermissionException() {
		super("User has no permission");
	}
	
	@Override
	public String getMessage() {
		return "O utilizador com o username " + this.userName + " não tem permissão\n";
	}
	
}