package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exception.InvalidContentException;


public abstract class NonFunction extends NonFunction_Base {

	public NonFunction() {
		super();
	}

	public abstract int eval() throws InvalidContentException;

	public void delete() {
		setCell(null);
		setBinaryFunctionRight(null);
		setBinaryFunctionLeft(null);
		deleteDomainObject();
	}
}
