package pt.tecnico.bubbledocs.sdstore.ws.handler;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import java.security.Key;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import pt.tecnico.bubbledocs.sdstore.security.SymKey;

/**
 * This is the handler client class of the Relay example.
 *
 * #2 The client handler receives data from the client (via message context). #3
 * The client handler passes data to the server handler (via outbound SOAP
 * message header).
 *
 * *** GO TO server handler to see what happens next! ***
 *
 * #10 The client handler receives data from the server handler (via inbound
 * SOAP message header). #11 The client handler passes data to the client (via
 * message context).
 *
 * *** GO BACK TO client to see what happens next! ***
 */

public class RelayClientHandler implements SOAPHandler<SOAPMessageContext> {

	public static final String REQUEST_PROPERTY_TICKET = "my.request.ticket";
	public static final String REQUEST_PROPERTY_AUTH = "my.request.username";
	public static final String REQUEST_PROPERTY_REQ = "my.request.req";
	public static final String RESPONSE_PROPERTY = "my.response.property";
	public static final String REQUEST_KEY = "my.request.key";

	public static final String REQUEST_HEADER = "myResponseHeader";
	public static final String REQUEST_HEADER_TICKET = "myTicketHeader";
	public static final String REQUEST_HEADER_REQ = "myRequestHeader";
	public static final String REQUEST_HEADER_REQ_MAC = "myRequestMACHeader";

	public static final String REQUEST_HEADER_AUTH = "myAuthHeader";
	public static final String REQUEST_NS = "urn:pt.tecnico.ulisboa.bubbledocs.essd";

	public static final String RESPONSE_HEADER = "myResponseHeader";
	public static final String RESPONSE_NS = REQUEST_NS;

	public static final String CLASS_NAME = RelayClientHandler.class
			.getSimpleName();

	public static final Key key = null;

	public boolean handleMessage(SOAPMessageContext smc) {
		Boolean outbound = (Boolean) smc
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outbound) {
			// outbound message

			// *** #2 ***
			// get token from request context
			String propertyValueTicket = (String) smc
					.get(REQUEST_PROPERTY_TICKET);
			String propertyValueUser = (String) smc.get(REQUEST_PROPERTY_AUTH);
			String propertyValueReq = (String) smc.get(REQUEST_PROPERTY_REQ);
			Key key = (Key) smc.get(REQUEST_KEY);

			// put token in request SOAP header
			try {
				// get SOAP envelope
				SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();

				// add header
				SOAPHeader sh = se.getHeader();
				if (sh == null)
					sh = se.addHeader();

				// * #3 *
				// add header element value
				if (propertyValueTicket != null && propertyValueUser != null
						&& propertyValueReq != null) {

					Date date = new Date();
					String authentication = propertyValueUser + ";"
							+ date.getHours() + date.getMinutes();

					byte[] encryptedAuth = null;
					byte[] valueReqMAC = null;
					byte[] valueReq = null;
					try {

						encryptedAuth = SymKey.encryptBytesWithKey(key,
								authentication.getBytes());

						valueReq = SymKey.encryptBytesWithKey(key,
								propertyValueReq.getBytes());

						// make mac with request
						valueReqMAC = SymKey.makeMAC(
								propertyValueReq.getBytes(), key);

					} catch (Exception e1) {
						e1.printStackTrace();
					}

					SOAPElement ticket = sh.addChildElement(
							REQUEST_HEADER_TICKET, "e", REQUEST_NS);
					SOAPElement auth = sh.addChildElement(REQUEST_HEADER_AUTH,
							"e", REQUEST_NS);
					SOAPElement requestMAC = sh.addChildElement(
							REQUEST_HEADER_REQ_MAC, "e", REQUEST_NS);
					SOAPElement request = sh.addChildElement(
							REQUEST_HEADER_REQ, "e", REQUEST_NS);

					ticket.addTextNode(propertyValueTicket);
					auth.addTextNode(printBase64Binary(encryptedAuth));
					requestMAC.addTextNode(printBase64Binary(valueReqMAC));
					request.addTextNode(printBase64Binary(valueReq));
				}

			} catch (SOAPException e) {
				System.out.printf("Failed to add SOAP header because of %s%n",
						e);
			}

		} else {
			// inbound message

			// get token from response SOAP header
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
				Name name = se.createName(RESPONSE_HEADER, "e", RESPONSE_NS);
				Iterator it = sh.getChildElements(name);
				// check header element
				if (!it.hasNext()) {
					System.out.printf("Header element %s not found.%n",
							RESPONSE_HEADER);
					return true;
				}
				SOAPElement element = (SOAPElement) it.next();

				// *** #10 ***
				// get header element value
				String headerValue = element.getValue();
				System.out.printf("%s got '%s'%n", CLASS_NAME, headerValue);

				// *** #11 ***
				// put token in response context
				smc.put(RESPONSE_PROPERTY, headerValue);
				// set property scope to application so that client class can
				// access property
				smc.setScope(RESPONSE_PROPERTY, Scope.APPLICATION);

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
