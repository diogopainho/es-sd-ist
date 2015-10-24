package pt.tecnico.bubbledocs.sdstore.cli;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

public class SDStoreMain {
	static KeyGenerator keyGen;

	public static void main(String[] args) {
		try {
			keyGen = KeyGenerator.getInstance("DES");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Check arguments
		if (args.length < 2) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL name%n",
					SDStoreClient.class.getName());
			return;
		}

		String uddiURL = args[0];
		String name = args[1];

		System.out.println("name: " + name);
		System.out.println("uddiURL: " + uddiURL);
	}

}
