package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.local.GetSpreadSheetContent;

public class GetSpreadSheetContentIntegrator extends BubbleDocsIntegrator {

	private GetSpreadSheetContent getcontentService;

	public GetSpreadSheetContentIntegrator(String userToken, int docId) {

		getcontentService = new GetSpreadSheetContent(userToken, docId);
	}

	@Override
	public void execute() throws BubbleDocsException {
		getcontentService.execute();

	}

	public String[][] getSheetContent() {
		return getcontentService.getSheetContent();
	}
}
