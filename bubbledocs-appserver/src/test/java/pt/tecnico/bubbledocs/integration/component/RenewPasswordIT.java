package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertNull;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.RenewPasswordIntegrator;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPasswordIT extends BubbleDocsIT {
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

	@Test(expected = UnavailableServiceException.class)
	public void testMockRemoteInvocationException(
			@Mocked final IDRemoteServices remoteService) throws Exception {

		new Expectations() {
			{
				remoteService.renewPassword(anyString);
				result = new UnavailableServiceException();
			}
		};

		RenewPasswordIntegrator integrator = new RenewPasswordIntegrator(mp);
		integrator.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() throws Exception {

		removeUserFromSession(mp);
		RenewPasswordIntegrator integrator = new RenewPasswordIntegrator(mp);
		integrator.execute();
	}

	@Test
	public void success(@Mocked final IDRemoteServices remoteService)
			throws Exception {

		new Expectations() {
			{
				remoteService.renewPassword(anyString);
			}
		};

		RenewPasswordIntegrator integrator = new RenewPasswordIntegrator(mp);
		integrator.execute();
		assertNull(BubbleDocs.getInstance().getUserByUsername(USERNAME)
				.getPassword());
	}

}
