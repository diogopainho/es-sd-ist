package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.local.AssignLiteralCell;

public class AssignLiteralToCellIntegrator extends BubbleDocsIntegrator {
	AssignLiteralCell assignliteralcellservice;

	public AssignLiteralToCellIntegrator(String userToken, int docId,
			String cellId, String literal) {
		assignliteralcellservice = new AssignLiteralCell(userToken, docId,
				cellId, literal);
	}

	@Override
	public void execute() throws BubbleDocsException {
		assignliteralcellservice.execute();
	}
}
