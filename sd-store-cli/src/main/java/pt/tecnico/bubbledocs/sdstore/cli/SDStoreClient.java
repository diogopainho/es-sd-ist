package pt.tecnico.bubbledocs.sdstore.cli;

import java.util.List;

import javax.xml.registry.JAXRException;

import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

// classes generated from WSDL

public class SDStoreClient {

	// creates stub
	static SDStore_Service service = null;
	static SDStore port = null;
	private SDStoreFrontEnd frontend;

	public SDStoreClient() {
		frontend = new SDStoreFrontEnd("SD-STORE", "http://localhost:8081");
	}

	public SDStoreClient(String name, String uddiURL) throws JAXRException {
		frontend = new SDStoreFrontEnd(name, uddiURL);
	}

	public void createDoc(DocUserPair pair) throws DocAlreadyExists_Exception {
		frontend.createDoc(pair);
	}

	public List<String> listDocs(String userId)
			throws UserDoesNotExist_Exception {
		return frontend.listDocs(userId);
	}

	public byte[] load(DocUserPair docUserPair)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		return frontend.load(docUserPair);
	}

	public void store(DocUserPair docUserPair, byte[] contents)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		frontend.store(docUserPair, contents);
	}

	public SDStore getClientPort() {
		return frontend.getFrontEndPort();
	}

}
