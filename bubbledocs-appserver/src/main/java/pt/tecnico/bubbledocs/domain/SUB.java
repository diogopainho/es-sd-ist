package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exception.InvalidContentException;


public class SUB extends SUB_Base {

	public SUB() {
		super();
	}

	public SUB(NonFunction nfleft, NonFunction nfright) {
		super();
		init(nfleft, nfright);
	}

	protected void init(NonFunction nfleft, NonFunction nfright) {
		super.init(nfleft, nfright);
	}

	public int eval() throws InvalidContentException {
		int result = getLeftNonFunction().eval() - getRightNonFunction().eval();
		return result;
	}
}
