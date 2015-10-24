package pt.tecnico.bubbledocs.integration;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

import java.security.Key;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.security.SymKey;
import pt.tecnico.bubbledocs.security.Ticket;
import pt.tecnico.bubbledocs.service.local.LoginUser;

public class LoginUserIntegrator extends BubbleDocsIntegrator {

	private LoginUser loginuserservice;
	private String username;
	private byte[] data;
	private byte[] reserved;
	private String decryptedSessionKey;
	private String ticket;

	public LoginUserIntegrator(String userName, String password, byte[] service) {
		this.username = userName;
		this.reserved = service;
		loginuserservice = new LoginUser(userName, password);
	}

	@Override
	public void execute() throws BubbleDocsException {
		try {
			this.data = (getIDRemoteServices().loginUser(this.username,
					this.reserved));

			String responseString = new String(data);

			System.out.println("parsing data");
			// parse the server response
			String parse[] = responseString.split(":");
			String encryptedSessionKey = parse[0];
			ticket = parse[1];

			byte[] hashedUserPass;
			Key keyClient;
			System.out.println("creating hash");
			// create hash using user pass from user
			hashedUserPass = SymKey
					.digestUsingMD5(parseBase64Binary(this.loginuserservice
							.getPassword()));

			System.out.println("creating key");
			// cretae Kc using hash from pass
			keyClient = SymKey.createKeyFromString(hashedUserPass);

			System.out.println("decrypting");
			// decrypt session key with Kc key
			decryptedSessionKey = SymKey.decryptBytesWithKey(keyClient,
					parseBase64Binary(encryptedSessionKey));

			// create store client key
			String parseKey[] = decryptedSessionKey.split(";");
			String keyclientstore = parseKey[0];

			Key keycs = SymKey.createKeyFromString(keyclientstore.getBytes());
			SymKey.writeKeyToFile(keycs);

			Ticket.getInstance().putValue(this.username, ticket);

			loginuserservice.setUserPassword();
		} catch (RemoteInvocationException rie) {
			loginuserservice.execute();
		} catch (LoginBubbleDocsException e) {
			throw e;
		} catch (UnavailableServiceException us) {
			throw us;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public byte[] getData() {
		return data;
	}

	public String getTicket() {
		return ticket;
	}

	public String getDecryptedSessionKey() {
		return decryptedSessionKey;
	}
}
