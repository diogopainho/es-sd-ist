package pt.tecnico.bubbledocs.integration.component;

import java.util.Random;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Session;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

public class BubbleDocsIT {

	@Before
	public void setUp() throws Exception {

		try {
			FenixFramework.getTransactionManager().begin(false);
			populate4Test();
		} catch (WriteOnReadError | NotSupportedException | SystemException e1) {
			e1.printStackTrace();
		}
	}

	@After
	public void tearDown() {
		try {
			FenixFramework.getTransactionManager().rollback();
		} catch (IllegalStateException | SecurityException | SystemException e) {
			e.printStackTrace();
		}
	}

	// should redefine this method in the subclasses if it is needed to specify
	// some initial state
	public void populate4Test() {
	}

	// auxiliary methods that access the domain layer and are needed in the test
	// classes
	// for defining the initial state and checking that the service has the
	// expected behavior

	User createUser(String username, String email, String name) {
		BubbleDocs bd = BubbleDocs.getInstance();
		User user = new User(name, email, username);
		bd.addUser(user);
		return user;
	}

	public SpreadSheet createSpreadSheet(User user, String name, int row,
			int column) {
		BubbleDocs bd = BubbleDocs.getInstance();
		SpreadSheet ss = new SpreadSheet(name, row, column, user.getUsername());
		bd.addSpreadSheet(ss);
		return ss;
	}

	// returns a spreadsheet whose name is equal to name
	public SpreadSheet getSpreadSheet(String name) {
		BubbleDocs bd = BubbleDocs.getInstance();
		SpreadSheet spreadsheet = bd.getSpreadSheetByName(name);
		return spreadsheet;
	}

	// put a user into session and returns the token associated to it
	String addUserToSession(String username) {
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

	void removeUserFromSession(String token) {
		BubbleDocs bd = BubbleDocs.getInstance();
		bd.getUserSessionByToken(token).delete();
	}

	User getUserFromUsername(String username) {
		BubbleDocs bd = BubbleDocs.getInstance();
		try {
			User user = bd.getUserByUsername(username);
			return user;
		} catch (LoginBubbleDocsException e) {
			return null;
		}
	}

	// return the user registered in session whose token is equal to token
	User getUserFromSession(String token) {
		BubbleDocs bd = BubbleDocs.getInstance();
		try {
			User user = bd.getUserByToken(token);
			return user;
		} catch (UserNotInSessionException e) {
			return null;
		}
	}

	void addLiteralToCell(String cellID, int literal, int sheetID) {
		BubbleDocs bd = BubbleDocs.getInstance();
		
		String[] parse = cellID.split(";");
		
		SpreadSheet ss = bd.getSpreadSheetById(sheetID);
		Literal lit = new Literal(literal);
		
		ss.getCellByCoordinates(Integer.parseInt(parse[0]), 
								Integer.parseInt(parse[1])).setContent(lit);
	}
}
