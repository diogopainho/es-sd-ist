package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exception.InvalidContentException;


public class DIV extends DIV_Base {

	public DIV() {
		super();
	}

	public DIV(NonFunction nfleft, NonFunction nfright) {
		super();
		init(nfleft, nfright);
	}

	protected void init(NonFunction nfleft, NonFunction nfright) {
		super.init(nfleft, nfright);
	}

	public int eval() throws InvalidContentException {
		int result = getLeftNonFunction().eval() / getRightNonFunction().eval();
		return result;
	}

}
