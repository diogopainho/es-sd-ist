package pt.tecnico.bubbledocs.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.bubbledocs.exception.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.service.local.ExportDocument;
import pt.tecnico.bubbledocs.service.local.ImportDocument;

public class ImportDocumentTest extends BubbleDocsServiceTest {

	private static final String USERNAME_WITH_AUTHOR_ACCESS = "amp";
	private static final String EMAIL_1 = "pop4test1@test.com";
	private static final String SPREADSHEET_NAME = "spread";

	// the tokens for the users
	private String ap;

	// Id da spreadsheet criada
	private int ssId;

	// Bytes de um documento gerado para testar o servico de import
	private byte[] docBytes;

	@Override
	public void populate4Test() {
		User amp = createUser(USERNAME_WITH_AUTHOR_ACCESS, EMAIL_1,
				"Alberto Manuel Pardal");

		BubbleDocs bd = BubbleDocs.getInstance();

		SpreadSheet ss = createSpreadSheet(amp, SPREADSHEET_NAME, 20, 20);
		ssId = ss.getId();

		ap = addUserToSession(USERNAME_WITH_AUTHOR_ACCESS);

		// preenche-se um array de bytes com o resultado da exportacao de uma
		// spreadsheet de teste.
		ExportDocument auxService = new ExportDocument(ap, ssId);

		docBytes = auxService.convertDocumentToByteArray(bd.convertToXML(ss));

	};

	/*
	 * accessUsername exists, is in session and is author of the spreadsheet
	 */
	@Test
	public void successToImportDocumentHasAuthor() {
		ImportDocument service = new ImportDocument(ap, ssId);

		service.setDocumentByteArray(docBytes);
		service.execute();

		BubbleDocs bd = BubbleDocs.getInstance();

		boolean docCreated = bd.getSpreadSheetById(service.getDocId()) != null;

		assertTrue("Documento nao foi guardado com sucesso", docCreated);
	}

	/*
	 * the service receives an empty byte array
	 */
	@Test(expected = CannotLoadDocumentException.class)
	public void emptyDocumentReceived() {
		ImportDocument service = new ImportDocument(ap, ssId);

		service.setDocumentByteArray(null);
		service.execute();
	}
}
