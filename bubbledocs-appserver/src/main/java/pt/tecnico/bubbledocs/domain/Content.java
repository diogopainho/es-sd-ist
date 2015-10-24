package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exception.InvalidContentException;


public abstract class Content extends Content_Base {

	public Content() {
		super();
	}

	public abstract int eval() throws InvalidContentException;

	public abstract void delete();
}