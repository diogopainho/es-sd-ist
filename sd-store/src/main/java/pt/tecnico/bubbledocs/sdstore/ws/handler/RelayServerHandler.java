package pt.tecnico.bubbledocs.sdstore.ws.handler;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

import java.security.Key;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import pt.tecnico.bubbledocs.sdstore.security.SymKey;

/**
 * This is the handler server class of the Relay example.
 *
 * #4 The server handler receives data from the client handler (via inbound SOAP
 * message header). #5 The server handler passes data to the server (via message
 * context).
 *
 * *** GO TO server class to see what happens next! ***
 *
 * #8 The server class receives data from the server (via message context). #9
 * The server handler passes data to the client handler (via outbound SOAP
 * message header).
 *
 * *** GO BACK TO client handler to see what happens next! ***
 */

public class RelayServerHandler implements SOAPHandler<SOAPMessageContext> {

	public static final String REQUEST_PROPERTY_TICKET = "my.request.ticket";
	public static final String REQUEST_PROPERTY_AUTH = "my.request.auth";
	public static final String REQUEST_PROPERTY_REQ = "my.request.req";
	public static final String RESPONSE_PROPERTY = "my.response.property";
	public static final String TREQ_PROPERTY = "my.treq.property";

	public static final String REQUEST_HEADER_TICKET = "myTicketHeader";
	public static final String REQUEST_HEADER_REQ = "myRequestHeader";
	public static final String REQUEST_HEADER_REQ_MAC = "myRequestMACHeader";
	public static final String REQUEST_HEADER_AUTH = "myAuthHeader";
	public static final String REQUEST_NS = "urn:pt.tecnico.ulisboa.bubbledocs.essd";

	public static final String RESPONSE_HEADER = "myResponseHeader";
	public static final String RESPONSE_NS = REQUEST_NS;

	public static final String CLASS_NAME = RelayServerHandler.class
			.getSimpleName();
	public static final String TOKEN = "server-handler";

