package pt.tecnico.bubbledocs.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.CellDoesNotExistException;
import pt.tecnico.bubbledocs.exception.CellIsLockedException;
import pt.tecnico.bubbledocs.exception.InvalidContentException;
import pt.tecnico.bubbledocs.exception.InvalidLiteralException;
import pt.tecnico.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.local.AssignLiteralCell;

public class AssignLiteralCellTest extends BubbleDocsServiceTest {
	private static final String USERNAME = "ars";
	private static final String USERNAME_WITHOUT_PERMISSION = "maj";
	private static final String VALUE = "30";
	private static final String CELLID = "2;2";
	private static final String SHEET_NAME = "calc";
	private static final int SHEET_ROWS = 5;
	private static final int SHEET_COLUMNS = 5;
	private static int SHEETID;

	private String ars;
	private String maj;

	@Override
	public void populate4Test() {
		User user = createUser(USERNAME, "pop4test1@test.com",
				"António Rito Silva");
		createUser(USERNAME_WITHOUT_PERMISSION, "pop4test2@test.com",
				"Manuela Albertina Jasus");
		addUserToSession("root");
		ars = addUserToSession(USERNAME);
		maj = addUserToSession(USERNAME_WITHOUT_PERMISSION);
		SHEETID = createSpreadSheet(user, SHEET_NAME, SHEET_ROWS, SHEET_COLUMNS)
				.getId();
	}

	@Test
	public void success() throws NumberFormatException,
			CellDoesNotExistException, InvalidContentException {
		AssignLiteralCell service = new AssignLiteralCell(ars, SHEETID, CELLID,
				VALUE);
		service.execute();

		BubbleDocs bd = BubbleDocs.getInstance();

		User user = bd.getUserByToken(ars);
		SpreadSheet ss = bd.getSpreadSheetById(SHEETID);

		String[] parse = CELLID.split(";");

		assertEquals(ars, user.getToken());
		assertEquals(SHEETID, ss.getId());
		assertEquals(
				Integer.parseInt(VALUE),
				ss.getCellByCoordinates(Integer.parseInt(parse[0]),
						Integer.parseInt(parse[1])).getContent().eval());
	}

	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		removeUserFromSession(ars);
		AssignLiteralCell service = new AssignLiteralCell(ars, SHEETID, CELLID,
				VALUE);
		service.execute();
	}

	@Test(expected = CellIsLockedException.class)
	public void cellIsLocked() {
		String[] parse = CELLID.split(";");
		BubbleDocs
				.getInstance()
				.getSpreadSheetById(SHEETID)
				.lockCell(Integer.parseInt(parse[0]),
						Integer.parseInt(parse[1]));
		AssignLiteralCell service = new AssignLiteralCell(ars, SHEETID, CELLID,
				VALUE);
		service.execute();
	}

	@Test(expected = UnauthorizedOperationException.class)
	public void userHasNoPermission() {
		AssignLiteralCell service = new AssignLiteralCell(maj, SHEETID, CELLID,
				VALUE);
		service.execute();
	}

	@Test(expected = SpreadSheetDoesNotExistException.class)
	public void spreadSheatDoesNotExist() {
		AssignLiteralCell service = new AssignLiteralCell(ars, 999, CELLID,
				VALUE);
		service.execute();
	}

	@Test(expected = CellDoesNotExistException.class)
	public void cellDoesNotExist() {
		AssignLiteralCell service = new AssignLiteralCell(ars, SHEETID, "0;10",
				VALUE);
		service.execute();
	}

	@Test(expected = InvalidLiteralException.class)
	public void invalidLiteral() {
		AssignLiteralCell service = new AssignLiteralCell(ars, SHEETID, CELLID,
				"a");
		service.execute();
	}

}