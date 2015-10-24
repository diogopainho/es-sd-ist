package pt.tecnico.bubbledocs.sdid.ws;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import java.security.Key;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jws.HandlerChain;
import javax.jws.WebService;

import pt.tecnico.bubbledocs.sdid.app.SDIdApp;
import pt.tecnico.bubbledocs.sdid.domain.User;
import pt.tecnico.bubbledocs.sdid.security.SymKey;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

@WebService(endpointInterface = "pt.ulisboa.tecnico.sdis.id.ws.SDId", wsdlLocation = "SD-ID.1_1.wsdl", name = "SDId", portName = "SDIdImplPort", targetNamespace = "urn:pt:ulisboa:tecnico:sdis:id:ws", serviceName = "SDId")
@HandlerChain(file = "/handler-chain.xml")
public class SDIdImpl implements SDId {
	SDIdApp sdid = SDIdApp.getInstance();

	// Returns sessionKey:Ticket, also named as Data
	public byte[] requestAuthentication(String userId, byte[] reserved)
			throws AuthReqFailed_Exception {

		// parse values sent from client: service and hours
		String values = new String(reserved);
		String parse[] = values.split(";");
		String service = parse[0];
		String hours = parse[1];

		for (User u : sdid.getUserList()) {
			if (u.getUserID().equals(userId)) {
				String userPass = u.getPassword();
				try {

					/*
					 * Ks = shared key between SD-STORE and SD-ID Kc = key
					 * created hashing user password from db
					 */

					// read Ks from file
					Key keyStore = SymKey.read(System.getProperty("user.dir")
							+ "/src/main/resources/storeKey.key");

					// create hash using user pass from db
					byte[] hashedUserPass = SymKey
							.digestUsingMD5(parseBase64Binary(userPass));

					// cretae Kc using hash from pass
					Key keyClient = SymKey.createKeyFromString(hashedUserPass);

					// concatenate Ks with Kc
					String storeClientString = printBase64Binary(keyStore
							.getEncoded())
							+ ":"
							+ printBase64Binary(keyClient.getEncoded());

					// create hash using Ks and Kc string
					byte[] hashedKeyStoreKeyClient = SymKey
							.digestUsingMD5(parseBase64Binary(storeClientString));

					// create session key to send to client: hashed Ks and Kc ;
					// hours
					String sessionKey = SymKey.createSessionKey(
							printBase64Binary(hashedKeyStoreKeyClient), hours);

					// create ticket to send to client: userId ; service ; begin
					// ; end ; hashed Ks and Kc
					String ticket = SymKey.createTicket(userId, service, hours,
							printBase64Binary(hashedKeyStoreKeyClient));

					// encrypt session key with Kc key
					byte[] encryptedSessionKey = SymKey.encryptBytesWithKey(
							keyClient, sessionKey.getBytes());

					// encrypt tickt with Ks key
					byte[] encryptedTicket = SymKey.encryptBytesWithKey(
							keyStore, ticket.getBytes());

					// concatenate encrypted session key with encrypted ticket
					String result = printBase64Binary(encryptedSessionKey)
							+ ":" + printBase64Binary(encryptedTicket);

					// send final result
					return result.getBytes();

				} catch (Exception e1) {
					System.out.print(e1);
				}

			}
		}
		throw new AuthReqFailed_Exception("Authentication failed!",
				new AuthReqFailed());

	}

	public void createUser(String userId, String emailAddress)
			throws EmailAlreadyExists_Exception, InvalidEmail_Exception,
			InvalidUser_Exception, UserAlreadyExists_Exception {

		if (emailAddress == null)
			throw new InvalidEmail_Exception("Email is empty",
					new InvalidEmail());

		if (userId == null || userId == "")
			throw new InvalidUser_Exception("User is empty", new InvalidUser());

		String password = sdid.nextPassword();

		Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+");
		Matcher mat = pattern.matcher(emailAddress);

		if (mat.matches()) {
			System.out.println("Valid email address");
		} else {
			throw new InvalidEmail_Exception("The email " + emailAddress
					+ " is not valid!", new InvalidEmail());
		}

		for (User u : sdid.getUserList()) {
			if (u.getUserID().equals(userId)) {
				throw new UserAlreadyExists_Exception("The user " + userId
						+ " already exists!", new UserAlreadyExists());
			}
			if (u.getEmail().equals(emailAddress)) {
				throw new EmailAlreadyExists_Exception("The email "
						+ emailAddress + " already exists!",
						new EmailAlreadyExists());
			}
		}

		User user = new User(userId, password, emailAddress);

		sdid.getUserList().add(user);
		System.out.println("Your password is: " + password);

	}

	public void renewPassword(String userId) throws UserDoesNotExist_Exception {

		if (userId == null)
			throw new UserDoesNotExist_Exception("User is empty",
					new UserDoesNotExist());

		for (User u : sdid.getUserList()) {
			if (u.getUserID().equals(userId)) {
				u.setPassword(sdid.nextPassword());
				System.out.println("Your new password is: " + u.getPassword());
				return;
			}
		}
		throw new UserDoesNotExist_Exception("The user " + userId
				+ " does not exist!", new UserDoesNotExist());

	}

	public void removeUser(String userId) throws UserDoesNotExist_Exception {

		if (userId == null)
			throw new UserDoesNotExist_Exception("User is empty",
					new UserDoesNotExist());

		for (User u : sdid.getUserList()) {
			if (u.getUserID().equals(userId)) {
				sdid.getUserList().remove(u);
				return;
			}
		}
		throw new UserDoesNotExist_Exception("The user " + userId
				+ " does not exist!", new UserDoesNotExist());

	}
}
