package pt.tecnico.bubbledocs.service.local;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;

// add needed import declarations

public class CreateUser extends BubbleDocsService {
	private String usertoken;
	private String username;
	private String userEmail;
	private String userName;

	public CreateUser(String userToken, String newUsername, String email,
			String name) {
		this.usertoken = userToken;
		this.username = newUsername;
		this.userEmail = email;
		this.userName = name;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {

		if (!getBubbleDocs().getUserByToken(usertoken).getUsername()
				.equals("root"))
			throw new UnauthorizedOperationException();

		getBubbleDocs().validateSessionByToken(usertoken);
		getBubbleDocs().addUser(new User(userName, userEmail, username));

	}
}
