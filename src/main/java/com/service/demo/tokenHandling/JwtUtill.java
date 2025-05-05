package com.service.demo.tokenHandling;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.service.demo.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Signature;
import java.util.Date;

@Service
public class JwtUtill {

    @Value("${jwt.algorithim.key}")
    private String algorithimKey;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.duration}")
    private int expiryTime;

    private Algorithm algorithm;

    private static final  String USER_NAME = "username";

    @PostConstruct
    public void PostConstruct(){
         algorithm = Algorithm.HMAC256(algorithimKey);
    }


    public String generateToken(User user){

        return JWT.create().withClaim(USER_NAME,user.getName())
                .withExpiresAt(new Date(System.currentTimeMillis()+expiryTime))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String extractToken(String token){
        DecodedJWT decode = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
       return decode.getClaim(USER_NAME).asString();
    }

}
