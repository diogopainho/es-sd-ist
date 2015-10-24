package pt.tecnico.bubbledocs.exception;

public class CellDoesNotExistException extends BubbleDocsException {

	private static final long serialVersionUID = -6948935071585902341L;
	private int cell_line;
	private int cell_column;
	
	public int getLine() {
		return this.cell_line;
	}
	
	public int getColumn() {
		return this.cell_column;
	}
	
	public CellDoesNotExistException(int line, int column) {
		this.cell_line = line;
		this.cell_column = column;
	}
	
	public CellDoesNotExistException() {
		super("Cell does not exists.\n");
	}
	
	@Override
	public String getMessage() {
		return "Cell" + this.cell_line + ";" + this.cell_column + "does not exists.\n";
	}
	
}
