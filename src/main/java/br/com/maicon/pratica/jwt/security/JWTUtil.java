package br.com.maicon.pratica.jwt.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JWTUtil {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_AUTHORIZATION_PREFIX = "Bearer ";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact();
    }

    public Optional<String> validarToken(String token) {
        return getClaims(token).filter(claims -> {
            final Date expiration = claims.getExpiration();
            return claims.getSubject() != null && expiration != null &&
                    new Date(System.currentTimeMillis()).before(expiration);
        }).map(Claims::getSubject);
    }

    private Optional<Claims> getClaims(String token) {
        try {
            return Optional.of(Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .parseClaimsJws(token)
                    .getBody());
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
                SignatureException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
