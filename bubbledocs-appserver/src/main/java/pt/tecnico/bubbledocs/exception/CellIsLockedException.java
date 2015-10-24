package pt.tecnico.bubbledocs.exception;

public class CellIsLockedException extends BubbleDocsException {

	private static final long serialVersionUID = -7865256152384738293L;
	private int cell_line;
	private int cell_column;

	public int getLine() {
		return this.cell_line;
	}

	public int getColumn() {
		return this.cell_column;
	}

	public CellIsLockedException(int line, int column) {
		this.cell_line = line;
		this.cell_column = column;
	}

	public CellIsLockedException() {
		super("Cell is locked.\n");
	}

	@Override
	public String getMessage() {
		return "Cell" + this.cell_line + ";" + this.cell_column
				+ "is locked.\n";
	}

}