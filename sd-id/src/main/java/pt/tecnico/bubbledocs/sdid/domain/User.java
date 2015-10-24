package pt.tecnico.bubbledocs.sdid.domain;

public class User {

	public User(String userID, String password, String email) {
		this.setEmail(email);
		this.setUserID(userID);
		this.setPassword(password);
	}

	private String email;
	private String userID;
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}