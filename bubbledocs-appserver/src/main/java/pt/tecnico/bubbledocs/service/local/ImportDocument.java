package pt.tecnico.bubbledocs.service.local;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.serialize.SerializeToXML;

// add needed import declarations

public class ImportDocument extends BubbleDocsService {
	private byte[] documentByteArray;
	private String userToken;
	private int docIdRequest;
	private int docId;

	public ImportDocument(String userToken, int docId) {
		this.setUserToken(userToken);
		this.setDocIdRequest(docId);
	}

	public org.jdom2.Document convertByteArrayToDocument(byte[] doc) {
		SAXBuilder builder = new SAXBuilder();
		org.jdom2.Document document = null;
		try {
			document = builder.build(new ByteArrayInputStream(doc));
		} catch (JDOMException | IOException e) {
			System.out
					.println("Erro na conversao de byte array para documento.");
			e.printStackTrace();
		}
		return document;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		SerializeToXML serial = new SerializeToXML();

		SpreadSheet ss = new SpreadSheet();

		if (null != documentByteArray) {
			org.jdom2.Document loadedDoc = convertByteArrayToDocument(this.documentByteArray);
			ss = serial.recoverFromBackup(loadedDoc);
			this.setDocId(ss.getId());
			getBubbleDocs().addSpreadSheet(ss);

		} else {
			throw new CannotLoadDocumentException(this.docIdRequest);
		}

	}

	public int getDocId() {
		return docId;
	}

	public int getDocIdRequest() {
		return docIdRequest;
	}

	public byte[] getDocumentByteArray() {
		return documentByteArray;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	public void setDocIdRequest(int docIdRequest) {
		this.docIdRequest = docIdRequest;
	}

	public void setDocumentByteArray(byte[] documentByteArray) {
		this.documentByteArray = documentByteArray;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public boolean validateSession(String userToken) {
		return getBubbleDocs().validateSessionByToken(userToken);
	}
}