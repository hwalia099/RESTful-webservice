package com.springcourse.modelRequest.shared;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.springcourse.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component //since need to autowire it
public class Utils {
	//utility class 
	private final Random RANDOM = new SecureRandom();
	private final String Alphabet= "0987654321azxscdfvbghnjmkloiyttrqdAQSWDEFTGYH";
	//ALPHABET WTHEVER I WISH FOR
	//fucntion to genrate gandom alphnumeric userID or anything
	public String generateUserId(int length)
	{
		return generatedRandomString(length);
	}
	public String generateAddressId(int length)
	{
		return generatedRandomString(length);
	}
	public String generatedRandomString(int length)
	{
		StringBuilder rv1=new StringBuilder(length);
		String rv="";
		for(int i=0;i<length;i++)
		{
			rv1.append(Alphabet.charAt(RANDOM.nextInt(Alphabet.length())));
			String s=Character.toString(Alphabet.charAt(RANDOM.nextInt(Alphabet.length())));
	        rv=rv.concat(s);	
		}
		return new String (rv1);
	}
	public static boolean hasTokenExpired(String token) {
		boolean returnValue = false;

	
			Claims claims = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret()).parseClaimsJws(token)
					.getBody();

			Date tokenExpirationDate = claims.getExpiration();
			Date todayDate = new Date();

			returnValue = tokenExpirationDate.before(todayDate); // before is a method og date class which returns true / false
		

		return returnValue;
	}

    public String generateEmailVerificationToken(String userId) {
        String token = Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();
        return token;
    }


   
	public String generatePasswordResetToken(String userId) {
		String token = Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.PASSWORD_RESET_EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();
        return token;
	}
	
}
