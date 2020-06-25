package com.springcourse.security;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import org.springframework.security.core.userdetails.User;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
//import javax.naming.AuthenticationException;
//import com.appsdeveloperblog.app.ws.ui.model.request.UserLoginRequestModel;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcourse.modelRequest.UserLoginRequestModel;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
                                          //Ye UPAF(below) spring framework se mila hai
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager; //ye b spring provided
    
    //private String contentType;
 
    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    //json token is used for authoriztion  and not for returning info
    @Override 
        public Authentication attemptAuthentication(HttpServletRequest req, 
                                                     HttpServletResponse res) throws AuthenticationException { 
             try { 
             	 
             	//contentType = req.getHeader("Accept"); 
             	 //ye meri class hai
                UserLoginRequestModel creds = new ObjectMapper() 
                        .readValue(req.getInputStream(), UserLoginRequestModel.class); 
                
                return authenticationManager.authenticate( 
                        new UsernamePasswordAuthenticationToken( 
                                 creds.getEmail(), 
                                creds.getPassword(), 
                                new ArrayList<>()) 
                ); 
                 
            } catch (IOException e) { 
               throw new RuntimeException(e); 
            } 
        } 

    
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        
        String userName = ((User) auth.getPrincipal()).getUsername();  
        
        String token = Jwts.builder()
                .setSubject(userName) //setting expire time of token and other param
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET )
                .compact();
        //UserService userService = (UserService)SpringApplicationContext.getBean("userServiceImpl");
       // UserDto userDto = userService.getUser(userName);
        
        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
       // res.addHeader("UserID", userDto.getUserId());

    }  

}
