package co.com.uniandes.arquitectura.persistence;

/**
 * @author andresgarcias4n
 *
 */
public class LoginDTO {

	int user;
	String password;
	String roleId;
	
	/**
	 * @return the user
	 */
	public int getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(int user) {
		this.user = user;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return
	 */
	public String getRoleId() {
		return roleId;
	}
	/**
	 * @param roleId
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	
	
}
