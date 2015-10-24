package pt.tecnico.bubbledocs.integration.component;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.ImportDocumentIntegrator;
import pt.tecnico.bubbledocs.service.local.ExportDocument;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

public class ImportDocumentIT extends BubbleDocsIT {

	private static final String USERNAME_WITH_AUTHOR_ACCESS = "amp";
	private static final String EMAIL_1 = "pop4test1@test.com";
	private static final String SPREADSHEET_NAME = "spread";

	// the tokens for the users
	private String ap;

	// Id da spreadsheet criada
	private int ssId;

	private byte[] nhe;

	@Override
	public void populate4Test() {
		User amp = createUser(USERNAME_WITH_AUTHOR_ACCESS, EMAIL_1,
				"Alberto Manuel Pardal");

		ap = addUserToSession(USERNAME_WITH_AUTHOR_ACCESS);

		SpreadSheet ss = createSpreadSheet(amp, SPREADSHEET_NAME, 20, 20);
		ssId = ss.getId();
		ExportDocument exportDocument = new ExportDocument(ap, ssId);
		nhe = exportDocument.convertDocumentToByteArray(exportDocument
				.convertToXML(ss));

	};

	@Test
	public void success(@Mocked final StoreRemoteServices services)
			throws Exception {

		new Expectations() {
			{
				services.loadDocument(anyString, anyString);
				result = nhe;
			}
		};

		ImportDocumentIntegrator integrator = new ImportDocumentIntegrator(ap,
				ssId);
		integrator.execute();
	}

	@Test(expected = UnavailableServiceException.class)
	public void testMockRemoteInvocationException(
			@Mocked final StoreRemoteServices services) throws Exception {

		new Expectations() {
			{
				services.loadDocument(anyString, anyString);
				result = new RemoteInvocationException();
			}
		};

		ImportDocumentIntegrator integrator = new ImportDocumentIntegrator(ap,
				ssId);
		integrator.execute();
	}

	/*
	 * the service is not able to load the document
	 */
	@Test(expected = CannotLoadDocumentException.class)
	public void testMockCannotLoadDocumentException(
			@Mocked final StoreRemoteServices services) throws Exception {

		new Expectations() {
			{
				services.loadDocument(anyString, anyString);
				result = new CannotLoadDocumentException();
			}
		};

		ImportDocumentIntegrator integrator = new ImportDocumentIntegrator(ap,
				ssId);
		integrator.execute();
	}

	/*
	 * the user is not in session
	 */
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSessionException() {
		ImportDocumentIntegrator service = new ImportDocumentIntegrator(
				"abc1234", ssId);

		service.execute();
	}

}