	public boolean handleMessage(SOAPMessageContext smc) {
		Boolean outbound = (Boolean) smc
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outbound) {
			// outbound message

			// *** #8 ***
			// get token from response context
			String propertyValue = (String) smc.get(RESPONSE_PROPERTY);

			if (propertyValue != null) {

				// put token in response SOAP header
				try {
					// get SOAP envelope
					SOAPMessage msg = smc.getMessage();
					SOAPPart sp = msg.getSOAPPart();
					SOAPEnvelope se = sp.getEnvelope();

					// add header
					SOAPHeader sh = se.getHeader();
					if (sh == null)
						sh = se.addHeader();

					// add header element (name, namespace prefix, namespace)
					Name name = se
							.createName(RESPONSE_HEADER, "e", RESPONSE_NS);
					SOAPHeaderElement element = sh.addHeaderElement(name);

					// *** #9 ***
					// add header element value
					element.addTextNode(propertyValue);

				} catch (SOAPException e) {
					System.out.printf(
							"Failed to add SOAP header because of %s%n", e);
				}
			}

		} else {
			// inbound message
			// get token from request SOAP header
			try {
				// get SOAP envelope header
				SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				SOAPHeader sh = se.getHeader();

				// check header
				if (sh == null) {
					System.out.println("Header not found.");
					return true;
				}

				// get first header element
				Name nameTicket = se.createName(REQUEST_HEADER_TICKET, "e",
						REQUEST_NS);
				Name nameUser = se.createName(REQUEST_HEADER_AUTH, "e",
						REQUEST_NS);
				Name nameReq = se.createName(REQUEST_HEADER_REQ, "e",
						REQUEST_NS);
				Name nameReqMAC = se.createName(REQUEST_HEADER_REQ_MAC, "e",
						REQUEST_NS);

				Iterator itTicket = sh.getChildElements(nameTicket);
				// check header element
				if (!itTicket.hasNext()) {
					System.out.printf("Header element %s not found.%n",
							REQUEST_HEADER_TICKET);
					return true;
				}

				Iterator itUser = sh.getChildElements(nameUser);
				// check header element
				if (!itUser.hasNext()) {
					System.out.printf("Header element %s not found.%n",
							REQUEST_HEADER_AUTH);
					return true;
				}

				Iterator itReq = sh.getChildElements(nameReq);
				// check header element
				if (!itReq.hasNext()) {
					System.out.printf("Header element %s not found.%n",
							REQUEST_HEADER_REQ);
					return true;
				}

				Iterator itReqMAC = sh.getChildElements(nameReqMAC);
				// check header element
				if (!itReq.hasNext()) {
					System.out.printf("Header element %s not found.%n",
							REQUEST_HEADER_REQ_MAC);
					return true;
				}

				// *** #4 ***
				// get header element value
				SOAPElement elementTicket = (SOAPElement) itTicket.next();
				String headerValueTicket = elementTicket.getValue();
				// System.out.printf("%s got '%s'%n", CLASS_NAME,
				// headerValueTicket);

				// get header element value
				SOAPElement elementAuth = (SOAPElement) itUser.next();
				String headerValueAuth = elementAuth.getValue();
				// System.out.printf("%s got '%s'%n", CLASS_NAME,
				// headerValueAuth);

				// get header element value
				SOAPElement elementReq = (SOAPElement) itReq.next();
				String headerValueReq = elementReq.getValue();
				// System.out.printf("%s got '%s'%n", CLASS_NAME,
				// headerValueReq);

				// get header element value
				SOAPElement elementReqMAC = (SOAPElement) itReqMAC.next();
				String headerValueReqMAC = elementReqMAC.getValue();
				// System.out.printf("%s got '%s'%n", CLASS_NAME,
				// headerValueReqMAC);

				String path = System.getProperty("user.dir");
				path.substring(0);
				String parentPath = path.substring(0, path.length() - 8);
				parentPath += "sd-id/src/main/resources/storeKey.key";

				Key key = null;
				String ticket = null;
				String auth = null;
				String request = null;
				Boolean response = null;

				try {
					key = SymKey.read(parentPath);
					ticket = SymKey.decryptBytesWithKey(key,
							parseBase64Binary(headerValueTicket));

					String parse[] = ticket.split(";");
					String keyStoreClient = parse[4];
					Key kcs = SymKey.createKeyFromString(keyStoreClient
							.getBytes());

					auth = SymKey.decryptBytesWithKey(kcs,
							parseBase64Binary(headerValueAuth));

					request = SymKey.decryptBytesWithKey(kcs,
							parseBase64Binary(headerValueReq));

					response = SymKey.verifyMAC(
							parseBase64Binary(headerValueReqMAC),
							request.getBytes(), kcs);

					String ticketParse[] = ticket.split(";");
					int tBegin = Integer.parseInt(ticketParse[2]); // 1047

					int tPeriod = Integer.parseInt(ticketParse[3]); // 200

					String authParse[] = auth.split(";");
					int authTime = Integer.parseInt(authParse[1]); // 1139

					if ((tBegin + tPeriod) < authTime) {
						response = false;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				// *** #5 ***
				// put token in request context
				smc.put(RESPONSE_PROPERTY, response);
				// set property scope to application so that server class can
				// access property
				smc.setScope(RESPONSE_PROPERTY, Scope.APPLICATION);

				// put token in request context
				smc.put(REQUEST_PROPERTY_AUTH, headerValueAuth);
				// set property scope to application so that server class can
				// access property
				smc.setScope(REQUEST_PROPERTY_AUTH, Scope.APPLICATION);

			} catch (SOAPException e) {
				System.out.printf("Failed to get SOAP header because of %s%n",
						e);
			}

		}

		return true;
	}

	public boolean handleFault(SOAPMessageContext smc) {
		return true;
	}

	public Set<QName> getHeaders() {
		return null;
	}

	public void close(MessageContext messageContext) {
	}

}
