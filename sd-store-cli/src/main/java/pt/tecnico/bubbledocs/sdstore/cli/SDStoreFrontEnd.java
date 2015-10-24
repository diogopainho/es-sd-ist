package pt.tecnico.bubbledocs.sdstore.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Response;

import pt.tecnico.bubbledocs.sdstore.ws.handler.TagClientHandler;
import pt.tecnico.bubbledocs.sdstore.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.store.ws.CreateDocResponse;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.ListDocsResponse;
import pt.ulisboa.tecnico.sdis.store.ws.LoadResponse;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import pt.ulisboa.tecnico.sdis.store.ws.StoreResponse;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class SDStoreFrontEnd {
	private static int N = 3;
	private static double RT = 0.6;
	private static double WT = 0.6;

	// creates stub
	private SDStore_Service service = null;
	private SDStore port = null;
	private BindingProvider bindingProvider = null;
	private String baseServiceName;
	private String uddiURL;

	private long _clientID;

	public SDStore getFrontEndPort() {
		return this.port;
	}

	public SDStoreFrontEnd(String name, String url) {
		service = new SDStore_Service();
		port = service.getSDStoreImplPort();
		baseServiceName = name;
		uddiURL = url;
		bindingProvider = (BindingProvider) port;
		UUID myuuid = UUID.randomUUID();
		_clientID = Math.abs(myuuid.getMostSignificantBits());
	}

	public List<String> listServers() {
		List<String> urlCollection = new ArrayList<String>();
		String endpointAddress = null;
		UDDINaming uddiNaming = null;

		System.out.printf("Contacting UDDI at %s%n", this.uddiURL);

		try {
			uddiNaming = new UDDINaming(this.uddiURL);
		} catch (JAXRException e) {
			System.out.println("O UDDINaming nao esta disponivel");
			e.printStackTrace();
		}
		for (int i = 1; i <= N; i++) {
			String serviceName = baseServiceName + "-" + i;

			System.out.printf("Looking for '%s'%n", serviceName);

			try {
				endpointAddress = uddiNaming.lookup(serviceName);
			} catch (JAXRException e) {
				System.out.println("O UDDINaming nao esta disponivel");
				e.printStackTrace();
			}
			urlCollection.add(endpointAddress);

			if (endpointAddress == null) {
				System.out.println("Not found!");
				return null;
			} else {
				System.out.printf("Found %s%n", endpointAddress);
			}
		}
		return urlCollection;

	}

	public void createDoc(DocUserPair pair) throws DocAlreadyExists_Exception {

		List<String> urlCollection = listServers();
		List<Response<CreateDocResponse>> allResponses = new ArrayList<Response<CreateDocResponse>>();

		for (String url : urlCollection) {
			Map<String, Object> requestContext = bindingProvider
					.getRequestContext();
			System.out.println("Setting endpoint for address " + url);
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);
			// port.createDoc(pair);
			allResponses.add(port.createDocAsync(pair));
		}
		int Q = (int) Math.round((RT * N));
		// int Q = 2;
		int numResp = 0;

		while (Q > numResp) {

			List<Response<CreateDocResponse>> done = new ArrayList<Response<CreateDocResponse>>();

			for (Response<CreateDocResponse> r : allResponses) {
				if (r.isDone()) {
					// file was created
					done.add(r);
					numResp++;
				}
			}
			allResponses.removeAll(done);

			// Se nao receber respostas suficientes para terminar faz uma pausa.
			if (Q > numResp) {
				System.out.println("Vou dormir por 100 milisegundos...");
				try {
					Thread.sleep(100 /* milliseconds */);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public List<String> listDocs(String userId)
			throws UserDoesNotExist_Exception {

		List<List<String>> docList = new ArrayList<List<String>>();
		List<String> urlCollection = listServers();
		List<Response<ListDocsResponse>> allResponses = new ArrayList<Response<ListDocsResponse>>();

		for (String url : urlCollection) {

			Map<String, Object> requestContext = bindingProvider
					.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);
			// return port.listDocs(userId);
			allResponses.add(port.listDocsAsync(userId));
		}
		int Q = (int) Math.round((RT * N));
		// int Q = 2;
		int numResp = 0;

		while (Q > numResp) {

			List<Response<ListDocsResponse>> done = new ArrayList<Response<ListDocsResponse>>();

			for (Response<ListDocsResponse> r : allResponses) {
				if (r.isDone()) {
					try {
						docList.add(r.get().getDocumentId());
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}

					done.add(r);
					numResp++;
				}
			}
			allResponses.removeAll(done);

			// Se nao receber respostas suficientes para terminar faz uma pausa.
			if (Q > numResp) {
				System.out.println("Vou dormir por 100 milisegundos...");
				try {
					Thread.sleep(100 /* milliseconds */);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		List<String> resultList = new ArrayList<String>();

		// return the list with the most files
		for (List<String> list : docList) {
			if (list.size() > resultList.size()) {
				resultList = list;
			}
		}

		return resultList;
	}

	@SuppressWarnings("deprecation")
	public byte[] load(DocUserPair docUserPair)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {

		// Assinala quando o processo se iniciou.
		Date startDate = new Date();

		List<String> urlCollection = listServers();
		List<Response<LoadResponse>> allResponses = new ArrayList<Response<LoadResponse>>();

		int Q = (int) Math.round((RT * N));
		int numResp = 0;
		int seqNum = -1;
		long clientId = -1;
		byte[] loadedBytes = null;

		// Envia os requests
		for (String url : urlCollection) {
			Map<String, Object> requestContext = bindingProvider
					.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);

			// asynchronous call with polling
			allResponses.add(port.loadAsync(docUserPair));
		}

		while (Q > numResp) {

			List<Response<LoadResponse>> done = new ArrayList<Response<LoadResponse>>();

			for (Response<LoadResponse> r : allResponses) {
				if (r.isDone()) {
					System.out
							.println("\nAsynchronous load call result arrived...");
					Map<String, Object> responseContext = r.getContext();

					int responseSeq = Integer.parseInt((String) responseContext
							.get(TagClientHandler.RESPONSE_SEQ));
					long responseCid = Long.parseLong((String) responseContext
							.get(TagClientHandler.RESPONSE_CID));

					if (biggerTag(responseSeq, responseCid, seqNum, clientId)) {
						seqNum = responseSeq;
						clientId = responseCid;
						try {
							loadedBytes = r.get().getContents();
						} catch (InterruptedException e) {
							System.out.println("Caught interrupted exception.");
							System.out.print("Cause: ");
							e.printStackTrace();
						} catch (ExecutionException e) {
							System.out.println("Caught execution exception.");
							System.out.print("Cause: ");
							e.printStackTrace();
						}

					}

					done.add(r);
					numResp++;
				}
			}
			allResponses.removeAll(done);

			// Se nao receber respostas suficientes para terminar faz uma pausa.
			if (Q > numResp) {
				System.out.println("Vou dormir por 100 milisegundos...");
				try {
					Thread.sleep(100 /* milliseconds */);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Acordei.");
			}

			// Se passaram mais de 60 segundos sem o metodo retornar um
			// resultado
			// e' atirada uma excepcao de timeout.
			if (Q > numResp && startDate.getSeconds() > 60) {
				// TODO
				// ATIRAR EXCEPCAO!!!!
			}
		}

		// Executa o a fase de writeback...
		writeback(seqNum, clientId, loadedBytes, docUserPair, urlCollection);

		return loadedBytes;
	}

	private void writeback(int maxSeq, long maxCid, byte[] contents,
			DocUserPair docUserPair, List<String> urlCollection) {

		System.out.println("A fase de writeback iniciou...");

		// Assinala quando o processo se iniciou.
		Date startDate = new Date();

		List<Response<StoreResponse>> allStoreResponses = new ArrayList<Response<StoreResponse>>();

		int Q = (int) Math.round((WT * N));

		// Numero de ACKs recebidos para as chamadas de write
		int numResp = 0;

		System.out.println("STORE: Write requests ");

		// Envia os requests de write
		for (String url : urlCollection) {
			Map<String, Object> requestContext = bindingProvider
					.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);

			requestContext.put(TagClientHandler.REQUEST_CID, maxCid);
			requestContext.put(TagClientHandler.REQUEST_SEQ, maxSeq);

			// asynchronous call with polling
			allStoreResponses.add(port.storeAsync(docUserPair, contents));
		}

		while (Q > numResp) {
			List<Response<StoreResponse>> done = new ArrayList<Response<StoreResponse>>();
			for (Response<StoreResponse> r : allStoreResponses) {
				if (r.isDone()) {
					System.out
							.println("\nAsynchronous store call result arrived...");

					done.add(r);

					numResp++;
				}
			}
			allStoreResponses.removeAll(done);

			// Se nao receber respostas suficientes para terminar faz uma pausa.
			if (Q > numResp) {
				System.out.println("Vou dormir por 100 milisegundos...");
				try {
					Thread.sleep(100 /* milliseconds */);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Acordei.");
			}

			// Se passaram mais de 60 segundos sem o metodo retornar um
			// resultado e' atirada uma excepcao de timeout.
			if (Q > numResp && startDate.getSeconds() > 60) {
				// TODO
				// ATIRAR EXCEPCAO!!!!
			}
		}

		System.out.println("A fase de writeback terminou.");
	}

	@SuppressWarnings("deprecation")
	public void store(DocUserPair docUserPair, byte[] contents)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {

		// Assinala quando o processo se iniciou.
		Date startDate = new Date();

		List<String> urlCollection = listServers();
		List<Response<LoadResponse>> allLoadResponses = new ArrayList<Response<LoadResponse>>();
		List<Response<StoreResponse>> allStoreResponses = new ArrayList<Response<StoreResponse>>();

		int Q = (int) Math.round((WT * N));
		int numResp = 0;
		int seqNum = -1;
		long clientId = -1;

		System.out.println("STORE: Read requests ");

		// Envia os requests de read
		for (String url : urlCollection) {
			Map<String, Object> requestContext = bindingProvider
					.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);

			// asynchronous call with polling
			allLoadResponses.add(port.loadAsync(docUserPair));
		}

		while (Q > numResp) {
			List<Response<LoadResponse>> done = new ArrayList<Response<LoadResponse>>();
			for (Response<LoadResponse> r : allLoadResponses) {
				if (r.isDone()) {
					System.out
							.println("\nAsynchronous read call result arrived...");

					Map<String, Object> responseContext = r.getContext();

					int responseSeq = Integer.parseInt((String) responseContext
							.get(TagClientHandler.RESPONSE_SEQ));
					long responseCid = Long.parseLong((String) responseContext
							.get(TagClientHandler.RESPONSE_CID));

					if (biggerTag(responseSeq, responseCid, seqNum, clientId)) {
						seqNum = responseSeq;
						clientId = responseCid;
					}

					done.add(r);

					numResp++;

				}
			}
			allLoadResponses.removeAll(done);

			// Se nao receber respostas suficientes para terminar faz uma pausa.
			if (Q > numResp) {
				System.out.println("Vou dormir por 100 milisegundos...");
				try {
					Thread.sleep(100 /* milliseconds */);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Acordei.");
			}

			// Se passaram mais de 60 segundos sem o metodo retornar um
			// resultado e' atirada uma excepcao de timeout.
			if (Q > numResp && startDate.getSeconds() > 60) {
				// TODO
				// ATIRAR EXCEPCAO!!!!
			}
		}

		int newSeq = seqNum + 1;

		String requestCID = _clientID + "";
		String requestSeq = newSeq + "";

		System.out.println("STORE: Write requests ");

		// Envia os requests de write
		for (String url : urlCollection) {
			Map<String, Object> requestContext = bindingProvider
					.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);

			requestContext.put(TagClientHandler.REQUEST_CID, requestCID);
			requestContext.put(TagClientHandler.REQUEST_SEQ, requestSeq);

			// asynchronous call with polling
			allStoreResponses.add(port.storeAsync(docUserPair, contents));
		}

		// Numero de ACKs recebidos para as chamadas de write
		numResp = 0;

		while (Q > numResp) {
			List<Response<StoreResponse>> done = new ArrayList<Response<StoreResponse>>();
			for (Response<StoreResponse> r : allStoreResponses) {
				if (r.isDone()) {
					System.out
							.println("\nAsynchronous store call result arrived...");

					done.add(r);

					numResp++;
				}
			}
			allStoreResponses.removeAll(done);

			// Se nao receber respostas suficientes para terminar faz uma pausa.
			if (Q > numResp) {
				System.out.println("Vou dormir por 100 milisegundos...");
				try {
					Thread.sleep(100 /* milliseconds */);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Acordei.");
			}

			// Se passaram mais de 60 segundos sem o metodo retornar um
			// resultado e' atirada uma excepcao de timeout.
			if (Q > numResp && startDate.getSeconds() > 60) {
				// TODO
				// ATIRAR EXCEPCAO!!!!
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
