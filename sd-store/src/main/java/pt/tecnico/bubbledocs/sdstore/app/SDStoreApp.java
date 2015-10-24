package pt.tecnico.bubbledocs.sdstore.app;

import java.util.HashMap;

import pt.tecnico.bubbledocs.sdstore.domain.Repository;

public class SDStoreApp {
	private static SDStoreApp sdstore;
	HashMap<String, Repository> repositoryMap = new HashMap<String, Repository>();

	public static SDStoreApp getInstance() {
		if (sdstore == null) {
			sdstore = new SDStoreApp();
		}
		return sdstore;
	}

	public void addRepository(String userId) {
		Repository rep = new Repository();
		repositoryMap.put(userId, rep);
	}

	public void addRepository(String userId, long cap) {
		Repository rep = new Repository(cap);
		repositoryMap.put(userId, rep);
	}

	public Repository getRepoFromUser(String userId) {
		return repositoryMap.get(userId);
	}

}
