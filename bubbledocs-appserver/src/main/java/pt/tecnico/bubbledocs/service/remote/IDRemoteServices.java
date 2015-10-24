package pt.tecnico.bubbledocs.service.remote;

import pt.tecnico.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.sdid.cli.SDIdClient;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

public class IDRemoteServices {
	// SDIdClient sdidcli = new SDIdClient("http://localhost:8081", "SD-ID");
	public SDIdClient sdidcli = new SDIdClient();

	public void createUser(String username, String email)
			throws InvalidUsernameException, DuplicateUsernameException,
			DuplicateEmailException, InvalidEmailException,
			RemoteInvocationException {
		try {
			sdidcli.createUser(username, email);
		} catch (RemoteInvocationException rie) {
			throw new UnavailableServiceException();
		} catch (EmailAlreadyExists_Exception e) {
			throw new DuplicateEmailException();
		} catch (InvalidEmail_Exception e) {
			throw new InvalidEmailException();
		} catch (InvalidUser_Exception e) {
			throw new InvalidUsernameException();
		} catch (UserAlreadyExists_Exception e) {
			throw new DuplicateUsernameException();
		}

	}

	public byte[] loginUser(String username, byte[] reserved)
			throws LoginBubbleDocsException, RemoteInvocationException {

		try {
			return sdidcli.requestAuthentication(username, reserved);
		} catch (AuthReqFailed_Exception e) {
			throw new LoginBubbleDocsException();
		} catch (RemoteInvocationException rie) {
			throw new UnavailableServiceException();
		}
	}

	public void removeUser(String username) throws LoginBubbleDocsException,
			RemoteInvocationException {

		try {
			sdidcli.removeUser(username);
		} catch (UserDoesNotExist_Exception e) {
			throw new LoginBubbleDocsException();
		} catch (RemoteInvocationException rie) {
			throw new UnavailableServiceException();
		}

	}

	public void renewPassword(String username) throws LoginBubbleDocsException,
			RemoteInvocationException {
		try {
			sdidcli.renewPassword(username);
		} catch (RemoteInvocationException rie) {
			throw new UnavailableServiceException();
		} catch (UserDoesNotExist_Exception e) {
			throw new LoginBubbleDocsException();
		}
	}

}