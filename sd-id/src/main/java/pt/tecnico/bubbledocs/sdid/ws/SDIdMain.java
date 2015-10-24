package pt.tecnico.bubbledocs.sdid.ws;

import javax.xml.ws.Endpoint;

import pt.tecnico.bubbledocs.sdid.security.SymKey;
import pt.tecnico.bubbledocs.sdid.ws.uddi.UDDINaming;

public class SDIdMain {

	public static void main(String[] args) {
		// Check arguments

		if (args.length < 3) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL wsName wsURL%n",
					SDIdMain.class.getName());
			return;
		}

		// create keyfiles for client and store service
		try {
			// SymKey.write(System.getProperty("user.dir")
			// + "/src/main/resources/clientKey.key");
			SymKey.write(System.getProperty("user.dir")
					+ "/src/main/resources/storeKey.key");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String uddiURL = args[0];
		String name = args[1];
		String url = args[2];

		Endpoint endpoint = null;
		UDDINaming uddiNaming = null;
		try {
			endpoint = Endpoint.create(new SDIdImpl());

			// publish endpoint
			System.out.printf("Starting %s%n", url);
			endpoint.publish(url);

			// publish to UDDI
			System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
			uddiNaming = new UDDINaming(uddiURL);
			uddiNaming.rebind(name, url);

			// wait
			System.out.println("Awaiting connections");
			System.out.println("Press enter to shutdown");
			System.in.read();

		} catch (Exception e) {
			System.out.printf("Caught exception: %s%n", e);
			e.printStackTrace();

		} finally {
			try {
				if (endpoint != null) {
					// stop endpoint
					endpoint.stop();
					System.out.printf("Stopped %s%n", url);
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when stopping: %s%n", e);
			}
			try {
				if (uddiNaming != null) {
					// delete from UDDI
					uddiNaming.unbind(name);
					System.out.printf("Deleted '%s' from UDDI%n", name);
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when deleting: %s%n", e);
			}
		}

	}
}
