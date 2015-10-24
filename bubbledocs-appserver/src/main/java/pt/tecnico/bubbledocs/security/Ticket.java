package pt.tecnico.bubbledocs.security;

import java.util.HashMap;
import java.util.Map;

public class Ticket {
	private static Ticket instance;

	Map<String, String> dictionary = new HashMap<String, String>();

	private Ticket() {
	}

	public static Ticket getInstance() {

		if (instance == null) {
			instance = new Ticket();
		}
		return instance;
	}

	public void putValue(String username, String ticket) {
		dictionary.put(username, ticket);
	}

	public String getValue(String username) {
		return dictionary.get(username);
	}
}
