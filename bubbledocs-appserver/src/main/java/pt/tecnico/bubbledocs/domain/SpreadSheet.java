package pt.tecnico.bubbledocs.domain;

import org.joda.time.DateTime;

import pt.tecnico.bubbledocs.exception.CellDoesNotExistException;

public class SpreadSheet extends SpreadSheet_Base {

	public SpreadSheet() {
		super();
		BubbleDocs bd = BubbleDocs.getInstance();
		setId(bd.getSpreadCount());
		bd.setSpreadCount(bd.getSpreadCount() + 1);
	}

	public SpreadSheet(String name, int numlines, int numcolumns, String user) {
		super();
		BubbleDocs bd = BubbleDocs.getInstance();
		setId(bd.getSpreadCount());
		bd.setSpreadCount(bd.getSpreadCount() + 1);
		setName(name);
		DateTime date = new DateTime();
		setDate(date);
		setNumlines(numlines);
		setNumcolumns(numcolumns);
		setAuthor(user);
		bd.addPermissions(new Permissions(true, true, this.getId(), user));
	}

	public Cell getCellByCoordinates(int line, int column)
			throws CellDoesNotExistException {
		if (line <= getNumlines() && column <= getNumcolumns() && line > 0
				&& column > 0) {
			for (Cell cell : getCellsSet()) {
				if (cell.getLine() == line && cell.getColumn() == column) {
					return cell;
				}
			}
			return addEmptyCell(line, column);
		}
		throw new CellDoesNotExistException(line, column);
	}

	public void addContentToCell(int line, int column, Content content) {
		Cell cell = getCellByCoordinates(line, column);
		if (cell != null) {
			cell.setContent(content);
		} else {
			cell = new Cell(line, column, false);
			cell.setContent(content);
			this.addCells(cell);
		}
	}

	public Cell addEmptyCell(int line, int column) {
		Cell celula = new Cell(line, column, false);
		this.addCells(celula);
		return celula;
	}

	public boolean isAuthor(String username) {
		return username.equals(this.getAuthor());
	}

	public void delete() {
		// test
		for (Cell c : getCellsSet())
			c.delete();

		for (Permissions p : BubbleDocs.getInstance().getPermissionsSet()) {
			if (p.getSsId() == this.getId())
				p.delete();
		}

		setName(null);
		setAuthor(null);
		setBubbleDocs(null);
		deleteDomainObject();
	}

	public void lockCell(int lin, int col) {
		this.getCellByCoordinates(lin, col).setLock(true);
	}

	public void unlockCell(int lin, int col) {
		this.getCellByCoordinates(lin, col).setLock(false);
	}
}
