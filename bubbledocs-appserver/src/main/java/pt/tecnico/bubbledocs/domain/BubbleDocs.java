package pt.tecnico.bubbledocs.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.CellDoesNotExistException;
import pt.tecnico.bubbledocs.exception.CellIsLockedException;
import pt.tecnico.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.bubbledocs.exception.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exception.EmptySpreadSheetNameException;
import pt.tecnico.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.bubbledocs.exception.InvalidBinaryFunctionInputException;
import pt.tecnico.bubbledocs.exception.InvalidBinaryFunctionNameException;
import pt.tecnico.bubbledocs.exception.InvalidColumnNumberException;
import pt.tecnico.bubbledocs.exception.InvalidContentException;
import pt.tecnico.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.bubbledocs.exception.InvalidFirstContentException;
import pt.tecnico.bubbledocs.exception.InvalidLiteralException;
import pt.tecnico.bubbledocs.exception.InvalidReferenceException;
import pt.tecnico.bubbledocs.exception.InvalidRowNumberException;
import pt.tecnico.bubbledocs.exception.InvalidSecondContentException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.SpreadSheetDoesNotExistException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.serialize.SerializeToXML;

/**
 * This class implements the Singleton design pattern.
 **/

public class BubbleDocs extends BubbleDocs_Base {

	public static BubbleDocs getInstance() {
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbleDocs();
		if (bd == null)
			bd = new BubbleDocs();

		return bd;
	}

	public BubbleDocs() {
		this.addUser(new User("SuperUser", "root@email.com", "root"));

		FenixFramework.getDomainRoot().setBubbleDocs(this);
	}

	// Este metodo faz a verificacao se o user ja existe na lista de permissioes
	// para uma dada ss antes de criar um novo objecto.
	// Edita a permissao para os valores atualizados em caso de match.
	public void addNewPermission(boolean permissionLevel, boolean isAuthor,
			int ssId, String user) {

		for (Permissions p : this.getPermissionsSet()) {
			if (p.getUsername().equals(user) && p.getSsId() == ssId) {
				p.setPermissionLevel(permissionLevel);
				return;
			}
		}
		this.addPermissions(new Permissions(permissionLevel, isAuthor, ssId,
				user));
	}

	@Override
	public void addUser(User userToBeAdded) throws BubbleDocsException {

		Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+");
		Matcher mat = pattern.matcher(userToBeAdded.getEmail());

		if (!mat.matches())
			throw new InvalidEmailException(userToBeAdded.getEmail());
		if (existEmail(userToBeAdded.getEmail()))
			throw new DuplicateEmailException(userToBeAdded.getEmail());
		if (existsUser(userToBeAdded.getUsername()))
			throw new DuplicateUsernameException(userToBeAdded.getUsername());
		if (userToBeAdded.getUsername() == "")
			throw new EmptyUsernameException();

		super.addUser(userToBeAdded);
	}

	public User getUserByToken(String token) throws UserNotInSessionException {
		for (User user : getUserSet()) {
			if (user.getToken() != null && user.getToken().equals(token)) {
				return user;
			}
		}
		throw new UserNotInSessionException(token);
	}

	public void deleteUser(String username) throws LoginBubbleDocsException,
			UnauthorizedOperationException, UserNotInSessionException {
		User userToBeDeleted = this.getUserByUsername(username);

		if (!existsUser(userToBeDeleted.getUsername()))
			throw new LoginBubbleDocsException(userToBeDeleted.getUsername());

		userToBeDeleted.delete();
		super.removeUser(userToBeDeleted);
	}

	public void createNewSpreadsheet(String name, int numlines, int numcolumns,
			String username) throws UserNotInSessionException {
		if (numlines < 1)
			throw new InvalidRowNumberException(numlines);
		if (numcolumns < 1)
			throw new InvalidColumnNumberException(numcolumns);
		if (name == null)
			throw new EmptySpreadSheetNameException(numlines, numcolumns);
		User user = this.getUserByUsername(username);

		validateSessionByToken(user.getToken());
		SpreadSheet spreadsheet = new SpreadSheet(name, numlines, numcolumns,
				username);
		this.addSpreadSheet(spreadsheet);
	}

	// Remove permissoes de leitura da spreadsheet "ssId" ao user dado.
	public void removeUserAcessToSpreadSheet(String username, long ssId) {

		for (Permissions p : this.getPermissionsSet()) {
			if (p.getUsername().equals(username) && p.getSsId() == ssId) {
				if (this.getSpreadSheetById(p.getSsId()).isAuthor(
						p.getUsername())) {
					break;
				} else {
					p.delete();
					break;
				}
			}
		}
	}

