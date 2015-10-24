package pt.tecnico.bubbledocs.service.local;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

// add needed import declarations

public class GetUsername4TokenService extends BubbleDocsService {
	private String token;
	private String username;

	public GetUsername4TokenService(String token) {
		this.token = token;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {

		User user = BubbleDocs.getInstance().getUserByToken(token);
		this.username = user.getUsername();
	}

	public String getUserName() {
		return username;
	}
}