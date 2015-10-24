package pt.tecnico.bubbledocs.exception;

public class UnavailableServiceException extends BubbleDocsException {

	private static final long serialVersionUID = 8880758742237016586L;

	public UnavailableServiceException() {
		super("O servico nao esta disponivel");
	}

	@Override
	public String getMessage() {
		return "O servico nao esta disponivel\n";
	}

}
