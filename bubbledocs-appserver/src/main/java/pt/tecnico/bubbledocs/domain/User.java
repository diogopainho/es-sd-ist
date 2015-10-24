package pt.tecnico.bubbledocs.domain;

import java.util.List;

import pt.tecnico.bubbledocs.exception.InvalidUsernameException;

public class User extends User_Base {

	public User() {
	}

	public User(String name, String email, String username) {
		super();
		
		if (username.length() < 3 || username.length() > 8)
			throw new InvalidUsernameException(username);
		
		setName(name);
		setEmail(email);
		setUsername(username);
	}

	public void createNewSpreadsheet(int id, String name, int numlines,
			int numcolumns) {
		SpreadSheet spreadSheet = new SpreadSheet(name, numlines, numcolumns,
				this.getUsername());
		BubbleDocs.getInstance().addSpreadSheet(spreadSheet);
	}

	public List<SpreadSheet> getAcessibleSpreadsheets() {
		return BubbleDocs.getInstance().getSheetsAcessibleByUser(
				this.getUsername());
	}

	public List<SpreadSheet> getCreatedSpreadsheets() {
		return BubbleDocs.getInstance().getSpreadsheetsFromAuthor(
				this.getUsername());
	}

	public void printCreatedSpreadSheets() {
		BubbleDocs.getInstance()
				.printSpreadsheetsFromAuthor(this.getUsername());
	}

	public void printAcessibleSpreadSheets() {
		BubbleDocs.getInstance().printSheetsAcessibleByUser(this.getUsername());
	}

	public SpreadSheet getSpreadSheetByName(String name) {
		return BubbleDocs.getInstance().getSpreadSheetByName(name);
	}

	public void delete() {

		// delete all the spreadsheets the user created
		for (SpreadSheet spreadsheet : this.getCreatedSpreadsheets()) {
			spreadsheet.delete();
		}

		// remove the user access from spreadsheets
		for (SpreadSheet accspreadsheet : this.getAcessibleSpreadsheets()) {
			BubbleDocs.getInstance().removeUserAcessToSpreadSheet(
					this.getUsername(), accspreadsheet.getId());
		}

		setBubbleDocs(null);
		// deleteDomainObject();
	}

}
