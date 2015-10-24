package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.local.GetUsername4TokenService;
import pt.tecnico.bubbledocs.service.local.RenewPassword;

public class RenewPasswordIntegrator extends BubbleDocsIntegrator {

	private RenewPassword renewpasswordservice;
	private String usertoken;
	private String userToRenewPass;

	public RenewPasswordIntegrator(String userToken) {
		this.usertoken = userToken;
		GetUsername4TokenService username4TokenService = new GetUsername4TokenService(
				this.usertoken);
		username4TokenService.execute();
		this.userToRenewPass = username4TokenService.getUserName();
		renewpasswordservice = new RenewPassword(this.usertoken);
	}

	@Override
	public void execute() throws BubbleDocsException {
		renewpasswordservice.execute();
		try {
			getIDRemoteServices().renewPassword(this.userToRenewPass);
		} catch (RemoteInvocationException rie) {
			throw new UnavailableServiceException();
		} catch (LoginBubbleDocsException e) {
			throw e;
		}
	}
}
