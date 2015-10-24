package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exception.InvalidContentException;


public abstract class Function extends Function_Base {

	public Function() {
		super();
	}

	public abstract int eval() throws InvalidContentException;

}
