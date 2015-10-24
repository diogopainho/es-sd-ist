package pt.tecnico.bubbledocs.exception;

public class SpreadSheetDoesNotExistException extends BubbleDocsException {

	private static final long serialVersionUID = -8875012156422644839L;
	private long id;

	public SpreadSheetDoesNotExistException(long id) {
		this.id = id;
	}

	public long getSpreadSheetId() {
		return this.id;
	}

	@Override
	public String getMessage() {
		return "A spreadsheet com o id " + this.id + " não existe\n";
	}
}
