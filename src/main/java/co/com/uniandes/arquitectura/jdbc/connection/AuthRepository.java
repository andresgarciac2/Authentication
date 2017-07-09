package co.com.uniandes.arquitectura.jdbc.connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.Date;

import co.com.uniandes.arquitectura.persistence.AuthenticationDTO;
import co.com.uniandes.arquitectura.persistence.TokenDTO;
import co.com.uniandes.arquitectura.persistence.UserDTO;

public class AuthRepository {

static OracleJDBCConnection conn = OracleJDBCConnection.getDbCon();
    
	public static UserDTO getUserAuth(int cedula){
			UserDTO user = new UserDTO();
			ResultSet result = null;
	        PreparedStatement preparedStatement = null;
	        String select = "SELECT * FROM tuser join tauthentication ON tuser.id = tauthentication.ID_USER and tuser.id = ?";
			try {
				preparedStatement = conn.conn.prepareStatement(select);
				preparedStatement.setInt(1, cedula);

				// execute insert SQL stetement
				result = preparedStatement.executeQuery();
				 while (result.next()) {
					 AuthenticationDTO auth = new AuthenticationDTO();
					 user.setId(cedula);
					 user.setRoleId(result.getString(2));
					 auth.setUserId(cedula);
					 auth.setPassword(result.getBytes(4));
					 auth.setSalt(result.getBytes(5));
					 user.setAuth(auth);
				    }
				System.out.println("sentencia ejecutada");
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return user;
	}
	
	public static int createAuth(int user, byte[] pass, byte[] salt){
        int result = 0;
        PreparedStatement preparedStatement = null;
        String insertAuth = "INSERT INTO tauthentication (id_user,password,salt) VALUES (?,?,?)";
		try {
			preparedStatement = conn.conn.prepareStatement(insertAuth);

			preparedStatement.setInt(1, user);
			preparedStatement.setBytes(2, pass);
			preparedStatement.setBytes(3, salt);

			// execute insert SQL stetement
			result = preparedStatement.executeUpdate();
			System.out.println("sentencia ejecutada");
			preparedStatement.close();
			return result;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        return result;
	}
	
	public static int createUser(int user, String role){
        int result = 0;
        PreparedStatement preparedStatement = null;
        String insertUser = "INSERT INTO tuser (id,role_id) VALUES (?,?)";
		try {
			preparedStatement = conn.conn.prepareStatement(insertUser);

			preparedStatement.setInt(1, user);
			preparedStatement.setString(2, role);

			// execute insert SQL stetement
			result = preparedStatement.executeUpdate();
			System.out.println("sentencia ejecutada");
			preparedStatement.close();
			return result;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        return result;
	}
	
	public static int createUserToken(int user, Timestamp expiration, String token){
        int result = 0;
        PreparedStatement preparedStatement = null;
        String insertUser = "INSERT INTO ttoken (id_user,EXPIRATION,token) VALUES (?,?,?)";
		try {
			preparedStatement = conn.conn.prepareStatement(insertUser);

			preparedStatement.setInt(1, user);
			preparedStatement.setTimestamp(2, expiration);
			preparedStatement.setString(3, token);

			// execute insert SQL stetement
			result = preparedStatement.executeUpdate();
			System.out.println("sentencia ejecutada");
			preparedStatement.close();
			return result;
			
		} catch (SQLIntegrityConstraintViolationException e) {
			
			insertUser = "UPDATE ttoken SET EXPIRATION = ?, token = ? WHERE id_user = ?";
			
			try {
				preparedStatement = conn.conn.prepareStatement(insertUser);
				
				preparedStatement.setTimestamp(1, expiration);
				preparedStatement.setString(2, token);
				preparedStatement.setInt(3, user);

				// execute insert SQL stetement
				result = preparedStatement.executeUpdate();
				System.out.println("sentencia ejecutada");
				preparedStatement.close();
				return result;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        return result;
	}
	
	public static TokenDTO getToken(int cedula){
		TokenDTO token = null;
		ResultSet result = null;
        PreparedStatement preparedStatement = null;
        String select = "SELECT * FROM ttoken WHERE id_user = ?";
		try {
			preparedStatement = conn.conn.prepareStatement(select);
			preparedStatement.setInt(1, cedula);

			// execute insert SQL stetement
			result = preparedStatement.executeQuery();
			 while (result.next()) {
				 token = new TokenDTO();
				 token.setUserId(cedula);
				 token.setExpiration(result.getTimestamp(2));
				 token.setHashToken(result.getString(3));
			    }
			System.out.println("sentencia ejecutada");
			preparedStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return token;
}
}
