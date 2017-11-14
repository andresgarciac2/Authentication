package co.com.uniandes.arquitectura.auth;

import java.sql.Timestamp;
import java.util.Date;

import co.com.uniandes.arquitectura.auth.jwt.JWTGenerator;
import co.com.uniandes.arquitectura.controller.Controller;
import co.com.uniandes.arquitectura.jdbc.connection.AuthRepository;
import co.com.uniandes.arquitectura.persistence.LoginDTO;
import co.com.uniandes.arquitectura.persistence.TokenDTO;
import co.com.uniandes.arquitectura.persistence.UpdateUsersDTO;
import co.com.uniandes.arquitectura.persistence.UserDTO;
import co.com.uniandes.arquitectura.persistence.UsersDTO;
import co.com.uniandes.arquitectura.utils.EmailSender;
import io.vertx.rxjava.ext.web.RoutingContext;

/**
 * @author andresgarcias4n
 *
 */
public class AuthenticationController implements Controller {

	public void getUserInfo(RoutingContext ctx){
		int userId = Integer.parseInt(ctx.getBodyAsString());
		
		UsersDTO user = AuthRepository.getUserInformation(userId);
		respondWithJson(ctx, 200, user);
	}
	
	public void updateUserInfo(RoutingContext ctx) {
		UpdateUsersDTO user = extractBodyAsJson(ctx, UpdateUsersDTO.class);
		
		int userUpdated = AuthRepository.updateUser(user.getDni(), user.getCountry(), user.getEmail(), user.getFirstName(), user.getAddress(), user.getPhone());
	
		if(userUpdated == 1){
			respondWithJson(ctx, 200, "User Updated");
		} else {
			respondWithJson(ctx, 501, "user not updated");
		}
	}
	
	public void login(RoutingContext ctx) {
		LoginDTO req = extractBodyAsJson(ctx, LoginDTO.class);
		UserDTO user = AuthRepository.getUserAuth(req.getUser());
		String jwToken = ""; 
		boolean success = AuthChecker.isExpectedPassword(req.getPassword().toCharArray(), user.getAuth().getSalt(), user.getAuth().getPassword());
		String rta = success ? "the password match" : "the user or password does not match";
		if (success) {
			jwToken = JWTGenerator.createJWT(Integer.toString(user.getId()), user.getName(), 1111111, user.getRoleId());
			Timestamp sqlTimeStamp = new java.sql.Timestamp(new Date(System.currentTimeMillis() + 1111111).getTime());
			AuthRepository.createUserToken(user.getId(),sqlTimeStamp, jwToken);
			ctx.response().headers().add("TOKEN", jwToken);
			respondWithJson(ctx, 200, jwToken);
		}else{
			respondWithJson(ctx, 500, rta);
		}
	}
	
	public void createAuth(RoutingContext ctx) {
		UsersDTO req = extractBodyAsJson(ctx, UsersDTO.class);
		int userCreated = AuthRepository.createUser(req.getDni(), req.getDniType(), req.getCountry(),
			req.getEmail(), req.getFirstName(), req.getLastName(), req.getAddress(), req.getPhone());
		if (userCreated == 1) {
			AuthRepository.createRoleAccess(req.getDni(), req.getRoleId());
			byte[] salt = AuthChecker.getNextSalt();
			byte[] hash = AuthChecker.hash(req.getPassword().toCharArray(), salt);
			int success = AuthRepository.createAuth(req.getDni(), false , hash, salt);
			if (success == 1 && req.getRoleId() == 1) AuthRepository.createOfferor(req.getDni(), req.getFirstName(), 1);
			String rta = success == 1 ? "user created successfully" : "The user could not be created"; 
			respondWithJson(ctx, 200, rta);
		} else {
			respondWithJson(ctx, 501, "The user could not be created");
		}
	}
	
	public void validateToken(RoutingContext ctx) {
		String token = ctx.request().getHeader("TOKEN");
		int userId = Integer.parseInt(ctx.getBodyAsString());
		
		TokenDTO tokenDto = AuthRepository.getToken(userId);
		if (tokenDto != null && tokenDto.getHashToken().equals(token)) {
			if(tokenDto.getExpiration().after(new Date())){
				respondWithJson(ctx, 200, tokenDto);
			}else{
				respondWithJson(ctx, 501, "The session expired");
			}
		}else{
			respondWithJson(ctx, 501, "The session does not exist");
		}
	}
	
	public void recoverPassword(RoutingContext ctx) {
		UsersDTO req = extractBodyAsJson(ctx, UsersDTO.class);
		UserDTO user = AuthRepository.getUserAuth(req.getDni());
		
		if (req.getDni() != 0 && req.getEmail() != null) {
			// Send password to user
			System.out.println("email: " + req.getEmail());
			System.out.println("pass: " + user.getAuth().getUserId());
			EmailSender.sendEmail(req.getEmail(), user.getAuth().getPassword().toString());
			respondWithJson(ctx, 200, "Email successfully sent to " + req.getEmail());
		}else{
			respondWithJson(ctx, 501, "Empty user data");
		}
	}
	
}