	// Metodo que devolve a lista de Spreadsheet a que um dado user tem
	// permissoes de acesso.
	// Podem ser de read, write ou author, e' indiferente.
	public List<SpreadSheet> getSheetsAcessibleByUser(String username) {
		List<SpreadSheet> matchingSpreadsheets = new ArrayList<SpreadSheet>();

		if (username.equals("root")) {
			for (SpreadSheet sheet : this.getSpreadSheetSet()) {
				matchingSpreadsheets.add(sheet);
			}
		} else {
			for (Permissions p : this.getPermissionsSet()) {
				if (p.getUsername().equals(username)) {
					matchingSpreadsheets.add(this.getSpreadSheetById(p
							.getSsId()));
				}
			}
		}

		return matchingSpreadsheets;
	}

	public SpreadSheet getSpreadSheetById(long id)
			throws SpreadSheetDoesNotExistException {
		for (SpreadSheet s : this.getSpreadSheetSet()) {
			if (s.getId() == id) {
				return s;
			}
		}
		throw new SpreadSheetDoesNotExistException(id);

	}

	public List<SpreadSheet> getSpreadsheetsFromAuthor(String username) {
		List<SpreadSheet> matchingSpreadsheets = new ArrayList<SpreadSheet>();

		for (SpreadSheet existingSpreadsheet : this.getSpreadSheetSet()) {
			if (existingSpreadsheet.getAuthor().equals(username)) {
				matchingSpreadsheets.add(existingSpreadsheet);
			}
		}
		return matchingSpreadsheets;
	}

	public SpreadSheet getSpreadSheetByName(String name) {
		for (SpreadSheet existingSpreadsheet : this.getSpreadSheetSet()) {
			if (existingSpreadsheet.getName().equals(name))

				return existingSpreadsheet;
		}
		return null;
	}

	public boolean existEmail(String email) {
		for (User user : getUserSet()) {
			if (user.getEmail().equals(email))
				return true;
		}
		return false;
	}

	public User getUserByUsername(String username)
			throws LoginBubbleDocsException {
		for (User user : getUserSet()) {
			if (user.getUsername().equals(username)) {
				return user;
			}
		}
		throw new LoginBubbleDocsException(username);
	}

	public String getPasswordByUsername(String username)
			throws LoginBubbleDocsException {
		for (User u : this.getUserSet()) {
			if (u.getUsername().equals(username)) {
				return u.getPassword();
			}
		}
		throw new LoginBubbleDocsException(username);
	}

	public boolean existsUser(String username) {
		try {
			return getUserByUsername(username) != null;
		} catch (LoginBubbleDocsException e) {
			return false;
		}
	}

	public void printAllUsers() {
		for (User user : getUserSet()) {
			System.out.println("Name: " + user.getName() + " Password: "
					+ user.getPassword() + " Username: " + user.getUsername()
					+ "\n");
		}
	}

	public void printSheetsAcessibleByUser(String username) {
		List<SpreadSheet> matchingSpreadsheets = this
				.getSheetsAcessibleByUser(username);

		for (SpreadSheet sheet : matchingSpreadsheets) {
			System.out.println("User: " + username + "; Nome da Folha: "
					+ sheet.getName() + "; ID da Folha: " + sheet.getId());
		}
	}

	public void printSpreadsheetsFromAuthor(String username) {
		List<SpreadSheet> matchingSpreadsheets = this
				.getSpreadsheetsFromAuthor(username);
		for (SpreadSheet sheet : matchingSpreadsheets) {
			System.out.println("User: " + username + "; Nome da Folha: "
					+ sheet.getName() + "; ID da Folha: " + sheet.getId());
		}
	}

	public Session getUserSessionByToken(String token)
			throws UserNotInSessionException {
		for (Session s : this.getSessionSet()) {
			if (s.getUserToken().equals(token))
				return s;
		}
		throw new UserNotInSessionException(token);
	}

	public void removeExpiredSessions() {
		for (Session s : this.getSessionSet()) {
			if (s.getDateOfLastLogin().plusHours(2).isBeforeNow()) {
				s.delete();
			}
		}
	}

	public boolean validateSessionByToken(String userToken)
			throws UserNotInSessionException {
		if (userToken == null)
			throw new UserNotInSessionException(userToken);
		for (Session s : this.getSessionSet()) {
			if (s.getUserToken().equals(userToken)
					&& s.getDateOfLastLogin().plusHours(2).isAfterNow()) {
				s.setDateOfLastLogin(new DateTime());
				return true;
			}
		}
		throw new UserNotInSessionException(userToken);
	}

