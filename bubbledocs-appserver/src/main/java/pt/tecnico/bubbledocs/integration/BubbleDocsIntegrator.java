package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

public abstract class BubbleDocsIntegrator {

	protected abstract void execute() throws BubbleDocsException;

	static IDRemoteServices getIDRemoteServices() {
		IDRemoteServices idremserv = new IDRemoteServices();
		return idremserv;
	}

	static StoreRemoteServices getStoreRemoteServices() {
		StoreRemoteServices storeremserv = new StoreRemoteServices();
		return storeremserv;
	}

}
