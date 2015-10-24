package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exception.InvalidContentException;

public class Cell extends Cell_Base {

	protected Cell() {
		this.setLock(false);
	}

	public Cell(int line, int column, boolean locked) {
		this.setLine(line);
		this.setColumn(column);
		this.setLock(locked);
	}

	public int eval() throws InvalidContentException {
		return this.getContent().eval();
	}

	public void delete() {
		for (Reference r : getCellReferenceSet())
			r.delete();

		Content c = this.getContent();
		if (c != null)
			c.delete();

		setContent(null);
		setSpreadSheet(null);
		deleteDomainObject();
	}
}
