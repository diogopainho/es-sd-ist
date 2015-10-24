package pt.tecnico.bubbledocs.exception;

public class InvalidColumnNumberException extends BubbleDocsException {

	private static final long serialVersionUID = 6428240727975065986L;
	private int column;

	public int getRow() {
		return this.column;
	}
	
	public InvalidColumnNumberException(int col) {
		this.column = col;
	}
	
	public InvalidColumnNumberException() {
		super("The column number is invalid");
	}
	
	@Override
	public String getMessage() {
		return "Column " + this.column + " invalid\n";
	}
	
}
