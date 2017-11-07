package co.com.uniandes.arquitectura.auth.jwt;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import io.jsonwebtoken.*;
import java.util.Date; 

public class JWTGenerator {

	public static String createJWT(String id, String subject, long ttlMillis, int role) {
	 
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	 
	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);
	 
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("temporal");
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	 
	    JwtBuilder builder = Jwts.builder().setId(id)
	                                .setIssuedAt(now)
	                                .setSubject(subject)
	                                .setAudience(Integer.toString(role))
	                                .signWith(signatureAlgorithm, signingKey);
	 
	    if (ttlMillis >= 0) {
	    long expMillis = nowMillis + ttlMillis;
	        Date exp = new Date(expMillis);
	        builder.setExpiration(exp);
	    }
	 
	    return builder.compact();
	}
	
	public static void parseJWT(String jwt) {
	 
	    Claims claims = Jwts.parser()         
	       .setSigningKey(DatatypeConverter.parseBase64Binary("temporal"))
	       .parseClaimsJws(jwt).getBody();
	    System.out.println("ID: " + claims.getId());
	    System.out.println("Subject: " + claims.getSubject());
	    System.out.println("Expiration: " + claims.getExpiration());
	}
	
}
