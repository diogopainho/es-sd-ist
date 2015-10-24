package pt.tecnico.bubbledocs.service.local;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

// add needed import declarations

public class GetUserInfoService extends BubbleDocsService {
	private String username;
	private String userEmail;
	private String userName;

	public GetUserInfoService(String username) {
		this.username = username;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {

		User user = BubbleDocs.getInstance().getUserByUsername(username);
		this.userEmail = user.getEmail();
		this.userName = user.getName();
	}

	public String getUserEmail() {
		return userEmail;
	}

	public String getUserName() {
		return userName;
	}
}