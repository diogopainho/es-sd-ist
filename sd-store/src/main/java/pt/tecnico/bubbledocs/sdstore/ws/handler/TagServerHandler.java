package pt.tecnico.bubbledocs.sdstore.ws.handler;

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

public class TagServerHandler implements SOAPHandler<SOAPMessageContext> {

	public static final String REQUEST_SEQ = "my.request.seq";
	public static final String RESPONSE_SEQ = "my.response.seq";

	public static final String REQUEST_CID = "my.request.cid";
	public static final String RESPONSE_CID = "my.response.cid";

	public static final String REQUEST_HEADER_SEQ = "myRequestHeaderSeq";
	public static final String REQUEST_HEADER_CID = "myRequestHeaderCid";
	public static final String REQUEST_NS = "urn:pt.tecnico.ulisboa.bubbledocs.essd";

	public static final String RESPONSE_HEADER_SEQ = "myResponseHeaderSeq";
	public static final String RESPONSE_HEADER_CID = "myResponseHeaderCid";
	public static final String RESPONSE_NS = REQUEST_NS;

	public static final String CLASS_NAME = TagServerHandler.class
			.getSimpleName();

	public boolean handleMessage(SOAPMessageContext smc) {
		Boolean outbound = (Boolean) smc
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outbound) {
			// outbound message

			// *** #8 ***
			// get token from response context
			String seqValue = (String) smc.get(RESPONSE_SEQ);
			String cidValue = (String) smc.get(RESPONSE_CID);

			if (seqValue == null || cidValue == null)
				return true;

			System.out.printf("%s will send to the client the seq = '%s'%n",
					CLASS_NAME, seqValue);
			System.out.printf("%s will send to the client the cid = '%s'%n",
					CLASS_NAME, cidValue);

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
				Name nameSeq = se.createName(RESPONSE_HEADER_SEQ, "e",
						RESPONSE_NS);
				SOAPHeaderElement elementSeq = sh.addHeaderElement(nameSeq);

				Name nameCid = se.createName(RESPONSE_HEADER_CID, "e",
						RESPONSE_NS);
				SOAPHeaderElement elementCid = sh.addHeaderElement(nameCid);

				// *** #9 ***
				// add header element value
				String newValue = seqValue;
				elementSeq.addTextNode(newValue);

				newValue = cidValue;
				elementCid.addTextNode(newValue);

			} catch (SOAPException e) {
				System.out.printf("Failed to add SOAP header because of %s%n",
						e);
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
				Name nameSeq = se.createName(REQUEST_HEADER_SEQ, "e",
						REQUEST_NS);
				Name nameCid = se.createName(REQUEST_HEADER_CID, "e",
						REQUEST_NS);

				Iterator it = sh.getChildElements(nameSeq);
				// check header element
				if (!it.hasNext()) {
					System.out.printf("Header element %s not found.%n",
							REQUEST_HEADER_SEQ);
					return true;
				}
				SOAPElement elementSeq = (SOAPElement) it.next();

				it = sh.getChildElements(nameCid);
				// check header element
				if (!it.hasNext()) {
					System.out.printf("Header element %s not found.%n",
							REQUEST_HEADER_CID);
					return true;
				}
				SOAPElement elementCid = (SOAPElement) it.next();

				// *** #4 ***
				// get header element value
				String seqValue = elementSeq.getValue();
				String cidValue = elementCid.getValue();

				if (seqValue == null || cidValue == null)
					return true;

				System.out.printf(
						"%s received from the client the seq = '%s'%n",
						CLASS_NAME, seqValue);
				System.out.printf(
						"%s received from the client the cid = '%s'%n",
						CLASS_NAME, cidValue);

				// *** #5 ***
				// put token in request context
				String newValue = seqValue;

				smc.put(REQUEST_SEQ, newValue);
				// set property scope to application so that server class can
				// access property
				smc.setScope(REQUEST_SEQ, Scope.APPLICATION);

				newValue = cidValue;
				System.out.printf(
						"%s put token 'server-handler' on request context%n",
						CLASS_NAME);
				smc.put(REQUEST_CID, newValue);
				// set property scope to application so that server class can
				// access property
				smc.setScope(REQUEST_CID, Scope.APPLICATION);

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
