package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exception.InvalidContentException;


public class Reference extends Reference_Base {

	public Reference() {
		super();
	}

	public Reference(Cell cell) {
		super();
		init(cell);
	}

	protected void init(Cell cell) {
		this.setReferencedCell(cell);
	}

	public int eval() throws InvalidContentException {
		if(this.getReferencedCell().getContent() == null){
			throw new InvalidContentException();
		} else {
		return this.getReferencedCell().eval();
		}
	}

	public void delete() {
		setCell(null);
		setReferencedCell(null);
		setBinaryFunctionRight(null);
		setBinaryFunctionLeft(null);
		deleteDomainObject();
	}
}
