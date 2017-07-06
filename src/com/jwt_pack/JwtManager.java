package com.jwt_pack;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import io.jsonwebtoken.*;
import java.util.Date;

public class JwtManager {  
	 
	//Sample method to construct a JWT
	public static String createJWT(String id, String issuer) {
	 
	    //The JWT signature algorithm we will be using to sign the token
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	 
	 
	    //We will sign our JWT with our ApiKey secret
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Constants.API_KEY);
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	 
	    //Let's set the JWT Claims
	    JwtBuilder builder = Jwts.builder().setId(id)
	                                .setIssuer(issuer)
	                                .signWith(signatureAlgorithm, signingKey);
	 
	    //Builds the JWT and serializes it to a compact, URL-safe string
	    return builder.compact();
	}
	
	
	//Sample method to validate and read the JWT
	public static boolean parseJwt(String jwt, String username) {
	 
	    boolean validToken = false;   
		Claims claims;
		try {
			//This line will throw an exception if it is not a signed JWS (as expected) 
			claims = Jwts.parser()         
			   .setSigningKey(DatatypeConverter.parseBase64Binary(Constants.API_KEY))
			   .parseClaimsJws(jwt).getBody();
			
		    if(claims.getId().toString().equals(username))
		    {
		    	validToken = true;
		    }

		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			return false;
		} catch (UnsupportedJwtException e) {
			e.printStackTrace();
			return false;
		} catch (MalformedJwtException e) {
			e.printStackTrace();
			return false;
		} catch (SignatureException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		}
	    

	    /*
		    System.out.println("ID: " + claims.getId());
		    System.out.println("Subject: " + claims.getSubject());
		    System.out.println("Issuer: " + claims.getIssuer());
		    //System.out.println("Expiration: " + claims.getExpiration());
	     */
	    return validToken;
	}
	
}
