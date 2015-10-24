package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.CreateUserIntegrator;
import pt.tecnico.bubbledocs.service.local.CreateUser;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class CreateUserIT extends BubbleDocsIT {

	private static String root;
	private static String ars;

	private static final String USERNAME = "ars";
	private static final String EMAIL = "ars@tecnico.pt";
	private static final String ROOT_USERNAME = "root";
	private static final String USERNAME_DOES_NOT_EXIST = "no-one";

	public void populate4Test() {
		createUser(USERNAME, EMAIL, "António Rito Silva");
		root = addUserToSession(ROOT_USERNAME);
		ars = addUserToSession("ars");
	}

	@Test
	public void success(@Mocked final IDRemoteServices remoteService)
			throws Exception {

		new Expectations() {
			{
				remoteService.createUser(anyString, anyString);
			}
		};

		CreateUserIntegrator integrator = new CreateUserIntegrator(root,
				USERNAME_DOES_NOT_EXIST, "sucesstest@test.com", "José Ferreira");
		integrator.execute();

		// User is the domain class that represents a User
		User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);

		assertEquals(USERNAME_DOES_NOT_EXIST, user.getUsername());
		assertEquals("sucesstest@test.com", user.getEmail());
		assertEquals("José Ferreira", user.getName());
	}

	@Test(expected = UnavailableServiceException.class)
	public void testMockRemoteInvocationException(
			@Mocked final IDRemoteServices remoteService) throws Exception {

		new Expectations() {
			{
				remoteService.createUser(anyString, anyString);
				result = new RemoteInvocationException();
			}
		};

		CreateUserIntegrator integrator = new CreateUserIntegrator(root,
				USERNAME_DOES_NOT_EXIST, "sucesstest@test.com", "José Ferreira");
		integrator.execute();

	}

	@Test
	public void successCompensation(@Mocked final IDRemoteServices remoteService)
			throws Exception {

		new Expectations() {
			{
				remoteService.createUser(anyString, anyString);
				result = new RemoteInvocationException();
			}
		};

		CreateUserIntegrator integrator = new CreateUserIntegrator(root,
				USERNAME_DOES_NOT_EXIST, "sucesscompensation@test.com", "José");
		try {
			integrator.execute();
		} catch (UnavailableServiceException e) {
			assertNull(getUserFromUsername(USERNAME_DOES_NOT_EXIST));

		}

	}

	@Test(expected = DuplicateEmailException.class)
	public void duplicateEmail(@Mocked final IDRemoteServices remoteService)
			throws Exception {

		new Expectations() {
			{
				remoteService.createUser(anyString, anyString);
			}
		};
		CreateUserIntegrator service1 = new CreateUserIntegrator(root, "arra1",
				"dupemail@test.com", "José Ferreira");
		CreateUser service2 = new CreateUser(root, "arra2",
				"dupemail@test.com", "José Ferreira");

		service1.execute();
		service2.execute();

	}

	@Test(expected = DuplicateUsernameException.class)
	public void usernameExists() {
		CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME,
				"usernameExist@test.com", "José Ferreira");
		service.execute();
	}

	@Test(expected = UnauthorizedOperationException.class)
	public void unauthorizedUserCreation() throws Exception {
		CreateUserIntegrator service = new CreateUserIntegrator(ars,
				USERNAME_DOES_NOT_EXIST, EMAIL, "José Ferreira");
		service.execute();

	}

	@Test(expected = UserNotInSessionException.class)
	public void accessUsernameNotExist() {
		removeUserFromSession(root);
		CreateUserIntegrator service = new CreateUserIntegrator(root,
				USERNAME_DOES_NOT_EXIST, "usernamedoesnotexist@test.com",
				"José Ferreira");
		service.execute();
	}

	@Test(expected = InvalidUsernameException.class)
	public void usernameTooBig() {
		CreateUserIntegrator service = new CreateUserIntegrator(root,
				"9charname", "usernametoobig@test.com", "José Ferreira");
		service.execute();

	}

	@Test(expected = InvalidUsernameException.class)
	public void usernameTooSmall() {
		CreateUserIntegrator service = new CreateUserIntegrator(root, "2c",
				"usernametoosmall@test.com", "José Ferreira");
		service.execute();

	}

	@Test(expected = InvalidEmailException.class)
	public void invalidEmail() {
		CreateUserIntegrator service = new CreateUserIntegrator(root, "arra",
				"ars.com", "José Ferreira");
		service.execute();

	}

}