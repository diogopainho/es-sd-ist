package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.CellDoesNotExistException;
import pt.tecnico.bubbledocs.exception.CellIsLockedException;
import pt.tecnico.bubbledocs.exception.InvalidReferenceException;
import pt.tecnico.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.AssignReferenceToCellIntegrator;

public class AssignReferenceCellIT extends BubbleDocsIT {
	private static final String USERNAME = "ars";
	private static final String USERNAME_WITHOUT_PERMISSION = "maj";
	private static final String EMAIL_1 = "pop4test1@test.com";
	private static final String EMAIL_2 = "pop4test2@test.com";
	private static final String REFERENCE = "3;3";
	private static final String CELLID = "2;2";
	private static final String SHEET_NAME = "calc";
	private static final int SHEET_ROWS = 5;
	private static final int SHEET_COLUMNS = 5;
	private static final String LITERAL_VALUE = "6";
	private static int SHEETID;

	private String ars;
	private String maj;

	@Override
	public void populate4Test() {
		User user = createUser(USERNAME, EMAIL_1, "António Rito Silva");
		createUser(USERNAME_WITHOUT_PERMISSION, EMAIL_2,
				"Manuela Albertina Jasus");
		addUserToSession("root");
		ars = addUserToSession(USERNAME);
		maj = addUserToSession(USERNAME_WITHOUT_PERMISSION);
		SHEETID = createSpreadSheet(user, SHEET_NAME, SHEET_ROWS, SHEET_COLUMNS)
				.getId();
	}

	@Test
	public void sucess() {
		AssignReferenceToCellIntegrator integrator = new AssignReferenceToCellIntegrator(ars, SHEETID,
				CELLID, REFERENCE);
		integrator.execute();

		BubbleDocs bd = BubbleDocs.getInstance();

		User user = bd.getUserByToken(ars);
		SpreadSheet ss = bd.getSpreadSheetById(SHEETID);

		String[] parse = CELLID.split(";");
		String[] parseRef = REFERENCE.split(";");
		Reference ref = (Reference) ss.getCellByCoordinates(
				Integer.parseInt(parse[0]), Integer.parseInt(parse[1]))
				.getContent();
		assertEquals(ars, user.getToken());
		assertEquals(SHEETID, ss.getId());
		assertEquals(Integer.parseInt(parseRef[0]), ref.getReferencedCell()
				.getLine());
		assertEquals(Integer.parseInt(parseRef[1]), ref.getReferencedCell()
				.getColumn());
		assertEquals(integrator.getResult(), "#VALUE");
	}

	@Test
	public void successWithReferencedContent() {
		AssignReferenceToCellIntegrator integrator = new AssignReferenceToCellIntegrator(ars, SHEETID,
				CELLID, REFERENCE);
		integrator.execute();

		BubbleDocs bd = BubbleDocs.getInstance();
		SpreadSheet ss = bd.getSpreadSheetById(SHEETID);
		bd.addLiteralToCell(ars, SHEETID, REFERENCE, LITERAL_VALUE);

		User user = bd.getUserByToken(ars);

		String[] parse = CELLID.split(";");
		String[] parseRef = REFERENCE.split(";");
		Reference ref = (Reference) ss.getCellByCoordinates(
				Integer.parseInt(parse[0]), Integer.parseInt(parse[1]))
				.getContent();
		assertEquals(ars, user.getToken());
		assertEquals(SHEETID, ss.getId());
		assertEquals(Integer.parseInt(parseRef[0]), ref.getReferencedCell()
				.getLine());
		assertEquals(Integer.parseInt(parseRef[1]), ref.getReferencedCell()
				.getColumn());
		assertEquals(integrator.getResult(), LITERAL_VALUE);
	}

	@Test(expected = CellIsLockedException.class)
	public void cellIsLocked() {
		String[] parse = CELLID.split(";");
		BubbleDocs
				.getInstance()
				.getSpreadSheetById(SHEETID)
				.lockCell(Integer.parseInt(parse[0]),
						Integer.parseInt(parse[1]));
		AssignReferenceToCellIntegrator integrator = new AssignReferenceToCellIntegrator(ars, SHEETID,
				CELLID, REFERENCE);
		integrator.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		removeUserFromSession(ars);
		AssignReferenceToCellIntegrator integrator = new AssignReferenceToCellIntegrator(ars, SHEETID,
				CELLID, REFERENCE);
		integrator.execute();
	}

	@Test(expected = UnauthorizedOperationException.class)
	public void userHasNoPermission() {
		AssignReferenceToCellIntegrator integrator = new AssignReferenceToCellIntegrator(maj, SHEETID,
				CELLID, REFERENCE);
		integrator.execute();
	}

	@Test(expected = SpreadSheetDoesNotExistException.class)
	public void spreadSheatDoesNotExist() {
		AssignReferenceToCellIntegrator integrator = new AssignReferenceToCellIntegrator(ars, 999, CELLID,
				REFERENCE);
		integrator.execute();
	}

	@Test(expected = CellDoesNotExistException.class)
	public void cellDoesNotExist() {
		AssignReferenceToCellIntegrator integrator = new AssignReferenceToCellIntegrator(ars, SHEETID,
				"0;10", REFERENCE);
		integrator.execute();
	}

	@Test(expected = InvalidReferenceException.class)
	public void invalidReference() {
		AssignReferenceToCellIntegrator integrator = new AssignReferenceToCellIntegrator(ars, SHEETID,
				CELLID, "a");
		integrator.execute();
	}

	@Test(expected = InvalidReferenceException.class)
	public void invalidReferenceColumn() {
		AssignReferenceToCellIntegrator integrator = new AssignReferenceToCellIntegrator(ars, SHEETID,
				CELLID, "1;20");
		integrator.execute();
	}

	@Test(expected = InvalidReferenceException.class)
	public void invalidReferenceLine() {
		AssignReferenceToCellIntegrator integrator = new AssignReferenceToCellIntegrator(ars, SHEETID,
				CELLID, "20;1");
		integrator.execute();
	}
}