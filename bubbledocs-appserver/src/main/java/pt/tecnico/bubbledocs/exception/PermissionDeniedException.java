package pt.tecnico.bubbledocs.exception;

public class PermissionDeniedException extends BubbleDocsException {

	private static final long serialVersionUID = 4408248365437042955L;
	String userName;
	
	public String getPersonName() {
		return this.userName;
	}
	
	public PermissionDeniedException(String user) {
		this.userName = user;
	}
	
	public PermissionDeniedException() {
		super("Sem permissão");
	}
	
	public String getMessage() {
		return "O utilizador com o username " + this.userName + " não tem permissão\n";
	}

}
