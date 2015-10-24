package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.local.CreateUser;
import pt.tecnico.bubbledocs.service.local.DeleteUser;

public class CreateUserIntegrator extends BubbleDocsIntegrator {
	private CreateUser createuserservice;

	private String usertoken;
	private String newusername;
	private String useremail;
	private String username;

	public CreateUserIntegrator(String userToken, String newUsername,
			String email, String name) {
		this.usertoken = userToken;
		this.newusername = newUsername;
		this.useremail = email;
		this.username = name;
		createuserservice = new CreateUser(usertoken, newusername, useremail,
				username);

	}

	@Override
	public void execute() throws BubbleDocsException {
		createuserservice.execute();
		try {
			getIDRemoteServices().createUser(this.newusername, this.useremail);
		} catch (RemoteInvocationException rie) {
			new DeleteUser(this.usertoken, this.newusername).execute();
			throw new UnavailableServiceException();
		} catch (InvalidUsernameException | DuplicateUsernameException
				| DuplicateEmailException | InvalidEmailException e) {
			new DeleteUser(this.usertoken, this.newusername).execute();
			throw e;
		}

	}

}
