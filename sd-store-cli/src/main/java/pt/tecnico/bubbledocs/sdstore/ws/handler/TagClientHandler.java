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

public class TagClientHandler implements SOAPHandler<SOAPMessageContext> {

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

	public static final String CLASS_NAME = TagClientHandler.class
			.getSimpleName();

	public boolean handleMessage(SOAPMessageContext smc) {
		Boolean outbound = (Boolean) smc
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outbound) {
			// outbound message

			// *** #2 ***
			// get tokens from request context
			String seqValue = (String) smc.get(REQUEST_SEQ);

			String cidValue = (String) smc.get(REQUEST_CID);

			if (seqValue == null || cidValue == null)
				return true;

			System.out.printf("%s will send to the server the seq = '%s'%n",
					CLASS_NAME, seqValue);
			System.out.printf("%s will send to the server the cid = '%s'%n",
					CLASS_NAME, cidValue);

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

				// add headers elements (name, namespace prefix, namespace)
				Name name_seq = se.createName(REQUEST_HEADER_SEQ, "e",
						REQUEST_NS);
				SOAPHeaderElement element_seq = sh.addHeaderElement(name_seq);

				Name name_cid = se.createName(REQUEST_HEADER_CID, "e",
						REQUEST_NS);
				SOAPHeaderElement element_cid = sh.addHeaderElement(name_cid);

				// *** #3 ***
				// add header elements values
				String newValue = seqValue;
				element_seq.addTextNode(newValue);

				newValue = cidValue;
				element_cid.addTextNode(newValue);

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
				Name name_seq = se.createName(RESPONSE_HEADER_SEQ, "e",
						RESPONSE_NS);
				Name name_cid = se.createName(RESPONSE_HEADER_CID, "e",
						RESPONSE_NS);

				Iterator it = sh.getChildElements(name_seq);
				// check header element
				if (!it.hasNext()) {
					System.out.printf("Header element %s not found.%n",
							RESPONSE_HEADER_SEQ);
					return true;
				}
				SOAPElement element_seq = (SOAPElement) it.next();

				it = sh.getChildElements(name_cid);
				// check header element
				if (!it.hasNext()) {
					System.out.printf("Header element %s not found.%n",
							RESPONSE_HEADER_CID);
					return true;
				}
				SOAPElement element_cid = (SOAPElement) it.next();

				// *** #10 ***
				// get header element value
				String seqValue = element_seq.getValue();
				String cidValue = element_cid.getValue();

				if (seqValue == null || cidValue == null)
					return true;

				System.out.printf("%s received from server the seq = '%s'%n",
						CLASS_NAME, seqValue);
				System.out.printf("%s received from server the cid = '%s'%n",
						CLASS_NAME, cidValue);

				// *** #11 ***
				// put token in response context
				String newValue = seqValue;

				smc.put(RESPONSE_SEQ, newValue);
				smc.setScope(RESPONSE_SEQ, Scope.APPLICATION);

				newValue = cidValue;

				smc.put(RESPONSE_CID, newValue);
				smc.setScope(RESPONSE_CID, Scope.APPLICATION);
				// set property scope to application so that client class can
				// access property

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
