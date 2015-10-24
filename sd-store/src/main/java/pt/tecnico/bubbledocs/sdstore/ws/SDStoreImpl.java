package pt.tecnico.bubbledocs.sdstore.ws;

import java.util.List;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import pt.tecnico.bubbledocs.sdstore.app.SDStoreApp;
import pt.tecnico.bubbledocs.sdstore.domain.Document;
import pt.tecnico.bubbledocs.sdstore.domain.Repository;
import pt.tecnico.bubbledocs.sdstore.ws.handler.RelayServerHandler;
import pt.tecnico.bubbledocs.sdstore.ws.handler.TagServerHandler;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

@WebService(endpointInterface = "pt.ulisboa.tecnico.sdis.store.ws.SDStore", wsdlLocation = "SD-STORE.1_1.wsdl", name = "SdStore", portName = "SDStoreImplPort", targetNamespace = "urn:pt:ulisboa:tecnico:sdis:store:ws", serviceName = "SDStore")
@HandlerChain(file = "/handler-chain.xml")
public class SDStoreImpl implements SDStore {

	SDStoreApp sdstore = SDStoreApp.getInstance();

	@Resource
	private WebServiceContext webServiceContext;

	public void createDoc(DocUserPair docUserPair)
			throws DocAlreadyExists_Exception {

		if (docUserPair.getDocumentId() != null
				&& docUserPair.getUserId() != null) {
			Document newdoc = new Document(docUserPair.getDocumentId());
			Repository rep = sdstore.getRepoFromUser(docUserPair.getUserId());

			if (rep != null)
				rep.addDocumentToList(newdoc);
			else {
				sdstore.addRepository(docUserPair.getUserId());
				rep = sdstore.getRepoFromUser(docUserPair.getUserId());
				rep.addDocumentToList(newdoc);
			}
		} else {
			System.out
					.println("O user ou o id do documento recebido estao vazios.");
		}
	}

	public List<String> listDocs(String userId)
			throws UserDoesNotExist_Exception {
		if (sdstore.getRepoFromUser(userId) != null) {
			return sdstore.getRepoFromUser(userId).listDocuments();
		}
		throw new UserDoesNotExist_Exception("O utilizador com o nome "
				+ userId + " nao existe, ou nao tem um repositorio associado",
				new UserDoesNotExist());

	}

	public byte[] load(DocUserPair docUserPair)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {

		byte[] result;

		// retrieve message context
		MessageContext messageContext = webServiceContext.getMessageContext();

		if (sdstore.getRepoFromUser(docUserPair.getUserId()) != null
				&& sdstore.getRepoFromUser(docUserPair.getUserId())
						.existsDocument(docUserPair.getDocumentId())) {

			result = sdstore.getRepoFromUser(docUserPair.getUserId())
					.loadDocument(docUserPair.getDocumentId());
			int sequenceNum = sdstore.getRepoFromUser(docUserPair.getUserId())
					.getDocument(docUserPair.getDocumentId()).getSeq();

			long clientId = sdstore.getRepoFromUser(docUserPair.getUserId())
					.getDocument(docUserPair.getDocumentId()).getCid();

			// put token in message context
			String seqValue = sequenceNum + "";
			String cidValue = clientId + "";

			messageContext.put(TagServerHandler.RESPONSE_SEQ, seqValue);
			messageContext.put(TagServerHandler.RESPONSE_CID, cidValue);

			messageContext.put(RelayServerHandler.RESPONSE_PROPERTY, "true");

			return result;

		} else {
			try {
				System.out.println("A registar o documento no repositorio...");
				createDoc(docUserPair);
				System.out.println("Documento registado.");

				if (sdstore.getRepoFromUser(docUserPair.getUserId()) != null) {

					result = sdstore.getRepoFromUser(docUserPair.getUserId())
							.loadDocument(docUserPair.getDocumentId());
					int sequenceNum = sdstore
							.getRepoFromUser(docUserPair.getUserId())
							.getDocument(docUserPair.getDocumentId()).getSeq();

					long clientId = sdstore
							.getRepoFromUser(docUserPair.getUserId())
							.getDocument(docUserPair.getDocumentId()).getCid();

					// put token in message context
					String seqValue = sequenceNum + "";
					String cidValue = clientId + "";

					messageContext.put(TagServerHandler.RESPONSE_SEQ, seqValue);
					messageContext.put(TagServerHandler.RESPONSE_CID, cidValue);

					return result;

				} else {
					// Isto tambem nunca devera de chegar aqui
					System.out.println("Erro fatal no server!");
				}

			} catch (DocAlreadyExists_Exception e) {
				// Nunca deve de acontecer!!!
				e.printStackTrace();
			}
		}
		return null;

	}

