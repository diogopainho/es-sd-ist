//package pt.tecnico.bubbledocs.sdid.test;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.fail;
//
//import java.nio.charset.Charset;
//
//import javax.xml.ws.WebServiceException;
//
//import mockit.Expectations;
//import mockit.Mocked;
//
//import org.junit.Test;
//
//import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
//import pt.ulisboa.tecnico.sdis.id.ws.CreateUser;
//import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
//import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
//import pt.ulisboa.tecnico.sdis.id.ws.RemoveUser;
//import pt.ulisboa.tecnico.sdis.id.ws.RenewPassword;
//import pt.ulisboa.tecnico.sdis.id.ws.SDId;
//import pt.ulisboa.tecnico.sdis.id.ws.SDId_Service;
//import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists;
//import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist;
//import pt.ulisboa.tecnico.sdis.id.ws.UsernameProblem;
//import sdid.cli.SDIdClient;
//
//import com.sun.xml.ws.client.ClientTransportException;
//
//public class SDIdClientContractTest {
//
//	@Test(expected = WebServiceException.class)
//	public void testMockServerException(@Mocked final SDId_Service service,
//			@Mocked final SDId port) throws Exception {
//
//		new Expectations() {
//			{
//				new SDId_Service();
//				service.getSDIdImplPort();
//				result = port;
//				port.createUser((CreateUser) any);
//				result = new WebServiceException("fabricated");
//			}
//		};
//
//		SDIdClient client = new SDIdClient();
//		CreateUser user = new CreateUser();
//		user.setUserId("mike");
//		user.setEmailAddress("mike@tecnico.pt");
//		client.createUser(user);
//	}
//
//	@Test
//	public void testMockServerExceptionOnSecondCall(
//			@Mocked final SDId_Service service, @Mocked final SDId port)
//			throws Exception {
//
//		new Expectations() {
//			{
//				new SDId_Service();
//				service.getSDIdImplPort();
//				result = port;
//				port.createUser((CreateUser) any);
//				result = null;
//				port.removeUser((RemoveUser) any);
//				result = new WebServiceException("fabricated");
//			}
//		};
//
//		SDIdClient client = new SDIdClient();
//		try {
//			CreateUser user = new CreateUser();
//			user.setUserId("mike");
//			user.setEmailAddress("mike@tecnico.pt");
//			client.createUser(user);
//		} catch (WebServiceException e) {
//			fail();
//		}
//		try {
//			RemoveUser removeUser = new RemoveUser();
//			removeUser.setUserId("john");
//			client.removeUser(removeUser);
//			fail();
//		} catch (WebServiceException e) {
//			assertEquals("fabricated", e.getMessage());
//		}
//	}
//
//	@Test(expected = ClientTransportException.class)
//	public void testMockClientTransportException(
//			@Mocked final SDId_Service service, @Mocked final SDId port)
//			throws Exception {
//
//		new Expectations() {
//			{
//				new SDId_Service();
//				service.getSDIdImplPort();
//				result = port;
//				port.createUser((CreateUser) any);
//				Throwable any = new Throwable();
//				result = new ClientTransportException(any);
//			}
//		};
//
//		SDIdClient client = new SDIdClient();
//		CreateUser user = new CreateUser();
//		user.setUserId("mike");
//		user.setEmailAddress("mike@tecnico.pt");
//		client.createUser(user);
//	}
//
//	@Test
//	public void testMockServerUserDoesNotExistException(
//			@Mocked final SDId_Service service, @Mocked final SDId port)
//			throws Exception {
//
//		new Expectations() {
//			{
//				new SDId_Service();
//				service.getSDIdImplPort();
//				result = port;
//				port.renewPassword((RenewPassword) any);
//				result = new UserDoesNotExist("fabricated",
//						new UsernameProblem());
//			}
//		};
//
//		SDIdClient client = new SDIdClient();
//		RenewPassword pass = new RenewPassword();
//		pass.setUserId("mike");
//
//		try {
//			client.renewPassword(pass);
//			fail();
//		} catch (UserDoesNotExist e) {
//			// exception is expected
//			assertEquals("fabricated", e.getMessage());
//		}
//	}
//
//	@Test
//	public void testMockServerAuthenticationFailedException(
//			@Mocked final SDId_Service service, @Mocked final SDId port)
//			throws Exception {
//
//		new Expectations() {
//			{
//				new SDId_Service();
//				service.getSDIdImplPort();
//				result = port;
//				port.requestAuthentication(anyString, (byte[]) any);
//				result = new AuthReqFailed_Exception("fabricated", null);
//			}
//		};
//
//		SDIdClient client = new SDIdClient();
//		String pass = "password";
//		byte[] b = pass.getBytes(Charset.forName("UTF-8"));
//		try {
//			client.requestAuthentication("mike", b);
//			fail();
//		} catch (AuthReqFailed_Exception e) {
//			// exception is expected
//			assertEquals("fabricated", e.getMessage());
//		}
//	}
//
//	@Test
//	public void testMockEmailAlreadyExistsException(
//			@Mocked final SDId_Service service, @Mocked final SDId port)
//			throws Exception {
//
//		new Expectations() {
//			{
//				new SDId_Service();
//				service.getSDIdImplPort();
//				result = port;
//				port.createUser((CreateUser) any);
//				result = new EmailAlreadyExists_Exception("fabricated", null);
//			}
//		};
//
//		SDIdClient client = new SDIdClient();
//		CreateUser user = new CreateUser();
//		user.setUserId("mike");
//		user.setEmailAddress("mike@tecnico.pt");
//
//		try {
//			client.createUser(user);
//			fail();
//		} catch (EmailAlreadyExists_Exception e) {
//			// exception is expected
//			assertEquals("fabricated", e.getMessage());
//		}
//	}
//
//	@Test
//	public void testMockInvalidEmailException(
//			@Mocked final SDId_Service service, @Mocked final SDId port)
//			throws Exception {
//
//		new Expectations() {
//			{
//				new SDId_Service();
//				service.getSDIdImplPort();
//				result = port;
//				port.createUser((CreateUser) any);
//				result = new InvalidEmail_Exception("fabricated", null);
//			}
//		};
//
//		SDIdClient client = new SDIdClient();
//		CreateUser user = new CreateUser();
//		user.setUserId("mike");
//		user.setEmailAddress("mike@tecnico.pt");
//
//		try {
//			client.createUser(user);
//			fail();
//		} catch (InvalidEmail_Exception e) {
//			// exception is expected
//			assertEquals("fabricated", e.getMessage());
//		}
//	}
//
//	@Test
//	public void testMockUserAlreadyExistsException(
//			@Mocked final SDId_Service service, @Mocked final SDId port)
//			throws Exception {
//
//		new Expectations() {
//			{
//				new SDId_Service();
//				service.getSDIdImplPort();
//				result = port;
//				port.createUser((CreateUser) any);
//				result = new UserAlreadyExists("fabricated", null);
//			}
//		};
//
//		SDIdClient client = new SDIdClient();
//		CreateUser user = new CreateUser();
//		user.setUserId("mike");
//		user.setEmailAddress("mike@tecnico.pt");
//
//		try {
//			client.createUser(user);
//			fail();
//		} catch (UserAlreadyExists e) {
//			// exception is expected
//			assertEquals("fabricated", e.getMessage());
//		}
//	}
//
//	@Test
//	public void testMockServerUserDoesNotExistException2(
//			@Mocked final SDId_Service service, @Mocked final SDId port)
//			throws Exception {
//
//		new Expectations() {
//			{
//				new SDId_Service();
//				service.getSDIdImplPort();
//				result = port;
//				port.removeUser((RemoveUser) any);
//				result = new UserDoesNotExist("fabricated",
//						new UsernameProblem());
//			}
//		};
//
//		SDIdClient client = new SDIdClient();
//		RemoveUser user = new RemoveUser();
//		user.setUserId("mike");
//
//		try {
//			client.removeUser(user);
//			fail();
//		} catch (UserDoesNotExist e) {
//			// exception is expected
//			assertEquals("fabricated", e.getMessage());
//		}
//	}
// }
