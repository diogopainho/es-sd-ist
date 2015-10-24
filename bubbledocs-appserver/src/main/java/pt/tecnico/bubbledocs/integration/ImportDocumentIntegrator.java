package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.local.GetUsername4TokenService;
import pt.tecnico.bubbledocs.service.local.ImportDocument;

public class ImportDocumentIntegrator extends BubbleDocsIntegrator {
	private ImportDocument importService;
	private String username;
	private String userToken;
	private String docName;

	public ImportDocumentIntegrator(String userToken, int docId) {
		GetUsername4TokenService service = new GetUsername4TokenService(
				userToken);
		service.execute();

		importService = new ImportDocument(userToken, docId);
		this.userToken = userToken;
		this.username = service.getUserName();
		this.docName = Integer.toString(docId);
	}

	@Override
	public void execute() throws BubbleDocsException {
		byte[] loadedDoc;
		if (importService.validateSession(userToken)) {
			try {
				loadedDoc = getStoreRemoteServices().loadDocument(username,
						docName);
				if (loadedDoc.length > 0) {
					importService.setDocumentByteArray(loadedDoc);
				} else {
					importService.setDocumentByteArray(null);
				}
				importService.execute();
			} catch (RemoteInvocationException rie) {
				throw new UnavailableServiceException();
			} catch (CannotLoadDocumentException e) {
				throw e;
			}
		} else {
			throw new UserNotInSessionException(username);
		}
	}

}
