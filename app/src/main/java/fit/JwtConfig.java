package fit;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.UUID;

public class JwtConfig {

    private final String jwtSecretKey;
    private final long jwtExpiryTime;

    public JwtConfig(String jwtSecretKey, long jwtExpiryTime) {
        this.jwtSecretKey = jwtSecretKey;
        this.jwtExpiryTime = jwtExpiryTime;
    }

    public String createToken(UUID id, String email) {
        Claims claims = Jwts.claims();
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(id.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiryTime))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }

    public MemberView getMemberView(String token) {
        return new MemberView(
                UUID.fromString(jwtGetBody(token).getSubject()),
                jwtGetBody(token).get("email", String.class)
        );
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