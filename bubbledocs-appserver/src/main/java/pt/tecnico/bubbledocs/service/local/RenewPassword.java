package pt.tecnico.bubbledocs.service.local;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class RenewPassword extends BubbleDocsService {
	private String userToken;

	public RenewPassword(String userToken) {
		this.userToken = userToken;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {

		User user = getBubbleDocs().getUserByToken(userToken);
		getBubbleDocs().validateSessionByToken(user.getToken());
		user.setPassword(null);
	}

	public final String getUserToken() {
		return userToken;
	}

}
