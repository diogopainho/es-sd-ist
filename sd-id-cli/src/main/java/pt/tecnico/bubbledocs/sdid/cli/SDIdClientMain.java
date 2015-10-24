package pt.tecnico.bubbledocs.sdid.cli;

public class SDIdClientMain {

	public static void main(String[] args) {

		// Check arguments
		if (args.length < 2) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s uddiURL name%n",
					SDIdClient.class.getName());
			return;
		}

		String uddiURL = args[0];
		String name = args[1];

		try {
			new SDIdClient(uddiURL, name);
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}
	}
}
