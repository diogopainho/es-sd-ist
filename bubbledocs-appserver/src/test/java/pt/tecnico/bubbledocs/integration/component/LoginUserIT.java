//package pt.tecnico.bubbledocs.integration.component;
//
//import mockit.Expectations;
//import mockit.Mocked;
//
//import org.junit.Test;
//
//import pt.tecnico.bubbledocs.domain.User;
//import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
//import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
//import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
//import pt.tecnico.bubbledocs.integration.LoginUserIntegrator;
//import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
//
//public class LoginUserIT extends BubbleDocsIT {
//
//	private static final String USERNAME = "jpp";
//	private static final String USERNAME_NO_PASS = "jmm";
//	private static final String PASSWORD = "jp#";
//	private static final String EMAIL = "pop4test@test.com";
//	private static final String EMAIL_NO_PASS = "nopass@test.com";
//	private static final String WRONG_PASSWORD = "jp!";
//	private static final String WRONG_USERNAME = "jp2";
//	private static final String SERVICE = "SD-STORE;2";
//
//	@Override
//	public void populate4Test() {
//		User user = createUser(USERNAME, EMAIL, "João Pereira");
//		createUser(USERNAME_NO_PASS, EMAIL_NO_PASS, "João Miguel");
//		user.setPassword(PASSWORD);
//	}
//
//	/*
//	 * Nao sei se e necessario estar aqui porque esta exactamente igual nos
//	 * testes do domain
//	 */
//	@Test(expected = UnavailableServiceException.class)
//	public void userNoLocalPassword(@Mocked final IDRemoteServices remoteService)
//			throws Exception {
//
//		new Expectations() {
//			{
//				remoteService.loginUser(anyString, (byte[])any);
//				result = new RemoteInvocationException();
//			}
//		};
//
//		LoginUserIntegrator loginIntegrator = new LoginUserIntegrator(
//				USERNAME_NO_PASS, PASSWORD, SERVICE.getBytes());
//		loginIntegrator.execute();
//	}
//
//	@Test
//	public void successLocal(@Mocked final IDRemoteServices remoteService)
//			throws Exception {
//
//		new Expectations() {
//			{
//				remoteService.loginUser(anyString, (byte[])any);
//				result = new RemoteInvocationException();
//			}
//		};
//
//		LoginUserIntegrator integrator = new LoginUserIntegrator(USERNAME,
//				PASSWORD, SERVICE.getBytes());
//		integrator.execute();
//	}
//
//	@Test
//	public void successRemote(@Mocked final IDRemoteServices remoteService)
//			throws Exception {
//
//		new Expectations() {
//			{
//				remoteService.loginUser(anyString, (byte[])any);
//			}
//		};
//
//		LoginUserIntegrator integrator = new LoginUserIntegrator(USERNAME,
//				PASSWORD, SERVICE.getBytes());
//		integrator.execute();
//	}
//
//	@Test
//	public void successLoginTwiceLocal(
//			@Mocked final IDRemoteServices remoteService) throws Exception {
//
//		new Expectations() {
//			{
//				remoteService.loginUser(anyString, (byte[])any);
//				result = new RemoteInvocationException();
//				remoteService.loginUser(anyString, (byte[])any);
//				result = new RemoteInvocationException();
//			}
//		};
//
//		LoginUserIntegrator integrator = new LoginUserIntegrator(USERNAME,
//				PASSWORD, SERVICE.getBytes());
//		integrator.execute();
//		integrator.execute();
//	}
//
//	@Test
//	public void successLoginTwiceRemote(
//			@Mocked final IDRemoteServices remoteService) throws Exception {
//
//		new Expectations() {
//			{
//				remoteService.loginUser(anyString, (byte[])any);
//				remoteService.loginUser(anyString, (byte[])any);
//			}
//		};
//
//		LoginUserIntegrator integrator1 = new LoginUserIntegrator(USERNAME,
//				PASSWORD, SERVICE.getBytes());
//		LoginUserIntegrator integrator2 = new LoginUserIntegrator(USERNAME,
//				PASSWORD, SERVICE.getBytes());
//
//		integrator1.execute();
//		integrator2.execute();
//	}
//
//	@Test(expected = LoginBubbleDocsException.class)
//	public void loginWithWrongPasswordLocal(
//			@Mocked final IDRemoteServices remoteService) throws Exception {
//
//		new Expectations() {
//			{
//				remoteService.loginUser(anyString, (byte[])any);
//				result = new RemoteInvocationException();
//			}
//		};
//
//		LoginUserIntegrator integrator = new LoginUserIntegrator(USERNAME,
//				WRONG_PASSWORD, SERVICE.getBytes());
//		integrator.execute();
//	}
//
//	// isto deve estar mal. devia ser authreq e nao login
//	@Test(expected = LoginBubbleDocsException.class)
//	public void loginWithWrongPasswordRemote(
//			@Mocked final IDRemoteServices remoteService) throws Exception {
//
//		new Expectations() {
//			{
//				remoteService.loginUser(anyString, (byte[])any);
//				result = new LoginBubbleDocsException();
//			}
//		};
//
//		LoginUserIntegrator integrator = new LoginUserIntegrator(USERNAME,
//				WRONG_PASSWORD, SERVICE.getBytes());
//		integrator.execute();
//
//	}
//
//	@Test(expected = LoginBubbleDocsException.class)
//	public void loginWithWrongUsernameLocal(
//			@Mocked final IDRemoteServices remoteService) throws Exception {
//
//		new Expectations() {
//			{
//				remoteService.loginUser(anyString, (byte[])any);
//				result = new RemoteInvocationException();
//			}
//		};
//
//		LoginUserIntegrator integrator = new LoginUserIntegrator(
//				WRONG_USERNAME, PASSWORD, SERVICE.getBytes());
//		integrator.execute();
//	}
//
//	// isto deve estar mal. devia ser authreq e nao login
//	@Test(expected = LoginBubbleDocsException.class)
//	public void loginWithWrongUsernameRemote(
//			@Mocked final IDRemoteServices remoteService) throws Exception {
//
//		new Expectations() {
//			{
//				remoteService.loginUser(anyString, (byte[])any);
//				result = new LoginBubbleDocsException();
//			}
//		};
//
//		LoginUserIntegrator integrator = new LoginUserIntegrator(
//				WRONG_USERNAME, PASSWORD, SERVICE.getBytes());
//		integrator.execute();
//	}
//
//	@Test(expected = LoginBubbleDocsException.class)
//	public void loginWithWrongUsernameAndPasswordLocal(
//			@Mocked final IDRemoteServices remoteService) throws Exception {
//
//		new Expectations() {
//			{
//				remoteService.loginUser(anyString, (byte[])any);
//				result = new RemoteInvocationException();
//			}
//		};
//
//		LoginUserIntegrator integrator = new LoginUserIntegrator(
//				WRONG_USERNAME, WRONG_PASSWORD, SERVICE.getBytes());
//		integrator.execute();
//	}
//
//	// isto deve estar mal. devia ser authreq e nao login
//	@Test(expected = LoginBubbleDocsException.class)
//	public void loginWithWrongUsernameAndPasswordRemote(
//			@Mocked final IDRemoteServices remoteService) throws Exception {
//
//		new Expectations() {
//			{
//				remoteService.loginUser(anyString, (byte[])any);
//				result = new LoginBubbleDocsException();
//			}
//		};
//
//		LoginUserIntegrator integrator = new LoginUserIntegrator(
//				WRONG_USERNAME, WRONG_PASSWORD, SERVICE.getBytes());
//		integrator.execute();
//	}
//
// }
