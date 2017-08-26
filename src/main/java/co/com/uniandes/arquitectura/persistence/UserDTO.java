package co.com.uniandes.arquitectura.persistence;

public class UserDTO {

	int id;
	int roleId;
	AuthenticationDTO auth;
	TokenDTO token;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public AuthenticationDTO getAuth() {
		return auth;
	}
	public void setAuth(AuthenticationDTO auth) {
		this.auth = auth;
	}
	public TokenDTO getToken() {
		return token;
	}
	public void setToken(TokenDTO token) {
		this.token = token;
	}
	
	
}
