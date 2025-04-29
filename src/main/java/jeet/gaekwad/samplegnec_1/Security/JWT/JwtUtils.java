package jeet.gaekwad.samplegnec_1.Security.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final String JWT_SECRET = "your-secret-key-must-be-more-than-32-bit";
    private static final Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

    public String generateToken(String name, int timeInMinutes) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + timeInMinutes * 60 * 1000);
        return Jwts.builder()
                .subject(name)
                .claim("username", name)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateAndExtractUsername(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (JwtException e) {
            System.out.println("JWT validation error: " + e.getMessage());
            return null;
        }
    }
}
