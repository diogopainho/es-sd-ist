package pt.tecnico.bubbledocs.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.exception.EmptySpreadSheetNameException;
import pt.tecnico.bubbledocs.exception.InvalidColumnNumberException;
import pt.tecnico.bubbledocs.exception.InvalidRowNumberException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.local.CreateSpreadSheet;

public class CreateSpreadSheetTest extends BubbleDocsServiceTest {

	// the tokens
	private String ars;

	private static final String USERNAME = "ars";
	private static final String EMAIL_1 = "pop4test1@test.com";
	private static final String USERNAME_NOT_IN_SESSION = "smf";
	private static final String EMAIL_2 = "pop4test2@test.com";

	private static final int ROW = 10;
	private static final int COLUMN = 10;
	private static final String SPREADSHEET_NAME = "calc";

	@Override
	public void populate4Test() {
		createUser(USERNAME, EMAIL_1, "António Rito Silva");
		createUser(USERNAME_NOT_IN_SESSION, EMAIL_2, "Super Mário Funil");
		ars = addUserToSession(USERNAME);
	}

	@Test
	public void success() {
		CreateSpreadSheet service = new CreateSpreadSheet(ars,
				SPREADSHEET_NAME, ROW, COLUMN);
		service.execute();

		User user = getUserFromUsername(USERNAME);
		SpreadSheet ss = user.getSpreadSheetByName(SPREADSHEET_NAME);

		assertEquals(USERNAME, user.getUsername());
		assertEquals(SPREADSHEET_NAME, ss.getName());
		assertEquals(ROW, ss.getNumlines());
		assertEquals(COLUMN, ss.getNumcolumns());
	}

	@Test(expected = EmptySpreadSheetNameException.class)
	public void emptySpreadSheetName() {
		CreateSpreadSheet service = new CreateSpreadSheet(ars, null, ROW,
				COLUMN);
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void accessUsernameNotExist() {
		removeUserFromSession(ars);
		CreateSpreadSheet service = new CreateSpreadSheet(ars,
				SPREADSHEET_NAME, ROW, COLUMN);
		service.execute();
	}

	@Test(expected = InvalidRowNumberException.class)
	public void rowNumberAtZero() {
		CreateSpreadSheet service = new CreateSpreadSheet(ars,
				SPREADSHEET_NAME, 0, COLUMN);
		service.execute();
	}

	@Test(expected = InvalidColumnNumberException.class)
	public void columnNumberAtZero() {
		CreateSpreadSheet service = new CreateSpreadSheet(ars,
				SPREADSHEET_NAME, ROW, 0);
		service.execute();
	}
}
