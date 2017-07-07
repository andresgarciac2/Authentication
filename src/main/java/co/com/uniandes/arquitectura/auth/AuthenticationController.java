package co.com.uniandes.arquitectura.auth;

import co.com.uniandes.arquitectura.auth.jwt.JWTGenerator;
import co.com.uniandes.arquitectura.controller.Controller;
import co.com.uniandes.arquitectura.persistence.LoginDTO;
import io.vertx.rxjava.ext.web.RoutingContext;

/**
 * @author andresgarcias4n
 *
 */
public class AuthenticationController implements Controller {

	public void login(RoutingContext ctx) {

		LoginDTO req = extractBodyAsJson(ctx, LoginDTO.class);
		String jwToken = ""; 
		byte[] salt = AuthChecker.getNextSalt();
		byte[] hash = AuthChecker.hash(req.getPassword().toCharArray(), salt);
		boolean success = AuthChecker.isExpectedPassword(req.getPassword().toCharArray(), salt, hash);
		String rta = success ? "the password match" : "the password does not match";
		if (success) {
			jwToken = JWTGenerator.createJWT("iddd", "andres", "nnn", 1111111);
			System.out.println(jwToken);
			JWTGenerator.parseJWT(jwToken);
		}
		respondWithJson(ctx, 200, rta);
	}
}
