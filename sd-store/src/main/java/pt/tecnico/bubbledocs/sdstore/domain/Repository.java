package pt.tecnico.bubbledocs.sdstore.domain;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;

public class Repository {
	private long size;
	private List<Document> documentList;

	public Repository() {
		this.size = 0;
		this.documentList = new ArrayList<Document>();
	}

	public Repository(long cap) {
		this.size = 0;
		this.documentList = new ArrayList<Document>();
	}

	public long getSize() {
		return size;
	}

	public void setSize(long newsize) {
		this.size = newsize;
	}

	public Document getDocument(String docid) throws DocDoesNotExist_Exception {
		for (Document d : documentList) {
			if (d.getDocID().equals(docid)) {
				return d;
			}
		}
		throw new DocDoesNotExist_Exception("O documento com o nome " + docid
				+ " nao existe", new DocDoesNotExist());
	}

	public boolean existsDocument(String docid)
			throws DocDoesNotExist_Exception {
		for (Document d : documentList) {
			if (d.getDocID().equals(docid)) {
				return true;
			}
		}
		return false;
	}

	public List<Document> getAllDocuments() {
		return documentList;
	}

	public void addDocumentToList(Document newdoc)
			throws DocAlreadyExists_Exception {
		boolean exists = false;
		for (Document d : documentList) {
			if (d.getDocID().equals(newdoc.getDocID())) {
				exists = true;
				break;
			}
		}
		if (!exists)
			documentList.add(newdoc);
		else
			throw new DocAlreadyExists_Exception("O documento com o nome "
					+ newdoc.getDocID() + " ja existe", new DocAlreadyExists());
	}

	public List<String> listDocuments() {
		List<String> list = new ArrayList<String>();
		for (Document d : documentList) {
			list.add(d.getDocID());

		}
		return list;
	}

	public byte[] loadDocument(String docid) throws DocDoesNotExist_Exception {
		for (Document d : documentList) {
			if (d.getDocID().equals(docid)) {
				return d.getContent();
			}
		}
		throw new DocDoesNotExist_Exception("O documento com o nome " + docid
				+ "nao existe", new DocDoesNotExist());
	}

	public void storeDocument(String docid, byte[] content)
			throws DocDoesNotExist_Exception {

		Document doc = getDocument(docid);

		long newsize = this.getSize() - doc.getDocSize() + content.length;

		doc.setContent(content);
		this.setSize(newsize);
	}

}
