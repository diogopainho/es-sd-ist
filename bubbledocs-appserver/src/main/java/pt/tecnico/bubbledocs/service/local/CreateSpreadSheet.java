package pt.tecnico.bubbledocs.service.local;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class CreateSpreadSheet extends BubbleDocsService {
	private int sheetId; // id of the new sheet
	private String userToken;
	private String name;
	private int rows;
	private int columns;

	public int getSheetId() {
		return sheetId;
	}

	public CreateSpreadSheet(String userToken, String name, int rows,
			int columns) {
		this.userToken = userToken;
		this.name = name;
		this.rows = rows;
		this.columns = columns;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = BubbleDocs.getInstance();
		bd.createNewSpreadsheet(name, rows, columns,
				bd.getUserByToken(userToken).getUsername());
	}
}