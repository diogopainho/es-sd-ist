package pt.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.CellDoesNotExistException;
import pt.tecnico.bubbledocs.exception.CellIsLockedException;
import pt.tecnico.bubbledocs.exception.InvalidBinaryFunctionInputException;
import pt.tecnico.bubbledocs.exception.InvalidSecondContentException;
import pt.tecnico.bubbledocs.exception.InvalidFirstContentException;
import pt.tecnico.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.integration.AssignBinaryFunctionToCellIntegrator;

public class AssignBinaryCellIT extends BubbleDocsIT {
	private static final String USERNAME = "ars";
	private static final String USERNAME_WITHOUT_PERMISSION = "maj";
	private static final String ADD_FUNC = "=ADD(10,2)";
	private static final String SUB_FUNC = "=SUB(10,2)";
	private static final String DIV_FUNC = "=DIV(10,2)";
	private static final String MUL_FUNC = "=MUL(10,2)";
	private static final String ADD_FUNC_REFERENCE = "=ADD(10,3;2)";
	private static final String ADD_FUNC_TWO_REFERENCES = "=ADD(1;3,3;2)";
	private static final String SUB_FUNC_REFERENCE = "=SUB(10,3;2)";
	private static final String MUL_FUNC_REFERENCE = "=MUL(10,3;2)";
	private static final String DIV_FUNC_REFERENCE = "=DIV(10,3;2)";
	private static final int ADD_EXPECTED = 12;
	private static final int SUB_EXPECTED = 8;
	private static final int DIV_EXPECTED = 5;
	private static final int MUL_EXPECTED = 20;
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
		addLiteralToCell("3;2", 2, SHEETID);
		addLiteralToCell("1;3", 10, SHEETID);
	}

	@Test
	public void successADD() throws BubbleDocsException {
		AssignBinaryFunctionToCellIntegrator integrator = new AssignBinaryFunctionToCellIntegrator(ars, SHEETID, CELLID,
				ADD_FUNC);
		integrator.execute();

		BubbleDocs bd = BubbleDocs.getInstance();

		User user = bd.getUserByToken(ars);
		SpreadSheet ss = bd.getSpreadSheetById(SHEETID);

		String[] parse = CELLID.split(";");

		assertEquals(ars, user.getToken());
		assertEquals(SHEETID, ss.getId());
		assertEquals(ADD_EXPECTED,
				ss.getCellByCoordinates(Integer.parseInt(parse[0]),
						Integer.parseInt(parse[1])).getContent().eval());
	}
	
	@Test
	public void successSUB() throws BubbleDocsException {
		AssignBinaryFunctionToCellIntegrator integrator = new AssignBinaryFunctionToCellIntegrator(ars, SHEETID, CELLID,
				SUB_FUNC);
		integrator.execute();

		BubbleDocs bd = BubbleDocs.getInstance();

		User user = bd.getUserByToken(ars);
		SpreadSheet ss = bd.getSpreadSheetById(SHEETID);

		String[] parse = CELLID.split(";");

		assertEquals(ars, user.getToken());
		assertEquals(SHEETID, ss.getId());
		assertEquals(SUB_EXPECTED,
				ss.getCellByCoordinates(Integer.parseInt(parse[0]),
						Integer.parseInt(parse[1])).getContent().eval());
	}
	
	@Test
	public void successDIV() throws BubbleDocsException {
		AssignBinaryFunctionToCellIntegrator integrator = new AssignBinaryFunctionToCellIntegrator(ars, SHEETID, CELLID,
				DIV_FUNC);
		integrator.execute();

		BubbleDocs bd = BubbleDocs.getInstance();

		User user = bd.getUserByToken(ars);
		SpreadSheet ss = bd.getSpreadSheetById(SHEETID);

		String[] parse = CELLID.split(";");

		assertEquals(ars, user.getToken());
		assertEquals(SHEETID, ss.getId());
		assertEquals(DIV_EXPECTED,
				ss.getCellByCoordinates(Integer.parseInt(parse[0]),
						Integer.parseInt(parse[1])).getContent().eval());
	}
	
	@Test
	public void successMUL() throws BubbleDocsException {
		AssignBinaryFunctionToCellIntegrator integrator = new AssignBinaryFunctionToCellIntegrator(ars, SHEETID, CELLID,
				MUL_FUNC);
		integrator.execute();

		BubbleDocs bd = BubbleDocs.getInstance();

		User user = bd.getUserByToken(ars);
		SpreadSheet ss = bd.getSpreadSheetById(SHEETID);

		String[] parse = CELLID.split(";");

		assertEquals(ars, user.getToken());
		assertEquals(SHEETID, ss.getId());
		assertEquals(MUL_EXPECTED,
				ss.getCellByCoordinates(Integer.parseInt(parse[0]),
						Integer.parseInt(parse[1])).getContent().eval());
	}
	
	@Test
	public void successADDWithReference() throws BubbleDocsException {
		AssignBinaryFunctionToCellIntegrator integrator = new AssignBinaryFunctionToCellIntegrator(ars, SHEETID, CELLID,
				ADD_FUNC_REFERENCE);
		integrator.execute();
		
		BubbleDocs bd = BubbleDocs.getInstance();

		User user = bd.getUserByToken(ars);
		SpreadSheet ss = bd.getSpreadSheetById(SHEETID);

		String[] parse = CELLID.split(";");

		assertEquals(ars, user.getToken());
		assertEquals(SHEETID, ss.getId());
		assertEquals(ADD_EXPECTED,
				ss.getCellByCoordinates(Integer.parseInt(parse[0]),
						Integer.parseInt(parse[1])).getContent().eval());
	}
	
	@Test
	public void successMULWithReference() throws BubbleDocsException {
		AssignBinaryFunctionToCellIntegrator integrator = new AssignBinaryFunctionToCellIntegrator(ars, SHEETID, CELLID,
				MUL_FUNC_REFERENCE);
		integrator.execute();
		
		BubbleDocs bd = BubbleDocs.getInstance();

		User user = bd.getUserByToken(ars);
		SpreadSheet ss = bd.getSpreadSheetById(SHEETID);

		String[] parse = CELLID.split(";");

		assertEquals(ars, user.getToken());
		assertEquals(SHEETID, ss.getId());
		assertEquals(MUL_EXPECTED,
				ss.getCellByCoordinates(Integer.parseInt(parse[0]),
						Integer.parseInt(parse[1])).getContent().eval());
	}
	
	@Test
	public void successSUBWithReference() throws BubbleDocsException {
		AssignBinaryFunctionToCellIntegrator integrator = new AssignBinaryFunctionToCellIntegrator(ars, SHEETID, CELLID,
				SUB_FUNC_REFERENCE);
		integrator.execute();
		
		BubbleDocs bd = BubbleDocs.getInstance();

		User user = bd.getUserByToken(ars);
		SpreadSheet ss = bd.getSpreadSheetById(SHEETID);

		String[] parse = CELLID.split(";");

		assertEquals(ars, user.getToken());
		assertEquals(SHEETID, ss.getId());
		assertEquals(SUB_EXPECTED,
				ss.getCellByCoordinates(Integer.parseInt(parse[0]),
						Integer.parseInt(parse[1])).getContent().eval());
	}
	
	@Test
	public void successDIVWithReference() throws BubbleDocsException {
		AssignBinaryFunctionToCellIntegrator integrator = new AssignBinaryFunctionToCellIntegrator(ars, SHEETID, CELLID,
				DIV_FUNC_REFERENCE);
		integrator.execute();
		
		BubbleDocs bd = BubbleDocs.getInstance();

		User user = bd.getUserByToken(ars);
		SpreadSheet ss = bd.getSpreadSheetById(SHEETID);

		String[] parse = CELLID.split(";");

		assertEquals(ars, user.getToken());
		assertEquals(SHEETID, ss.getId());
		assertEquals(DIV_EXPECTED,
				ss.getCellByCoordinates(Integer.parseInt(parse[0]),
						Integer.parseInt(parse[1])).getContent().eval());
	}
	
	@Test
	public void successADDWithTwoReferences() throws BubbleDocsException {
		AssignBinaryFunctionToCellIntegrator integrator = new AssignBinaryFunctionToCellIntegrator(ars, SHEETID, CELLID,
				ADD_FUNC_TWO_REFERENCES);
		integrator.execute();
		
		BubbleDocs bd = BubbleDocs.getInstance();

		User user = bd.getUserByToken(ars);
		SpreadSheet ss = bd.getSpreadSheetById(SHEETID);

		String[] parse = CELLID.split(";");

		assertEquals(ars, user.getToken());
		assertEquals(SHEETID, ss.getId());
		assertEquals(ADD_EXPECTED,
				ss.getCellByCoordinates(Integer.parseInt(parse[0]),
						Integer.parseInt(parse[1])).getContent().eval());
	}

	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() {
		removeUserFromSession(ars);
		AssignBinaryFunctionToCellIntegrator integrator = new AssignBinaryFunctionToCellIntegrator(ars, SHEETID, CELLID,
				ADD_FUNC);
		integrator.execute();
	}

	@Test(expected = CellIsLockedException.class)
	public void cellIsLocked() {
		String[] parse = CELLID.split(";");
		BubbleDocs
				.getInstance()
				.getSpreadSheetById(SHEETID)
				.lockCell(Integer.parseInt(parse[0]),
						Integer.parseInt(parse[1]));
		AssignBinaryFunctionToCellIntegrator integrator = new AssignBinaryFunctionToCellIntegrator(ars, SHEETID, CELLID,
				ADD_FUNC);
		integrator.execute();
	}

	@Test(expected = UnauthorizedOperationException.class)
	public void userHasNoPermission() {
		AssignBinaryFunctionToCellIntegrator integrator = new AssignBinaryFunctionToCellIntegrator(maj, SHEETID, CELLID,
				ADD_FUNC);
		integrator.execute();
	}

	@Test(expected = SpreadSheetDoesNotExistException.class)
	public void spreadSheatDoesNotExist() {
		AssignBinaryFunctionToCellIntegrator integrator = new AssignBinaryFunctionToCellIntegrator(ars, 999, CELLID,
				ADD_FUNC);
		integrator.execute();
	}

	@Test(expected = CellDoesNotExistException.class)
	public void cellDoesNotExist() {
		AssignBinaryFunctionToCellIntegrator integrator = new AssignBinaryFunctionToCellIntegrator(ars, SHEETID, "0;10",
				ADD_FUNC);
		integrator.execute();
	}
	
	@Test(expected = InvalidFirstContentException.class)
	public void invalidFirstContent() {
		AssignBinaryFunctionToCellIntegrator integrator = new AssignBinaryFunctionToCellIntegrator(ars, SHEETID, CELLID,
				"=ADD(aaa,2)");
		integrator.execute();
	}
	
	@Test(expected = InvalidSecondContentException.class)
	public void invalidSecondContent() {
		AssignBinaryFunctionToCellIntegrator integrator = new AssignBinaryFunctionToCellIntegrator(ars, SHEETID, CELLID,
				"=ADD(2,aaa)");
		integrator.execute();
	}
	
	@Test(expected = InvalidBinaryFunctionInputException.class)
	public void invalidBinaryInput() {
		AssignBinaryFunctionToCellIntegrator integrator = new AssignBinaryFunctionToCellIntegrator(ars, SHEETID, CELLID,
				"INVALID_INPUT");
		integrator.execute();
	}
	
	@Test(expected = CellDoesNotExistException.class)
	public void invalidReferencedContent() {
		AssignBinaryFunctionToCellIntegrator integrator = new AssignBinaryFunctionToCellIntegrator(ars, SHEETID, CELLID,
				"=ADD(20;20,2)");
		integrator.execute();
	}

}
