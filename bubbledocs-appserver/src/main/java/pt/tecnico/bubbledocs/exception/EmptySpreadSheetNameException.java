package pt.tecnico.bubbledocs.exception;

public class EmptySpreadSheetNameException extends BubbleDocsException {

	private static final long serialVersionUID = -9162658733503069289L;
	private int cell_line;
	private int cell_column;
	
	public int getLine() {
		return this.cell_line;
	}
	
	public int getColumn() {
		return this.cell_column;
	}
	
	public EmptySpreadSheetNameException(int line, int column) {
		this.cell_line = line;
		this.cell_column = column;
	}
	
	public EmptySpreadSheetNameException() {
		super("The SpreedSheet name is empty");
	}
	
	@Override
	public String getMessage() {
		return "Cell" + this.cell_line + ";" + this.cell_column + "does not contain name.\n";
	}
	
}