	public void removeSessionFromUser(User user) {
		for (Session s : this.getSessionSet()) {
			if (s.getUserToken().equals(user.getToken())) {
				s.delete();
				return;
			}
		}
	}

	public org.jdom2.Document convertToXML(SpreadSheet ss) {
		SerializeToXML serial = new SerializeToXML();

		org.jdom2.Document jdomDoc = serial.convertToXML(ss);

		return jdomDoc;
	}

	public SpreadSheet recoverFromBackup(org.jdom2.Document jdomDoc) {
		SerializeToXML serial = new SerializeToXML();

		SpreadSheet ss = serial.recoverFromBackup(jdomDoc);

		return ss;
	}

	// Retorna as permissoes de acesso que um user tem a uma spreadsheet caso
	// existam e null em caso contrario
	public Permissions userHasPermissionToSpreadSheet(String username, int ssId) {
		for (Permissions p : this.getPermissionsSet()) {
			if (p.getUsername().equals(username) && p.getSsId() == ssId)
				return p;
		}
		return null;
	}

	// Verificar se o utilizador tem permissoes de acesso
	public void validatePermissions(String userToken, int sheetId)
			throws UnauthorizedOperationException {
		Permissions p = userHasPermissionToSpreadSheet(
				getUserByToken(userToken).getUsername(), sheetId);
		if (p == null || p.getPermissionLevel() == false)
			throw new UnauthorizedOperationException();
	}

	public void userLogin(String username, String password, String userToken)
			throws LoginBubbleDocsException, LoginBubbleDocsException {
		String dataBasePassword = getPasswordByUsername(username);

		if (!password.equals(dataBasePassword)) {
			throw new LoginBubbleDocsException(username);
		}

		User u = getUserByUsername(username);
		removeExpiredSessions();
		try {
			Session session = getUserSessionByToken(u.getToken());
			session.delete();
			u.setToken(userToken);
			session = new Session(userToken);
			addSession(session);
		} catch (UserNotInSessionException e) {
			Session session = new Session(userToken);
			u.setToken(userToken);
			addSession(session);
		}
	}

	public String addLiteralToCell(String userToken, int sheetId,
			String cellId, String literal) throws UserNotInSessionException,
			CellIsLockedException, UnauthorizedOperationException,
			InvalidLiteralException {
		SpreadSheet ss = getSpreadSheetById(sheetId);
		String[] parse = cellId.split(";");
		int cellRow = Integer.parseInt(parse[0]);
		int cellColumn = Integer.parseInt(parse[1]);
		int literalInt;

		if (ss.getCellByCoordinates(cellRow, cellColumn).getLock() == true)
			throw new CellIsLockedException(cellRow, cellColumn);

		validatePermissions(userToken, sheetId);

		try {
			literalInt = Integer.parseInt(literal);
		} catch (NumberFormatException e) {
			throw new InvalidLiteralException(literal);
		}

		validateSessionByToken(userToken);
		Literal lit = new Literal(literalInt);
		Cell cell = ss.getCellByCoordinates(cellRow, cellColumn);
		cell.setContent(lit);
		try {
			int result = cell.eval();
			return Integer.toString(result);
		} catch (InvalidContentException e) {
			return null;
		}
	}

	public Cell addReferenceToCell(String userToken, int sheetId,
			String cellId, String reference) throws BubbleDocsException {
		SpreadSheet ss = getSpreadSheetById(sheetId);
		String[] parse = cellId.split(";");
		int cellRow = Integer.parseInt(parse[0]);
		int cellColumn = Integer.parseInt(parse[1]);

		if (ss.getCellByCoordinates(cellRow, cellColumn).getLock() == true)
			throw new CellIsLockedException(cellRow, cellColumn);

		String[] parseRef = reference.split(";");
		int referenceColumn;
		int referenceRow;

		Cell refCell;

		validatePermissions(userToken, sheetId);

		try {
			referenceRow = Integer.parseInt(parseRef[0]);
			referenceColumn = Integer.parseInt(parseRef[1]);
			refCell = ss.getCellByCoordinates(referenceRow, referenceColumn);
		} catch (NumberFormatException | CellDoesNotExistException e) {
			throw new InvalidReferenceException(reference);
		}

		validateSessionByToken(userToken);
		Reference ref = new Reference(refCell);
		Cell cell = ss.getCellByCoordinates(cellRow, cellColumn);
		cell.setContent(ref);
		return cell;
	}

