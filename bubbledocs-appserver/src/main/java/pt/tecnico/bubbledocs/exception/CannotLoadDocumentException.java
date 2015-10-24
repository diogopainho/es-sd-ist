package pt.tecnico.bubbledocs.exception;

public class CannotLoadDocumentException extends BubbleDocsException {

	private static final long serialVersionUID = -7815268974686525856L;
	private int docID;

	public int getDocID() {
		return docID;
	}

	public CannotLoadDocumentException(int docID) {
		this.docID = docID;
	}

	public CannotLoadDocumentException() {
		super("The client was unable to load the required document");
	}

	@Override
	public String getMessage() {
		return "The document with the ID " + this.docID + ", does not exist\n";
	}
}
