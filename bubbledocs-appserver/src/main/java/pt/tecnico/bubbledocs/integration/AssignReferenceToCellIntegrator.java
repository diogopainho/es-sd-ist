package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.local.AssignReferenceCell;

public class AssignReferenceToCellIntegrator extends BubbleDocsIntegrator {
	AssignReferenceCell assignreferencecellservice;

	public AssignReferenceToCellIntegrator(String tokenUser, int docId,
			String cellId, String reference) {
		assignreferencecellservice = new AssignReferenceCell(tokenUser, docId,
				cellId, reference);
	}

	public final String getResult() {
		return assignreferencecellservice.getResult();
	}
	
	@Override
	public void execute() throws BubbleDocsException {
		assignreferencecellservice.execute();
	}
}
