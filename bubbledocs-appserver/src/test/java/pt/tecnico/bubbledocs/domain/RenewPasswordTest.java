package pt.tecnico.bubbledocs.domain;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.local.RenewPassword;

public class RenewPasswordTest extends BubbleDocsServiceTest {
	private static final String USERNAME = "mscp";
	private static final String EMAIL = "pop4test@test.com";
	private static final String PASSWORD = "pass";

	private static String mp;

	@Override
	public void populate4Test() {
		User user = createUser(USERNAME, EMAIL, "Mafalda Perdigao");
		user.setPassword(PASSWORD);
		mp = addUserToSession(USERNAME);
	}

	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {

		removeUserFromSession(mp);
		RenewPassword service = new RenewPassword(mp);
		service.execute();
	}

	@Test
	public void success() {

		RenewPassword service = new RenewPassword(mp);
		service.execute();
		assertNull(BubbleDocs.getInstance().getUserByUsername(USERNAME)
				.getPassword());
	}

}