	public String addUserToSession(String username) {
		BubbleDocs bd = BubbleDocs.getInstance();

		Random rand = new Random(System.currentTimeMillis());
		String lastToken = bd.getUserByUsername(username).getToken();
		int randomNum;

		if (lastToken == null) {
			randomNum = rand.nextInt(10);
			bd.getUserByUsername(username).setToken(
					username.concat(Integer.toString(randomNum)));
		} else {

			do {
				// Random number in the range 0-9
				randomNum = rand.nextInt(10);

				bd.getUserByUsername(username).setToken(
						username.concat(Integer.toString(randomNum)));
			} while (lastToken
					.equals(bd.getUserByUsername(username).getToken()));
		}
		Session session = new Session(bd.getUserByUsername(username).getToken());
		bd.addSession(session);
		return bd.getUserByUsername(username).getToken();
	}

	public NonFunction parseNonFunction(String input, long sheetID)
			throws BubbleDocsException {
		String[] parseReference;
		if (input.contains(";")) {
			parseReference = input.split(";");
			try {
				return new Reference(getSpreadSheetById(sheetID)
						.getCellByCoordinates(
								Integer.parseInt(parseReference[0]),
								Integer.parseInt(parseReference[1])));
			} catch (NumberFormatException e) {
				throw new InvalidBinaryFunctionInputException(input);
			}
		}
		try {
			return new Literal(Integer.parseInt(input));
		} catch (NumberFormatException e) {
			throw new InvalidBinaryFunctionInputException(input);
		}
	}

	public BinaryFunction parseBinaryFunction(String function, long sheetID)
			throws BubbleDocsException {

		if (!function.contains(",") || function.charAt(0) != '=')
			throw new InvalidBinaryFunctionInputException(function);

		// example - =ADD(10,2)
		// ADD
		String funcName = function.substring(1, 4);

		// funcInputParse[0] = "=ADD" ; funcInputparse[1] = "10,2)"
		String funcInput = function.substring(5, function.length() - 1);

		// contentParse[0] = "1" ; contentParse[1] = 2
		String[] contentParse = funcInput.split(",");

		NonFunction leftNonFunction, rightNonFunction;

		try {
			leftNonFunction = parseNonFunction(contentParse[0], sheetID);
		} catch (InvalidBinaryFunctionInputException e) {
			throw new InvalidFirstContentException(e.getFunction());
		}

		try {
			rightNonFunction = parseNonFunction(contentParse[1], sheetID);
		} catch (InvalidBinaryFunctionInputException e) {
			throw new InvalidSecondContentException(e.getFunction());
		}

		if (funcName.equals("ADD")) {
			return new ADD(leftNonFunction, rightNonFunction);
		} else if (funcName.equals("SUB")) {
			return new SUB(leftNonFunction, rightNonFunction);
		} else if (funcName.equals("MUL")) {
			return new MUL(leftNonFunction, rightNonFunction);
		} else if (funcName.equals("DIV")) {
			return new DIV(leftNonFunction, rightNonFunction);
		} else {
			throw new InvalidBinaryFunctionNameException(funcName);
		}
	}

	public Cell addBinaryFunctionToCell(String userToken, int sheetId,
			String cellId, String function) throws BubbleDocsException {
		SpreadSheet ss = getSpreadSheetById(sheetId);

		String[] parse = cellId.split(";");
		int cellRow = Integer.parseInt(parse[0]);
		int cellColumn = Integer.parseInt(parse[1]);

		if (ss.getCellByCoordinates(cellRow, cellColumn).getLock() == true)
			throw new CellIsLockedException(cellRow, cellColumn);

		validatePermissions(userToken, sheetId);

		validateSessionByToken(userToken);
		BinaryFunction binFunction = parseBinaryFunction(function, sheetId);
		Cell cell = ss.getCellByCoordinates(cellRow, cellColumn);
		cell.setContent(binFunction);
		return cell;
	}

	public String[][] getSpreadSheetContent(int id)
			throws CellDoesNotExistException, SpreadSheetDoesNotExistException {

		SpreadSheet spreadSheet = getSpreadSheetById(id);

		int maxCol = spreadSheet.getNumcolumns();
		int maxLin = spreadSheet.getNumlines();
		String[][] spreadSheetContent = new String[maxCol + 1][maxLin + 1];

		Content content;
		for (int line = 1; line <= maxLin; line++) {
			for (int col = 1; col <= maxCol; col++) {
				content = spreadSheet.getCellByCoordinates(line, col)
						.getContent();
				if (content != null) {
					try {
						int result = content.eval();
						spreadSheetContent[col][line] = Integer
								.toString(result);
					} catch (InvalidContentException e) {
						spreadSheetContent[col][line] = "#VALUE";
					}
				} else
					spreadSheetContent[col][line] = "()";
			}
		}
		return spreadSheetContent;
	}

}
