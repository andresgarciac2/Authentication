package co.com.uniandes.arquitectura.auth;

import java.sql.Timestamp;
import java.util.Date;

import co.com.uniandes.arquitectura.auth.jwt.JWTGenerator;
import co.com.uniandes.arquitectura.controller.Controller;
import co.com.uniandes.arquitectura.jdbc.connection.AuthRepository;
import co.com.uniandes.arquitectura.persistence.LoginDTO;
import co.com.uniandes.arquitectura.persistence.TokenDTO;
import co.com.uniandes.arquitectura.persistence.UserDTO;
import io.vertx.rxjava.ext.web.RoutingContext;

/**
 * @author andresgarcias4n
 *
 */
public class AuthenticationController implements Controller {

	public void login(RoutingContext ctx) {
		LoginDTO req = extractBodyAsJson(ctx, LoginDTO.class);
		UserDTO user = AuthRepository.getUserAuth(req.getUser());
		String jwToken = ""; 
		boolean success = AuthChecker.isExpectedPassword(req.getPassword().toCharArray(), user.getAuth().getSalt(), user.getAuth().getPassword());
		String rta = success ? "the password match" : "the user or password does not match";
		if (success) {
			jwToken = JWTGenerator.createJWT(Integer.toString(user.getId()), "nnn", 1111111);
			Timestamp sqlTimeStamp = new java.sql.Timestamp(new Date(System.currentTimeMillis() + 1111111).getTime());
			AuthRepository.createUserToken(user.getId(),sqlTimeStamp, jwToken);
			ctx.response().headers().add("TOKEN", jwToken);
			respondWithJson(ctx, 200, jwToken);
		}else{
			respondWithJson(ctx, 500, rta);
		}
	}
	
	public void createAuth(RoutingContext ctx) {
		LoginDTO req = extractBodyAsJson(ctx, LoginDTO.class);
		int userCreated = AuthRepository.createUser(req.getUser(), req.getRoleId());
		if (userCreated == 1) {
			byte[] salt = AuthChecker.getNextSalt();
			byte[] hash = AuthChecker.hash(req.getPassword().toCharArray(), salt);
			int success = AuthRepository.createAuth(req.getUser(), hash, salt);
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
}
