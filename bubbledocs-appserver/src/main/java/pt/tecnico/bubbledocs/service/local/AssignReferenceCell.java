package pt.tecnico.bubbledocs.service.local;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidContentException;

// add needed import declarations

public class AssignReferenceCell extends BubbleDocsService {
	private String result;
	private String reference;
	private String cellId;
	private int docId;
	private String userToken;
	private Integer intResult;

	public AssignReferenceCell(String tokenUser, int docId, String cellId,
			String reference) {
		this.reference = reference;
		this.cellId = cellId;
		this.docId = docId;
		this.userToken = tokenUser;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		try {
			this.intResult = getBubbleDocs().addReferenceToCell(userToken,
					docId, cellId, reference).eval();
		} catch (InvalidContentException e) {
			this.result = "#VALUE";
		}
	}

	public final String getResult() {
		this.dispatch();
		try {
			if (intResult != null)
				this.result = Integer.toString(intResult);
			return this.result;
		} catch (InvalidContentException e) {
			this.result = "#VALUE";
			return this.result;
		}
	}
}
