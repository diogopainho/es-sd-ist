package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.local.AssignBinaryFunctionToCell;

public class AssignBinaryFunctionToCellIntegrator extends BubbleDocsIntegrator {
	AssignBinaryFunctionToCell assignbinaryfunctionservice;

	public AssignBinaryFunctionToCellIntegrator(String token, int sheetID,
			String cellid, String function) {
		assignbinaryfunctionservice = new AssignBinaryFunctionToCell(token,
				sheetID, cellid, function);
	}

	@Override
	public void execute() throws BubbleDocsException {
		assignbinaryfunctionservice.execute();
	}
}
