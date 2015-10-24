package pt.tecnico.bubbledocs.domain;

import static org.junit.Assert.*;
import org.junit.Test;

import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.local.LoginUser;

// add needed import declarations

public class LoginUserTest extends BubbleDocsServiceTest {

	private static final String USERNAME = "jpp";
	private static final String USERNAME_NO_PASS = "jmm";
	private static final String PASSWORD = "jp#";
	private static final String EMAIL = "pop4test@test.com";
	private static final String EMAIL_NO_PASS = "nopass@test.com";
	private static final String WRONG_PASSWORD = "jp!";
	private static final String WRONG_USERNAME = "jp2";

	@Override
	public void populate4Test() {
		User user = createUser(USERNAME, EMAIL, "João Pereira");
		createUser(USERNAME_NO_PASS, EMAIL_NO_PASS, "João Miguel");
		user.setPassword(PASSWORD);
	}

	@Test
	public void success() throws Exception {
		LoginUser service = new LoginUser(USERNAME, PASSWORD);
		service.execute();

		// User is the domain class that represents a User
		User user = getUserFromUsername(USERNAME);

		assertEquals(service.getUserToken(), user.getToken());
		assertNotNull(getUserFromSession(user.getToken()));
	}

	@Test
	public void successLoginTwice() throws Exception {

		LoginUser service1 = new LoginUser(USERNAME, PASSWORD);
		LoginUser service2 = new LoginUser(USERNAME, PASSWORD);

		service1.execute();
		service2.execute();

		// User is the domain class that represents a User
		User user = getUserFromUsername(USERNAME);

		assertEquals(service2.getUserToken(), user.getToken());
		assertNotNull(getUserFromSession(user.getToken()));
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void loginWithWrongPassword() throws Exception {
		LoginUser service = new LoginUser(USERNAME, WRONG_PASSWORD);
		service.execute();
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void loginWithWrongUsername() throws Exception {
		LoginUser service = new LoginUser(WRONG_USERNAME, PASSWORD);
		service.execute();
	}

	@Test(expected = UnavailableServiceException.class)
	public void loginWithNullPassword() throws Exception {
		LoginUser service = new LoginUser(USERNAME_NO_PASS, PASSWORD);
		service.execute();

	}
}
