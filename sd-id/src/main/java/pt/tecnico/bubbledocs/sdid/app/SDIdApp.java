package pt.tecnico.bubbledocs.sdid.app;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import pt.tecnico.bubbledocs.sdid.domain.User;

public class SDIdApp {
	private SecureRandom random = new SecureRandom();

	private static SDIdApp sdid;
	private static List<User> userList = new ArrayList<User>();

	public static SDIdApp getInstance() {
		if (sdid == null) {
			sdid = new SDIdApp();
			populateListWithUsers();
		}
		return sdid;
	}

	private static void populateListWithUsers() {

		// userID, password, email
		User u1 = new User("alice", "Aaa1", "alice@tecnico.pt");
		User u2 = new User("bruno", "Bbb2", "bruno@tecnico.pt");
		User u3 = new User("carla", "Ccc3", "carla@tecnico.pt");
		User u4 = new User("duarte", "Ddd4", "duarte@tecnico.pt");
		User u5 = new User("eduardo", "Eee5", "eduardo@tecnico.pt");

		userList.add(u1);
		userList.add(u2);
		userList.add(u3);
		userList.add(u4);
		userList.add(u5);
	}

	public String nextPassword() {
		return new BigInteger(130, random).toString(64);
	}

	public List<User> getUserList() {
		return userList;
	}

	public static void printUsers() {
		for (User u : userList) {
			System.out.println(u.getUserID() + " " + " " + u.getPassword());
		}
	}

}
