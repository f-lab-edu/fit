package fit;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtConfig {

    private final String jwtSecretKey;
    private final long jwtExpiryTime;

    public JwtConfig(String jwtSecretKey, long jwtExpiryTime) {
        this.jwtSecretKey = jwtSecretKey;
        this.jwtExpiryTime = jwtExpiryTime;
    }

    public String createToken(String email) {
        Claims claims = Jwts.claims();
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiryTime))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }

    public String getEmail(String token) {
        return jwtGetBody(token).get("email", String.class);
    }

    public boolean isExpired(String token) {
        return jwtGetBody(token).getExpiration().before(new Date());
    }

    private Claims jwtGetBody(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
