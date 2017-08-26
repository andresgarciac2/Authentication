package co.com.uniandes.arquitectura.jdbc.connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;

import co.com.uniandes.arquitectura.persistence.AuthenticationDTO;
import co.com.uniandes.arquitectura.persistence.TokenDTO;
import co.com.uniandes.arquitectura.persistence.UserDTO;
import co.com.uniandes.arquitectura.utils.Countries;
import co.com.uniandes.arquitectura.utils.DniTypes;

public class AuthRepository {

static OracleJDBCConnection conn = OracleJDBCConnection.getDbCon();
    
	public static UserDTO getUserAuth(int cedula){
			UserDTO user = new UserDTO();
			ResultSet result = null;
	        PreparedStatement preparedStatement = null;
	        String select = "SELECT USERS.FIRST_NAME,USERS.EMAIL,USERS.LAST_NAME,USER_AUTHENTICATION.PASSWORD,USER_AUTHENTICATION.PASSWORD_SALT,ROLE_ACCESS.ROLE_ID  FROM USERS join USER_AUTHENTICATION ON USERS.id = USER_AUTHENTICATION.USER_ID and USERS.id = ?  join ROLE_ACCESS on USERS.id = ROLE_ACCESS.USER_ID";
			try {
				preparedStatement = conn.conn.prepareStatement(select);
				preparedStatement.setInt(1, cedula);

				// execute insert SQL stetement
				result = preparedStatement.executeQuery();
				 while (result.next()) {
					 AuthenticationDTO auth = new AuthenticationDTO();
					 user.setId(cedula);
					 user.setRoleId(result.getInt(6));
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
	
	public static int createAuth(int user, boolean isBlocked, byte[] pass, byte[] salt){
        int result = 0;
        PreparedStatement preparedStatement = null;
        String insertAuth = "INSERT INTO USER_AUTHENTICATION (user_id,is_blocked,password,password_salt) VALUES (?,?,?,?)";
		try {
			preparedStatement = conn.conn.prepareStatement(insertAuth);

			preparedStatement.setInt(1, user);
			preparedStatement.setInt(2, isBlocked ? 1 : 0);
			preparedStatement.setBytes(3, pass);
			preparedStatement.setBytes(4, salt);

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

	public static int createUser(int dni, String dniType, String country, String email, String firstName,
	    String lastName, String address, String phone) {
        int result = 0;
        
        PreparedStatement preparedStatement = null;
		String insertUser = 
            "INSERT INTO USERS (ID,DNI,DNI_TYPE,COUNTRY,EMAIL,FIRST_NAME,LAST_NAME,ADDRESS,PHONE) VALUES (?,?,?,?,?,?,?,?,?)";
		try {
			preparedStatement = conn.conn.prepareStatement(insertUser);

			preparedStatement.setInt(1, dni);
			preparedStatement.setInt(2, dni);
			preparedStatement.setInt(3, DniTypes.getTypes().get(dniType));
			preparedStatement.setInt(4, Countries.getCountries().get(country));
			preparedStatement.setString(5, email);
			preparedStatement.setString(6, firstName);
			preparedStatement.setString(7, lastName);
			preparedStatement.setString(8, address);
			preparedStatement.setString(9, phone);

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
	
	public static int createRoleAccess(int userId, int roleId){
		int result = 0;

		PreparedStatement preparedStatement = null;
		String insertRole = "INSERT INTO ROLE_ACCESS (USER_ID,ROLE_ID) VALUES (?,?)";
		try {
			preparedStatement = conn.conn.prepareStatement(insertRole);
			
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, roleId);

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
        String insertUser = "INSERT INTO USER_SESSION_TOKEN (USER_ID,EXPIRATION_DATE,TOKEN) VALUES (?,?,?)";
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
