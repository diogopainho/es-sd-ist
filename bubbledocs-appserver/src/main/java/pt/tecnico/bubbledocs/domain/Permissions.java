package pt.tecnico.bubbledocs.domain;

public class Permissions extends Permissions_Base {

	public Permissions() {
	}

	public Permissions(boolean permissionLevel, boolean isAuthor, long ssId,
			String user) {
		setSsId(ssId);

		// identifica se o user e o autor da ss.
		setIsAuthor(isAuthor);

		// true se tem permissao de R/W; false para R apenas.
		setPermissionLevel(permissionLevel);
		setUsername(user);
	}

	void delete() {
		setBubbleDocs(null);
		setUsername(null);
		deleteDomainObject();
	}
}
