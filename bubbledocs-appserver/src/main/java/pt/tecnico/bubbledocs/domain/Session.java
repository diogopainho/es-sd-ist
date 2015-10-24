package pt.tecnico.bubbledocs.domain;

import org.joda.time.DateTime;

public class Session extends Session_Base {
    
    public Session(String userToken) {
        super();
        DateTime date = new DateTime();
		setDateOfLastLogin(date);
		setUserToken(userToken);
    }
    
    public void delete() {
    	this.setBubbleDocs(null);
		deleteDomainObject();
	}
}