	public void store(DocUserPair docUserPair, byte[] contents)
			throws DocDoesNotExist_Exception, CapacityExceeded_Exception,
			UserDoesNotExist_Exception {

		// retrieve message context
		MessageContext messageContext = webServiceContext.getMessageContext();

		int seqValue = Integer.parseInt((String) messageContext
				.get(TagServerHandler.REQUEST_SEQ));
		long clientIdValue = Long.parseLong((String) messageContext
				.get(TagServerHandler.REQUEST_CID));

		if (sdstore.getRepoFromUser(docUserPair.getUserId()) != null
				&& sdstore.getRepoFromUser(docUserPair.getUserId())
						.existsDocument(docUserPair.getDocumentId())) {

			if (biggerTag(seqValue, clientIdValue,
					sdstore.getRepoFromUser(docUserPair.getUserId())
							.getDocument(docUserPair.getDocumentId()).getSeq(),
					sdstore.getRepoFromUser(docUserPair.getUserId())
							.getDocument(docUserPair.getDocumentId()).getCid())) {

				sdstore.getRepoFromUser(docUserPair.getUserId()).storeDocument(
						docUserPair.getDocumentId(), contents);

				sdstore.getRepoFromUser(docUserPair.getUserId())
						.getDocument(docUserPair.getDocumentId())
						.setCid(clientIdValue);
				sdstore.getRepoFromUser(docUserPair.getUserId())
						.getDocument(docUserPair.getDocumentId())
						.setSeq(seqValue);

			}
		} else {
			try {
				System.out.println("A registar o documento no repositorio...");
				createDoc(docUserPair);
				System.out.println("Documento registado.");
				if (sdstore.getRepoFromUser(docUserPair.getUserId()) != null) {

					if (biggerTag(seqValue, clientIdValue, sdstore
							.getRepoFromUser(docUserPair.getUserId())
							.getDocument(docUserPair.getDocumentId()).getSeq(),
							sdstore.getRepoFromUser(docUserPair.getUserId())
									.getDocument(docUserPair.getDocumentId())
									.getCid())) {

						sdstore.getRepoFromUser(docUserPair.getUserId())
								.storeDocument(docUserPair.getDocumentId(),
										contents);

						sdstore.getRepoFromUser(docUserPair.getUserId())
								.getDocument(docUserPair.getDocumentId())
								.setCid(clientIdValue);
						sdstore.getRepoFromUser(docUserPair.getUserId())
								.getDocument(docUserPair.getDocumentId())
								.setSeq(seqValue);

						messageContext.put(
								RelayServerHandler.RESPONSE_PROPERTY, "true");

					}
				} else {
					// Isto tambem nunca devera de chegar aqui
					System.out.println("Erro fatal no server!");
				}

			} catch (DocAlreadyExists_Exception e) {
				// Nunca deve de acontecer!!!
				e.printStackTrace();
			}
		}

	}

	// Return true if the tag1(seq1,cid1) is bigger than the tag2(seq2, cid2).
	// Else returns false.
	public boolean biggerTag(int seq1, long cid1, int seq2, long cid2) {
		if (seq1 == seq2) {
			if (cid1 > cid2)
				return true;
		}

		if (seq1 > seq2) {
			return true;
		} else
			return false;

	}

}
