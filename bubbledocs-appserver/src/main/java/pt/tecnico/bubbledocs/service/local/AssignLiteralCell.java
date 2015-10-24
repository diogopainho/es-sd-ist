package pt.tecnico.bubbledocs.service.local;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class AssignLiteralCell extends BubbleDocsService {
	private String result;

	private String literal;
	private String cellId;
	private int docId;
	private String userToken;

	public AssignLiteralCell(String userToken, int docId, String cellId,
			String literal) {
		this.literal = literal;
		this.cellId = cellId;
		this.docId = docId;
		this.userToken = userToken;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		this.result = getBubbleDocs().addLiteralToCell(userToken, docId,
				cellId, literal);
	}

	public final String getResult() {
		return result;
	}

}
