package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.CannotStoreDocumentException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.local.ExportDocument;
import pt.tecnico.bubbledocs.service.local.GetSpreadSheetID;
import pt.tecnico.bubbledocs.service.local.GetUsername4TokenService;

public class ExportDocumentIntegrator extends BubbleDocsIntegrator {
	private ExportDocument exportService;
	private String username;
	private String docName;
	private byte[] docBytes;

	public ExportDocumentIntegrator(String userToken, int docId) {
		GetUsername4TokenService service1 = new GetUsername4TokenService(
				userToken);
		service1.execute();

		GetSpreadSheetID service2 = new GetSpreadSheetID(docId);
		service2.execute();

		exportService = new ExportDocument(userToken, docId);
		this.username = service1.getUserName();
		this.docName = service2.getSpreadSheetName();
	}

	@Override
	public void execute() throws BubbleDocsException {
		exportService.execute();
		this.docBytes = exportService.getDocumentByteArray();
		try {
			System.out.println("USER NAME: " + username);
			getStoreRemoteServices().storeDocument(username, docName, docBytes);
		} catch (RemoteInvocationException rie) {
			throw new UnavailableServiceException();
		} catch (CannotStoreDocumentException e) {
			throw e;
		}
	}

}
