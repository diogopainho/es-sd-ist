package pt.tecnico.bubbledocs.integration.component;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.CannotStoreDocumentException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.integration.ExportDocumentIntegrator;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

public class ExportDocumentIT extends BubbleDocsIT {

	private static final String USERNAME_WITH_AUTHOR_ACCESS = "amp";
	private static final String EMAIL_1 = "pop4test1@test.com";
	private static final String SPREADSHEET_NAME = "spread";

	// the tokens for the users
	private String ap;

	// Id da spreadsheet criada
	private int ssId;

	@Override
	public void populate4Test() {
		User amp = createUser(USERNAME_WITH_AUTHOR_ACCESS, EMAIL_1,
				"Alberto Manuel Pardal");

		SpreadSheet ss = createSpreadSheet(amp, SPREADSHEET_NAME, 20, 20);
		ssId = ss.getId();

		ap = addUserToSession(USERNAME_WITH_AUTHOR_ACCESS);
	};

	@Test
	public void success(@Mocked final StoreRemoteServices services)
			throws Exception {

		new Expectations() {
			{
				services.storeDocument(anyString, anyString, (byte[]) any);
			}
		};

		ExportDocumentIntegrator integrator = new ExportDocumentIntegrator(ap,
				ssId);
		integrator.execute();
	}

	@Test(expected = UnavailableServiceException.class)
	public void testMockRemoteInvocationException(
			@Mocked final StoreRemoteServices services) throws Exception {

		new Expectations() {
			{
				services.storeDocument(anyString, anyString, (byte[]) any);
				result = new RemoteInvocationException();
			}
		};

		ExportDocumentIntegrator integrator = new ExportDocumentIntegrator(ap,
				ssId);
		integrator.execute();
	}

	@Test(expected = CannotStoreDocumentException.class)
	public void testMockCannotStoreDocumentException(
			@Mocked final StoreRemoteServices services) throws Exception {

		new Expectations() {
			{
				services.storeDocument(anyString, anyString, (byte[]) any);
				result = new CannotStoreDocumentException();
			}
		};

		ExportDocumentIntegrator integrator = new ExportDocumentIntegrator(ap,
				ssId);
		integrator.execute();
	}

}
