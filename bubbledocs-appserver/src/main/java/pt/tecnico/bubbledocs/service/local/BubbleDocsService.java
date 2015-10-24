package pt.tecnico.bubbledocs.service.local;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

// add needed import declarations

public abstract class BubbleDocsService {

	@Atomic
	public final void execute() throws BubbleDocsException {
		dispatch();
	}

	static BubbleDocs getBubbleDocs() {
		return FenixFramework.getDomainRoot().getBubbleDocs();
	}

	protected abstract void dispatch() throws BubbleDocsException;
}
