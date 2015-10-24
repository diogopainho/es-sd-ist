package pt.tecnico.bubbledocs.domain;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import pt.tecnico.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.local.CreateUser;

public class CreateUserTest extends BubbleDocsServiceTest {

	// the tokens
	private static String root;
	private static String ars;

	private static final String USERNAME = "ars";
	private static final String EMAIL = "ars@tecnico.pt";
	private static final String ROOT_USERNAME = "root";
	private static final String USERNAME_DOES_NOT_EXIST = "no-one";

	@Override
	public void populate4Test() {
		createUser(USERNAME, EMAIL, "António Rito Silva");
		root = addUserToSession(ROOT_USERNAME);
		ars = addUserToSession("ars");
	}

	@Test
	public void success() throws Exception {

		CreateUser service = new CreateUser(root, USERNAME_DOES_NOT_EXIST,
				"sucesstest@test.com", "José Ferreira");
		service.execute();

		// User is the domain class that represents a User
		User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);

		assertEquals(USERNAME_DOES_NOT_EXIST, user.getUsername());
		assertEquals("sucesstest@test.com", user.getEmail());
		assertEquals("José Ferreira", user.getName());
	}

	@Test(expected = DuplicateUsernameException.class)
	public void usernameExists() {
		CreateUser service = new CreateUser(root, USERNAME,
				"usernameExist@test.com", "José Ferreira");
		service.execute();
	}

	@Test(expected = UnauthorizedOperationException.class)
	public void unauthorizedUserCreation() throws Exception {
		CreateUser service = new CreateUser(ars, USERNAME_DOES_NOT_EXIST,
				EMAIL, "José Ferreira");
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void accessUsernameNotExist() {
		removeUserFromSession(root);
		CreateUser service = new CreateUser(root, USERNAME_DOES_NOT_EXIST,
				"usernamedoesnotexist@test.com", "José Ferreira");
		service.execute();
	}

	@Test(expected = InvalidUsernameException.class)
	public void usernameTooBig() {
		CreateUser service = new CreateUser(root, "9charname",
				"usernametoobig@test.com", "José Ferreira");
		service.execute();
	}

	@Test(expected = InvalidUsernameException.class)
	public void usernameTooSmall() {
		CreateUser service = new CreateUser(root, "2c",
				"usernametoosmall@test.com", "José Ferreira");
		service.execute();
	}

	@Test(expected = InvalidEmailException.class)
	public void invalidEmail() {
		CreateUser service = new CreateUser(root, "arra", "ars.com",
				"José Ferreira");
		service.execute();
	}

	@Test(expected = DuplicateEmailException.class)
	public void duplicateEmail() throws Exception {
		CreateUser service1 = new CreateUser(root, "arra1",
				"dupemail@test.com", "José Ferreira");
		CreateUser service2 = new CreateUser(root, "arra2",
				"dupemail@test.com", "José Ferreira");

		service1.execute();
		service2.execute();
	}
}
