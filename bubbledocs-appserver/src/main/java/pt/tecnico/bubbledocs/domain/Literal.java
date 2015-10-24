package pt.tecnico.bubbledocs.domain;


public class Literal extends Literal_Base {

	public Literal() {
		super();
	}

	public Literal(int value) {
		super();
		init(value);
	}

	protected void init(int value) {
		setValue(value);
	}

	public int eval() {
		return getValue();
	}

	public void delete() {
		setCell(null);
		setBinaryFunctionRight(null);
		setBinaryFunctionLeft(null);
		deleteDomainObject();
	}
}
