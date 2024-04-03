package ru.cfuv.cfuvscheduling.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    private String secret = "somesecretforjwt";

    private Key hmacKey = new SecretKeySpec(
            Base64.getDecoder().decode(secret),
            SignatureAlgorithm.HS256.getJcaName()
    );

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        String jwt = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, hmacKey)
                .compact();

        return jwt;
    }

    public String parseJwt(String jwt) {
        Jws<Claims> jws = Jwts.parser().setSigningKey(hmacKey).parseClaimsJws(jwt.substring(jwt.indexOf(' ') + 1));

        return jws.getBody().get("username", String.class);
    }
}
