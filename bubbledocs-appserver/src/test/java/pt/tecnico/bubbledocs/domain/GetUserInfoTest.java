package pt.tecnico.bubbledocs.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.service.local.GetUserInfoService;

public class GetUserInfoTest extends BubbleDocsServiceTest {
	private static final String USERNAME = "ars";
	private static final String EMAIL = "pop4test1@test.com";
	private static final String NAME = "Antonio Rito Silva";
	private static final String USERNAME_DOESNT_EXIST = "DOESNT EXIST";

	private User user;

	@Override
	public void populate4Test() {
		user = createUser(USERNAME, EMAIL, NAME);
	}

	@Test
	public void success() {
		GetUserInfoService service = new GetUserInfoService(USERNAME);
		service.execute();
		
		assertEquals(user.getEmail(), service.getUserEmail());
		assertEquals(user.getName(), service.getUserName());
	}
	
	@Test(expected = LoginBubbleDocsException.class)
	public void userNameDoesNotExist() {
		GetUserInfoService service = new GetUserInfoService(USERNAME_DOESNT_EXIST);
		service.execute();
	}
}
