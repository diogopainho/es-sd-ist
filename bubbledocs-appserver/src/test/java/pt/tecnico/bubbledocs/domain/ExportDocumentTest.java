package pt.tecnico.bubbledocs.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.bubbledocs.exception.PermissionDeniedException;
import pt.tecnico.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.local.ExportDocument;

public class ExportDocumentTest extends BubbleDocsServiceTest {

	private static final String USERNAME_WITH_AUTHOR_ACCESS = "amp";
	private static final String USERNAME_WITH_WRITE_ACCESS = "mrh";
	private static final String USERNAME_WITH_READ_ACCESS = "zcs";
	private static final String USERNAME_WITHOUT_ACCESS = "lel";
	private static final String USERNAME_WITHOUT_SESSION = "cas";
	private static final String EMAIL_1 = "pop4test1@test.com";
	private static final String EMAIL_2 = "pop4test2@test.com";
	private static final String EMAIL_3 = "pop4test3@test.com";
	private static final String EMAIL_4 = "pop4test4@test.com";
	private static final String EMAIL_5 = "pop4test5@test.com";
	private static final String ROOT_USERNAME = "root";
	private static final String SPREADSHEET_NAME = "spread";

	// the tokens for the users
	private String root;
	private String ap;
	private String zcs;
	private String mrh;
	private String lel;
	private String cas;

	// Id da spreadsheet criada
	private int ssId;

	@Override
	public void populate4Test() {
		User amp = createUser(USERNAME_WITH_AUTHOR_ACCESS, EMAIL_1,
				"Alberto Manuel Pardal");
		createUser(USERNAME_WITHOUT_ACCESS, EMAIL_2, "Luis Elidio Lima");
		createUser(USERNAME_WITH_WRITE_ACCESS, EMAIL_3,
				"Marilia Rafaela da Horta");
		createUser(USERNAME_WITH_READ_ACCESS, EMAIL_4,
				"Ze Carlos Lopes da Silva");
		createUser(USERNAME_WITHOUT_SESSION, EMAIL_5,
				"Constancia Albertina dos Santos");

		SpreadSheet ss = createSpreadSheet(amp, SPREADSHEET_NAME, 20, 20);
		ssId = ss.getId();

		BubbleDocs.getInstance().addNewPermission(true, false, ssId,
				USERNAME_WITH_WRITE_ACCESS);

		BubbleDocs.getInstance().addNewPermission(false, false, ssId,
				USERNAME_WITH_READ_ACCESS);

		root = addUserToSession(ROOT_USERNAME);
		ap = addUserToSession(USERNAME_WITH_AUTHOR_ACCESS);
		mrh = addUserToSession(USERNAME_WITH_WRITE_ACCESS);
		zcs = addUserToSession(USERNAME_WITH_READ_ACCESS);
		lel = addUserToSession(USERNAME_WITHOUT_ACCESS);
	};

	/*
	 * accessUsername exists, is in session and is author of the spreadsheet
	 */
	@Test
	public void successToExportDocumentHasAuthor() {
		ExportDocument service = new ExportDocument(ap, ssId);
		service.execute();

		boolean docCreated = service.getDocument() != null;

		assertTrue("Documento nao foi criado", docCreated);
	}

	/*
	 * accessUsername exists, is in session and has root access to the
	 * spreadsheet
	 */
	@Test(expected = PermissionDeniedException.class)
	public void rootWithoutPermissionToExportDocument() {
		ExportDocument service = new ExportDocument(root, ssId);
		service.execute();

		boolean docCreated = service.getDocument() != null;

		assertTrue("Documento nao foi criado", docCreated);
	}

	/*
	 * accessUsername exists, is in session and has read access to the
	 * spreadsheet
	 */
	@Test
	public void successToExportDocumentWithReadAcess() {
		ExportDocument service = new ExportDocument(zcs, ssId);
		service.execute();

		boolean docCreated = service.getDocument() != null;

		assertTrue("Documento nao foi criado", docCreated);
	}

	/*
	 * accessUsername exists, is in session and has write access to the
	 * spreadsheet
	 */
	@Test
	public void successToExportDocumentWithWriteAcess() {
		ExportDocument service = new ExportDocument(mrh, ssId);
		service.execute();

		boolean docCreated = service.getDocument() != null;

		assertTrue("Documento nao foi criado", docCreated);
	}

	@Test(expected = UserNotInSessionException.class)
	public void notInSessionAndNotRoot() {
		new ExportDocument(cas, ssId).execute();
	}

	@Test(expected = SpreadSheetDoesNotExistException.class)
	public void spreadSheetDoesNotExist() {
		int notAssignedId = BubbleDocs.getInstance().getSpreadCount();
		new ExportDocument(zcs, notAssignedId).execute();
	}

	@Test(expected = PermissionDeniedException.class)
	public void userWithoutPermissionToExport() {
		new ExportDocument(lel, ssId).execute();
	}
}