//package pt.tecnico.bubbledocs.sdid.test;
//
//import static org.junit.Assert.assertEquals;
//
//import java.nio.charset.Charset;
//import java.util.Arrays;
//
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
//import pt.ulisboa.tecnico.sdis.id.ws.CreateUser;
//import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
//import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
//import pt.ulisboa.tecnico.sdis.id.ws.RemoveUser;
//import pt.ulisboa.tecnico.sdis.id.ws.RenewPassword;
//import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists;
//import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist;
//import sdid.app.SDIdApp;
//import sdid.ws.SDIdImpl;
//
//public class SDIdImplTest {
//
//	// "alice", "Aaa1", "alice@tecnico.pt"
//	// "bruno", "Bbb2", "bruno@tecnico.pt"
//	// "carla", "Ccc3", "carla@tecnico.pt"
//	// "duarte", "Ddd4", "duarte@tecnico.pt"
//	// "eduardo", "Eee5", "eduardo@tecnico.pt"
//
//	private static SDIdImpl sdidImpl;
//
//	@BeforeClass
//	public static void oneTimeSetup() {
//		sdidImpl = new SDIdImpl();
//		SDIdApp.getInstance();
//	}
//
//	@AfterClass
//	public static void oneTimeTearDown() {
//		sdidImpl = null;
//	}
//
//	@Test(expected = EmailAlreadyExists_Exception.class)
//	public void testEmailAlreadyExists() throws EmailAlreadyExists_Exception,
//			InvalidEmail_Exception, UserAlreadyExists {
//		CreateUser user = new CreateUser();
//		user.setEmailAddress("alice@tecnico.pt");
//		user.setUserId("ana");
//		sdidImpl.createUser(user);
//	}
//
//	@Test(expected = InvalidEmail_Exception.class)
//	public void testInvalidEmail_Excpetion()
//			throws EmailAlreadyExists_Exception, InvalidEmail_Exception,
//			UserAlreadyExists {
//		CreateUser user = new CreateUser();
//		user.setEmailAddress("alice @tecnico.pt");
//		user.setUserId("ana");
//		sdidImpl.createUser(user);
//	}
//
//	@Test(expected = InvalidEmail_Exception.class)
//	public void testInvalidEmail_Excpetion2()
//			throws EmailAlreadyExists_Exception, InvalidEmail_Exception,
//			UserAlreadyExists {
//		CreateUser user = new CreateUser();
//		user.setEmailAddress("alice@");
//		user.setUserId("ana");
//		sdidImpl.createUser(user);
//	}
//
//	@Test(expected = UserAlreadyExists.class)
//	public void testUserAlreadyExists() throws EmailAlreadyExists_Exception,
//			InvalidEmail_Exception, UserAlreadyExists {
//		CreateUser user = new CreateUser();
//		user.setEmailAddress("ana@tecnico.pt");
//		user.setUserId("alice");
//		sdidImpl.createUser(user);
//	}
//
//	@Test(expected = UserDoesNotExist.class)
//	public void testUserDoesNotExist() throws UserDoesNotExist {
//		RenewPassword renew = new RenewPassword();
//		renew.setUserId("ana");
//		sdidImpl.renewPassword(renew);
//	}
//
//	@Test(expected = UserDoesNotExist.class)
//	public void testUserDoesNotExist2() throws UserDoesNotExist {
//		RemoveUser remove = new RemoveUser();
//		remove.setUserId("ana");
//		sdidImpl.removeUser(remove);
//	}
//
//	@Test(expected = AuthReqFailed_Exception.class)
//	public void testAuthReqFailed() throws AuthReqFailed_Exception {
//		String userID = "ana";
//		String password = "Aaa1";
//		sdidImpl.requestAuthentication(userID, password.getBytes());
//	}
//
//	@Test(expected = AuthReqFailed_Exception.class)
//	public void testAuthReqFailed2() throws AuthReqFailed_Exception {
//		String userID = "alice";
//		String password = "badPass";
//		sdidImpl.requestAuthentication(userID, password.getBytes());
//	}
//
//	@Test
//	public void successCreateUser() throws EmailAlreadyExists_Exception,
//			InvalidEmail_Exception, UserAlreadyExists {
//		CreateUser user = new CreateUser();
//		user.setUserId("pedro");
//		user.setEmailAddress("pedro@tecnico.pt");
//		sdidImpl.createUser(user);
//	}
//
//	@Test
//	public void successRenewPassword() throws UserDoesNotExist {
//		RenewPassword renew = new RenewPassword();
//		renew.setUserId("pedro");
//		sdidImpl.renewPassword(renew);
//	}
//
//	@Test
//	public void successRemovePassword() throws UserDoesNotExist {
//		RemoveUser remove = new RemoveUser();
//		remove.setUserId("pedro");
//		sdidImpl.removeUser(remove);
//	}
//
//	@Test
//	public void successRequestAuthentication() throws AuthReqFailed_Exception {
//		String userID = "duarte";
//		String password = "Ddd4";
//		String expected = "1";
//		byte[] result = sdidImpl.requestAuthentication(userID,
//				password.getBytes());
//		assertEquals(Arrays.equals(expected.getBytes(Charset.forName("UTF-8")),
//				result), true);
//	}
//
// }
