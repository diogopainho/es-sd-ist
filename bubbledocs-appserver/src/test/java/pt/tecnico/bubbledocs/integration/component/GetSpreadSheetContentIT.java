package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.AssignBinaryFunctionToCellIntegrator;
import pt.tecnico.bubbledocs.integration.AssignReferenceToCellIntegrator;
import pt.tecnico.bubbledocs.integration.GetSpreadSheetContentIntegrator;

public class GetSpreadSheetContentIT extends BubbleDocsIT {
	private static final String USERNAME = "ars";
	private static final String TOKEN_DOES_NOT_EXIST = "no-one";
	private static final String USERNAME_WITHOUT_PERMISSION = "maj";
	private static final String ADD_FUNC = "=ADD(10,2)";
	private static final String SUB_FUNC = "=SUB(10,2)";
	private static final String[] CONTENT_EXPECTED = { "12", "2", "10", "()",
			"#VALUE", "12", "()", "2", "()" };
	private static final String[] CONTENT_EXPECTED_EXTRA = { "12", "2", "10",
			"9", "8", "12", "8", "2", "8" };
	private static final String SHEET_NAME = "calc";
	private static final int SHEET_ROWS = 3;
	private static final int SHEET_COLUMNS = 3;
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

		// ADD LITERAL
		addLiteralToCell("3;2", 2, SHEETID); // 3;2 ---> 2
		addLiteralToCell("1;3", 10, SHEETID); // 1;3 ---> 10

		// ASSIGN BINARY FUNCTION
		AssignBinaryFunctionToCellIntegrator binaryFunction = new AssignBinaryFunctionToCellIntegrator(
				ars, SHEETID, "1;1", ADD_FUNC);
		binaryFunction.execute();

		// ASSIGN REFERENCE 2;2 --> 3;3 ---> #VALUE
		AssignReferenceToCellIntegrator referenceCell1 = new AssignReferenceToCellIntegrator(
				ars, SHEETID, "2;2", "3;3");
		referenceCell1.execute();

		// ASSIGN REFERENCE 1;2 --> 3;2 ---> 2
		AssignReferenceToCellIntegrator referenceCell2 = new AssignReferenceToCellIntegrator(
				ars, SHEETID, "1;2", "3;2");
		referenceCell2.execute();

		// ASSIGN REFERENCE 2;3 --> 4;4 ---> ADD FUNC
		AssignReferenceToCellIntegrator referenceCell3 = new AssignReferenceToCellIntegrator(
				ars, SHEETID, "2;3", "1;1");
		referenceCell3.execute();
	}

	@Test
	public void successGetContent() throws BubbleDocsException {
		GetSpreadSheetContentIntegrator integrator = new GetSpreadSheetContentIntegrator(
				ars, SHEETID);
		integrator.execute();

		String[][] content = integrator.getSheetContent();

		BubbleDocs bd = BubbleDocs.getInstance();

		User user = bd.getUserByToken(ars);
		SpreadSheet ss = bd.getSpreadSheetById(SHEETID);

		assertEquals(ars, user.getToken());
		assertEquals(SHEETID, ss.getId());
		int k = 0;

		for (int line = 1; line <= 3; line++) {
			for (int col = 1; col <= 3; col++) {
				assertEquals(CONTENT_EXPECTED[k], content[col][line]);
				k++;
			}
		}

	}

	@Test
	public void successGetContentExtra() throws BubbleDocsException {
		addLiteralToCell("2;1", 9, SHEETID);
		AssignBinaryFunctionToCellIntegrator binaryFunction = new AssignBinaryFunctionToCellIntegrator(
				ars, SHEETID, "3;1", SUB_FUNC);
		binaryFunction.execute();
		AssignReferenceToCellIntegrator referenceCell1 = new AssignReferenceToCellIntegrator(
				ars, SHEETID, "3;3", "3;1");
		referenceCell1.execute();

		GetSpreadSheetContentIntegrator integrator = new GetSpreadSheetContentIntegrator(
				ars, SHEETID);
		integrator.execute();

		String[][] content = integrator.getSheetContent();

		BubbleDocs bd = BubbleDocs.getInstance();

		User user = bd.getUserByToken(ars);
		SpreadSheet ss = bd.getSpreadSheetById(SHEETID);

		assertEquals(ars, user.getToken());
		assertEquals(SHEETID, ss.getId());
		int k = 0;

		for (int line = 1; line <= 3; line++) {
			for (int col = 1; col <= 3; col++) {
				assertEquals(CONTENT_EXPECTED_EXTRA[k], content[col][line]);
				k++;
			}
		}

	}

	@Test(expected = UserNotInSessionException.class)
	public void accessUsernameNotExist() {
		removeUserFromSession(ars);
		GetSpreadSheetContentIntegrator integrator = new GetSpreadSheetContentIntegrator(
				ars, SHEETID);
		integrator.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void sessionDoesNotExist() {
		GetSpreadSheetContentIntegrator integrator = new GetSpreadSheetContentIntegrator(
				TOKEN_DOES_NOT_EXIST, SHEETID);
		integrator.execute();
	}

	@Test(expected = UnauthorizedOperationException.class)
	public void spreadSheatDoesNotExist() {
		GetSpreadSheetContentIntegrator integrator = new GetSpreadSheetContentIntegrator(
				ars, 999);
		integrator.execute();
	}

	@Test(expected = UnauthorizedOperationException.class)
	public void userHasNoPermission() {
		GetSpreadSheetContentIntegrator integrator = new GetSpreadSheetContentIntegrator(
				maj, SHEETID);
		integrator.execute();
	}

}