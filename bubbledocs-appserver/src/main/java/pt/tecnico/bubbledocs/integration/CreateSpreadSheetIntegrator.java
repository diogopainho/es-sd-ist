package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.local.CreateSpreadSheet;

public class CreateSpreadSheetIntegrator extends BubbleDocsIntegrator {
	CreateSpreadSheet newspreadsheet;

	public CreateSpreadSheetIntegrator(String userToken, String name, int rows,
			int columns) {
		newspreadsheet = new CreateSpreadSheet(userToken, name, rows, columns);
	}

	@Override
	public void execute() throws BubbleDocsException {
		newspreadsheet.execute();
	}
}
