package pt.tecnico.bubbledocs.sdstore.ws;

import java.util.Scanner;

import javax.xml.ws.Endpoint;

import sdstore.ws.uddi.UDDINaming;

public class SDStoreMain {

	public static void main(String[] args) {
		// Check arguments
		if (args.length < 2) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s url%n",
					SDStoreMain.class.getName());
			return;
		}

		String uddiURL = args[0];
		String name = args[1];

		Endpoint endpoint = null;
		UDDINaming uddiNaming = null;

		// Para ler o porto onde ira ser lançado o endpoint (deve variar na gama
		// dos 8000 ate aos 9000 excepto 8081
		Scanner in = new Scanner(System.in);

		System.out.printf("Enter port address: ");
		int i = in.nextInt();
		String url = "http://localhost:" + i + "/sdstore-ws/endpoint";

		System.out.printf("Enter Service Identification Number (1-N):  ");
		int serviceNum = in.nextInt();
		String serviceName = name + "-" + serviceNum;

		try {
			endpoint = Endpoint.create(new SDStoreImpl());

			// publish endpoint
			System.out.printf("Starting %s%n", url);
			endpoint.publish(url);

			// publish to UDDI
			System.out.printf("Publishing '%s' to UDDI at %s%n", serviceName,
					uddiURL);
			uddiNaming = new UDDINaming(uddiURL);
			uddiNaming.rebind(serviceName, url);

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
					in.close();
					// stop endpoint
					endpoint.stop();
					System.out.printf("Stopped %s%n", url);
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when stopping: %s%n", e);
			}
			try {
				if (uddiNaming != null
						&& url == "http://localhost.8082/endpoint") {
					// delete from UDDI
					uddiNaming.unbind(serviceName);
					System.out.printf("Deleted '%s' from UDDI%n", serviceName);
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when deleting: %s%n", e);
			}
		}

	}

}
