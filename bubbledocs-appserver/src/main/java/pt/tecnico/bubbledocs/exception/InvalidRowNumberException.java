package pt.tecnico.bubbledocs.exception;

public class InvalidRowNumberException extends BubbleDocsException {

	private static final long serialVersionUID = 7685058388512530480L;
	private int row;

	public int getRow() {
		return this.row;
	}
	
	public InvalidRowNumberException(int line) {
		this.row = line;
	}
	
	public InvalidRowNumberException() {
		super("The number of rows is invalid");
	}

	@Override
	public String getMessage() {
		return "Line " + this.row + " invalid\n";
	}

}
