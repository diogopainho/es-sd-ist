package pt.tecnico.bubbledocs.service.local;

import java.util.Random;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;

// add needed import declarations

public class LoginUser extends BubbleDocsService {

	private String userToken;
	private String username;
	private String password;

	public LoginUser(String username, String password)
			throws LoginBubbleDocsException {

		this.username = username;
		this.password = password;

	}

	@Override
	protected void dispatch() throws BubbleDocsException {

		try {
			if (getBubbleDocs().getUserByUsername(username).getPassword() == null)
				throw new UnavailableServiceException();

			Random rand = new Random(System.currentTimeMillis());
			String lastToken = getBubbleDocs().getUserByUsername(username)
					.getToken();
			int randomNum;

			if (lastToken == null) {
				randomNum = rand.nextInt(10);
				this.userToken = username.concat(Integer.toString(randomNum));
			} else {

				do {
					// Random number in the range 0-9
					randomNum = rand.nextInt(10);

					this.userToken = username.concat(Integer
							.toString(randomNum));
				} while (lastToken.equals(userToken));
			}
			getBubbleDocs().userLogin(username, password, userToken);
		} catch (LoginBubbleDocsException lbd) {
			throw new LoginBubbleDocsException(username);
		} catch (UnavailableServiceException us) {
			throw new UnavailableServiceException();
		}

	}

	public final String getUserToken() {
		return userToken;
	}

	public final String getUsername() {
		return username;
	}

	public final String getPassword() {
		return password;
	}

	@Atomic
	public void setUserPassword() {
		getBubbleDocs().getUserByUsername(this.username).setPassword(this.password);

	}

}
