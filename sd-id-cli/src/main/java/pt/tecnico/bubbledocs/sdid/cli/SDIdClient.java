package pt.tecnico.bubbledocs.sdid.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import pt.tecnico.bubbledocs.sdid.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.SDId_Service;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

public class SDIdClient {
	
	static SDId_Service service = null;
	static SDId port = null;

	public SDIdClient() {
		service = new SDId_Service();
		port = service.getSDIdImplPort();
	}

	public SDIdClient(String uddiURL, String name) {
		service = new SDId_Service();
		port = service.getSDIdImplPort();

		System.out.printf("Contacting UDDI at %s%n", uddiURL);
		UDDINaming uddiNaming;
		try {
			uddiNaming = new UDDINaming(uddiURL);

			System.out.printf("Looking for '%s'%n", name);
			String endpointAddress = uddiNaming.lookup(name);

			if (endpointAddress == null) {
				System.out.println("Not found!");
				return;
			} else {
				System.out.printf("Found %s%n", endpointAddress);
			}

			System.out.println("Setting endpoint address ...");
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider
					.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
		} catch (JAXRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static SDId_Service getService() {
		return service;
	}

	public static SDId getPort() {
		return port;
	}

	public void createUser(String userId, String emailAddress)
			throws EmailAlreadyExists_Exception, InvalidEmail_Exception,
			InvalidUser_Exception, UserAlreadyExists_Exception {
		port.createUser(userId, emailAddress);
	}

	public void renewPassword(String userId) throws UserDoesNotExist_Exception {
		port.renewPassword(userId);
	}

	public void removeUser(String userId) throws UserDoesNotExist_Exception {
		port.removeUser(userId);
	}

	public byte[] requestAuthentication(String userId, byte[] reserved)
			throws AuthReqFailed_Exception {
		System.out.println("in remote client");
		return port.requestAuthentication(userId, reserved);
	}

}
