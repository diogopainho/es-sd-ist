package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.RemoveUserIntegrator;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RemoveUserIT extends BubbleDocsIT {

	private static final String USERNAME_TO_DELETE = "smf";
	private static final String USERNAME = "ars";
	private static final String ROOT_USERNAME = "root";
	private static final String USERNAME_DOES_NOT_EXIST = "no-one";
	private static final String SPREADSHEET_NAME = "spread";
	private static final String EMAIL = "pop4test@test.com";
	private static final String EMAIL_TO_DELETE = "delete@test.com";

	// the tokens for user root
	private String root;

	@Override
	public void populate4Test() {
		createUser(USERNAME, EMAIL, "António Rito Silva");
		User smf = createUser(USERNAME_TO_DELETE, EMAIL_TO_DELETE,
				"Sérgio Fernandes");
		createSpreadSheet(smf, SPREADSHEET_NAME, 20, 20);
		root = addUserToSession(ROOT_USERNAME);
	};

	@Test
	public void success(@Mocked final IDRemoteServices remoteService) {

		new Expectations() {
			{
				remoteService.removeUser(anyString);
			}
		};

		RemoveUserIntegrator integrator = new RemoveUserIntegrator(root,
				USERNAME_TO_DELETE);
		integrator.execute();

		boolean deleted = getUserFromUsername(USERNAME_TO_DELETE) == null;

		assertTrue("user was not deleted", deleted);
		assertNull("Spreadsheet was not deleted",
				getSpreadSheet(SPREADSHEET_NAME));

	}

	@Test(expected = UnavailableServiceException.class)
	public void testMockRemoteInvocationException(
			@Mocked final IDRemoteServices remoteService) throws Exception {

		new Expectations() {
			{
				remoteService.removeUser(anyString);
				result = new RemoteInvocationException();
			}
		};

		RemoveUserIntegrator integrator = new RemoveUserIntegrator(root,
				USERNAME);
		integrator.execute();

	}

	@Test
	public void successCompensation(@Mocked final IDRemoteServices remoteService)
			throws Exception {

		new Expectations() {
			{
				remoteService.removeUser(anyString);
				result = new RemoteInvocationException();
			}
		};

		RemoveUserIntegrator integrator = new RemoveUserIntegrator(root,
				USERNAME);

		try {
			integrator.execute();
		} catch (UnavailableServiceException e) {
			User user = getUserFromUsername(USERNAME);
			assertNotNull(user.getName());
			assertNotNull(user.getEmail());
		}

	}

	/*
	 * accessUsername exists, is in session and is root toDeleteUsername exists
	 * and is in session Test if user and session are both deleted
	 */
	@Test
	public void successToDeleteIsInSession(
			@Mocked final IDRemoteServices remoteService) throws Exception {

		new Expectations() {
			{
				remoteService.removeUser(anyString);
			}
		};

		String token = addUserToSession(USERNAME_TO_DELETE);
		RemoveUserIntegrator service = new RemoveUserIntegrator(root,
				USERNAME_TO_DELETE);
		service.execute();
		assertNull("Removed user but not removed from session",
				getUserFromSession(token));
	}

	/*
	 * accessUsername exists, is in session and is root toDeleteUsername exists
	 * and is not in session
	 */
	@Test(expected = UserNotInSessionException.class)
	public void successToDeleteIsNotInSession() {

		removeUserFromSession(root);
		RemoveUserIntegrator service = new RemoveUserIntegrator(root,
				USERNAME_TO_DELETE);
		service.execute();
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void userToDeleteDoesNotExist() {

		RemoveUserIntegrator service = new RemoveUserIntegrator(root,
				USERNAME_DOES_NOT_EXIST);
		service.execute();
	}

	@Test(expected = UnauthorizedOperationException.class)
	public void notRootUser() {

		String ars = addUserToSession(USERNAME);
		RemoveUserIntegrator service = new RemoveUserIntegrator(ars,
				USERNAME_TO_DELETE);
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void rootNotInSession() {

		removeUserFromSession(root);

		RemoveUserIntegrator service = new RemoveUserIntegrator(root,
				USERNAME_TO_DELETE);
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void notInSessionAndNotRoot() {

		String ars = addUserToSession(USERNAME);
		removeUserFromSession(ars);
		RemoveUserIntegrator service = new RemoveUserIntegrator(ars,
				USERNAME_TO_DELETE);
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void accessUserDoesNotExist() {

		RemoveUserIntegrator service = new RemoveUserIntegrator(
				USERNAME_DOES_NOT_EXIST, USERNAME_TO_DELETE);
		service.execute();
	}

}