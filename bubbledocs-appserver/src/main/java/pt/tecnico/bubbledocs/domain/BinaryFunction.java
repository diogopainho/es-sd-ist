package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exception.InvalidContentException;

public abstract class BinaryFunction extends BinaryFunction_Base {

	public BinaryFunction() {
		super();
	}

	public BinaryFunction(NonFunction nfleft, NonFunction nfright) {
		super();
		init(nfleft, nfright);
	}

	protected void init(NonFunction nfleft, NonFunction nfright) {
		this.setLeftNonFunction(nfleft);
		this.setRightNonFunction(nfright);
	}

	public abstract int eval() throws InvalidContentException;

	public void delete() {
		setCell(null);

		NonFunction right = this.getRightNonFunction();
		if (right != null)
			right.delete();

		NonFunction left = this.getLeftNonFunction();
		if (left != null)
			left.delete();

		setRightNonFunction(null);
		setLeftNonFunction(null);
		deleteDomainObject();
	}
}
