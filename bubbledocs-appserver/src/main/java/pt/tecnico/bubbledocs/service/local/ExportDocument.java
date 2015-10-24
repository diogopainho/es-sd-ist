package pt.tecnico.bubbledocs.service.local;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.PermissionDeniedException;
import pt.tecnico.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.serialize.SerializeToXML;

// add needed import declarations

public class ExportDocument extends BubbleDocsService {
	private byte[] documentByteArray;
	private String userToken;
	private int docId;

	public ExportDocument(String userToken, int docId) {
		this.setUserToken(userToken);
		this.setDocId(docId);
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

	public byte[] convertDocumentToByteArray(org.jdom2.Document doc) {
		String docString = new XMLOutputter().outputString(doc);
		return docString.getBytes();
	}

	public org.jdom2.Document convertToXML(SpreadSheet ss) {
		SerializeToXML serial = new SerializeToXML();

		org.jdom2.Document jdomDoc = serial.convertToXML(ss);

		return jdomDoc;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {

		SpreadSheet ss = getBubbleDocs().getSpreadSheetById(this.docId);
		String username = getBubbleDocs().getUserByToken(userToken)
				.getUsername();

		if (null != ss) {
			if (getBubbleDocs().validateSessionByToken(userToken)) {
				if (null != getBubbleDocs().userHasPermissionToSpreadSheet(
						username, docId)) {

					this.setDocumentByteArray(this
							.convertDocumentToByteArray(getBubbleDocs()
									.convertToXML(ss)));

				} else {
					throw new PermissionDeniedException(username);
				}
			} else {
				throw new UserNotInSessionException(username);
			}
		} else {
			throw new SpreadSheetDoesNotExistException(this.docId);
		}
	}

	public int getDocId() {
		return docId;
	}

	public org.jdom2.Document getDocument() {
		return convertByteArrayToDocument(this.getDocumentByteArray());
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

	public void setDocumentByteArray(byte[] array) {
		this.documentByteArray = array;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
}
