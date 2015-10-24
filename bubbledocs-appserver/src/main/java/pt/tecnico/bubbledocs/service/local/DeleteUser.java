package pt.tecnico.bubbledocs.service.local;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;

// add needed import declarations

public class DeleteUser extends BubbleDocsService {

	private String todeleteusername;
	private String usertoken;

	public DeleteUser(String userToken, String toDeleteUsername) {
		this.todeleteusername = toDeleteUsername;
		this.usertoken = userToken;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {

		getBubbleDocs().validateSessionByToken(usertoken);

		if (!getBubbleDocs().getUserByToken(usertoken).getUsername()
				.equals("root")) {
			throw new UnauthorizedOperationException();
		}
		getBubbleDocs().deleteUser(todeleteusername);
	}

}
