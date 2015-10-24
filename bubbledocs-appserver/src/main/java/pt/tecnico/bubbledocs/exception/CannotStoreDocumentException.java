package pt.tecnico.bubbledocs.exception;

public class CannotStoreDocumentException extends BubbleDocsException {

	private static final long serialVersionUID = 555038354222977869L;
	private int docID;

	public int getDocID() {
		return docID;
	}

	public CannotStoreDocumentException(int docID) {
		this.docID = docID;
	}

	public CannotStoreDocumentException() {
		super("The client was unable to store the required document");
	}

	@Override
	public String getMessage() {
		return "The document with the ID " + this.docID
				+ ", does not exist in the repository\n";
	}

}
