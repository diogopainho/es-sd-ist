package pt.tecnico.bubbledocs.service.local;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class GetSpreadSheetContent extends BubbleDocsService {
	private String usertoken;
	private int documentID;
	private String[][] contentSheet;

	public GetSpreadSheetContent(String userToken, int docId) {
		this.usertoken = userToken;
		this.documentID = docId;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = getBubbleDocs();
		bd.validateSessionByToken(usertoken);
		bd.validatePermissions(usertoken, documentID);
		this.contentSheet = bd.getSpreadSheetContent(documentID);
	}

	public String[][] getSheetContent() {
		return contentSheet;
	}

	public int getSheetID() {
		return documentID;
	}

}
