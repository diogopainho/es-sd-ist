package pt.tecnico.bubbledocs.service.local;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidContentException;

public class AssignBinaryFunctionToCell extends BubbleDocsService {

	private String result;
	private String function;
	private String cellId;
	private int sheetID;
	private String userToken;
	private Integer intResult;

	public AssignBinaryFunctionToCell(String userToken, int sheetID,
			String cellId, String function) {
		this.userToken = userToken;
		this.sheetID = sheetID;
		this.cellId = cellId;
		this.function = function;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		try {
			this.intResult = getBubbleDocs().addBinaryFunctionToCell(userToken,
					sheetID, cellId, function).eval();
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
