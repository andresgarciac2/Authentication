package co.com.uniandes.arquitectura.persistence;

import java.sql.Timestamp;

public class TokenDTO {

	int userId;
	Timestamp expiration;
	String hashToken;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Timestamp getExpiration() {
		return expiration;
	}
	public void setExpiration(Timestamp expiration) {
		this.expiration = expiration;
	}
	public String getHashToken() {
		return hashToken;
	}
	public void setHashToken(String hashToken) {
		this.hashToken = hashToken;
	}
	
	
	
}
