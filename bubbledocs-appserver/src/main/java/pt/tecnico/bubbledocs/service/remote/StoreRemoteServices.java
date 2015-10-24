package pt.tecnico.bubbledocs.service.remote;

import java.security.Key;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import pt.tecnico.bubbledocs.exception.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.exception.CannotStoreDocumentException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.sdstore.cli.SDStoreSecureClient;
import pt.tecnico.bubbledocs.sdstore.security.SymKey;
import pt.tecnico.bubbledocs.sdstore.ws.handler.RelayClientHandler;
import pt.tecnico.bubbledocs.security.Ticket;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class StoreRemoteServices {
	private Key key;
	private String path;

	public StoreRemoteServices() {
		this.path = System.getProperty("user.dir")
				+ "/src/main/resources/clientStoreKey.key";
		try {
			this.key = SymKey.read(this.path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		sdstorecli.setKey(key);
	}

	SDStoreSecureClient sdstorecli = new SDStoreSecureClient(key);

	public void storeDocument(String username, String docName, byte[] document)
			throws CannotStoreDocumentException, RemoteInvocationException {

		// access request context
		BindingProvider bindingProvider = (BindingProvider) sdstorecli
				.getPort();
		Map<String, Object> requestContext = bindingProvider
				.getRequestContext();

		// put token in request context
		requestContext.put(RelayClientHandler.REQUEST_PROPERTY_TICKET, Ticket
				.getInstance().getValue(username));
		requestContext.put(RelayClientHandler.REQUEST_PROPERTY_AUTH, username);
		requestContext.put(RelayClientHandler.REQUEST_PROPERTY_REQ, "storeDoc");
		requestContext.put(RelayClientHandler.REQUEST_KEY, key);

		DocUserPair docpair = new DocUserPair();
		docpair.setDocumentId(docName);
		docpair.setUserId(username);
		try {
			sdstorecli.storeSecure(docpair, document);
		} catch (CapacityExceeded_Exception | UserDoesNotExist_Exception
				| DocDoesNotExist_Exception e) {
			throw new CannotStoreDocumentException();
		}

	}

	public byte[] loadDocument(String username, String docName)
			throws CannotLoadDocumentException, RemoteInvocationException {

		// access request context
		BindingProvider bindingProvider = (BindingProvider) sdstorecli
				.getPort();
		Map<String, Object> requestContext = bindingProvider
				.getRequestContext();

		// put token in request context
		requestContext.put(RelayClientHandler.REQUEST_PROPERTY_TICKET, Ticket
				.getInstance().getValue(username));
		requestContext.put(RelayClientHandler.REQUEST_PROPERTY_AUTH, username);
		requestContext.put(RelayClientHandler.REQUEST_PROPERTY_REQ, "loadDoc");

		DocUserPair docpair = new DocUserPair();
		docpair.setDocumentId(docName);
		docpair.setUserId(username);
		byte[] result;
		try {
			result = sdstorecli.loadSecure(docpair);
		} catch (DocDoesNotExist_Exception | UserDoesNotExist_Exception e) {
			throw new CannotLoadDocumentException();
		}

		if (requestContext.get(RelayClientHandler.RESPONSE_PROPERTY) == "true") {
			System.out.println("GOOD");
		}

		return result;
	}
}
