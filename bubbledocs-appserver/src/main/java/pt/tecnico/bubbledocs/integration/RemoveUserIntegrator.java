package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.local.CreateUser;
import pt.tecnico.bubbledocs.service.local.DeleteUser;
import pt.tecnico.bubbledocs.service.local.GetUserInfoService;

public class RemoveUserIntegrator extends BubbleDocsIntegrator {

	private DeleteUser deleteuserservice;
	private String todeleteusername;
	private String usertoken;
	private String useremail;
	private String username;

	public RemoveUserIntegrator(String userToken, String toDeleteUsername) {
		this.todeleteusername = toDeleteUsername;
		this.usertoken = userToken;
		deleteuserservice = new DeleteUser(usertoken, todeleteusername);
		GetUserInfoService userinfo = new GetUserInfoService(
				this.todeleteusername);
		userinfo.execute();
		useremail = userinfo.getUserEmail();
		username = userinfo.getUserName();

	}

	@Override
	public void execute() throws BubbleDocsException {
		deleteuserservice.execute();
		try {
			getIDRemoteServices().removeUser(this.todeleteusername);
		} catch (RemoteInvocationException rie) {
			new CreateUser(this.usertoken, this.todeleteusername,
					this.useremail, this.username).execute();
			throw new UnavailableServiceException();
		} catch (InvalidUsernameException | UnauthorizedOperationException e) {
			new CreateUser(this.usertoken, this.todeleteusername,
					this.useremail, this.username).execute();
			throw e;
		}

	}
}
