package pt.tecnico.bubbledocs.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.local.GetUsername4TokenService;

public class GetUsername4TokenTest extends BubbleDocsServiceTest {

	private static final String USERNAME = "ars";
	private static final String NAME = "Antonio Rito Silva";
	private static final String EMAIL = "ars@tecnico.pt";
	private static final String TOKEN_DOES_NOT_EXIST = "no-one";

	// the tokens
	private static String ars;
		
	@Override
	public void populate4Test() {
		createUser(USERNAME, EMAIL, NAME);
		ars = addUserToSession("ars");
	}

	@Test
	public void success() {
		GetUsername4TokenService service = new GetUsername4TokenService(ars);
		service.execute();
		
		BubbleDocs bd = BubbleDocs.getInstance();
		
		assertEquals(USERNAME, service.getUserName());
		assertEquals(EMAIL, bd.getUserByUsername(service.getUserName()).getEmail());
		assertEquals(NAME, bd.getUserByUsername(service.getUserName()).getName());
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void sessionDoesNotExist() {
		GetUsername4TokenService service = new GetUsername4TokenService(TOKEN_DOES_NOT_EXIST);
		service.execute();
	}
}