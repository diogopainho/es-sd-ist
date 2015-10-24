package pt.tecnico.bubbledocs.domain;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.local.DeleteUser;

// add needed import declarations

public class DeleteUserTest extends BubbleDocsServiceTest {

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
	public void success() {

		DeleteUser service = new DeleteUser(root, USERNAME_TO_DELETE);
		service.execute();

		boolean deleted = getUserFromUsername(USERNAME_TO_DELETE) == null;

		assertTrue("user was not deleted", deleted);
		assertNull("Spreadsheet was not deleted",
				getSpreadSheet(SPREADSHEET_NAME));

	}

	/*
	 * accessUsername exists, is in session and is root toDeleteUsername exists
	 * and is not in session
	 */
	@Test(expected = UserNotInSessionException.class)
	public void successToDeleteIsNotInSession() {

		removeUserFromSession(root);
		DeleteUser service = new DeleteUser(root, USERNAME_TO_DELETE);
		service.execute();
	}

	/*
	 * accessUsername exists, is in session and is root toDeleteUsername exists
	 * and is in session Test if user and session are both deleted
	 */
	@Test
	public void successToDeleteIsInSession() {

		String token = addUserToSession(USERNAME_TO_DELETE);
		DeleteUser service = new DeleteUser(root, USERNAME_TO_DELETE);
		service.execute();
		assertNull("Removed user but not removed from session",
				getUserFromSession(token));
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void userToDeleteDoesNotExist() {

		DeleteUser service = new DeleteUser(root, USERNAME_DOES_NOT_EXIST);
		service.execute();
	}

	@Test(expected = UnauthorizedOperationException.class)
	public void notRootUser() {

		String ars = addUserToSession(USERNAME);
		DeleteUser service = new DeleteUser(ars, USERNAME_TO_DELETE);
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void rootNotInSession() {

		removeUserFromSession(root);

		DeleteUser service = new DeleteUser(root, USERNAME_TO_DELETE);
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void notInSessionAndNotRoot() {

		String ars = addUserToSession(USERNAME);
		removeUserFromSession(ars);
		DeleteUser service = new DeleteUser(ars, USERNAME_TO_DELETE);
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void accessUserDoesNotExist() {

		DeleteUser service = new DeleteUser(USERNAME_DOES_NOT_EXIST,
				USERNAME_TO_DELETE);
		service.execute();
	}
}
