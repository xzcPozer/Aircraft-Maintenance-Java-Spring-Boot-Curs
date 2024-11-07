package com.sharafutdinov.aircraft_maintenance.security.jwt;

import com.sharafutdinov.aircraft_maintenance.security.user.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JWTService {
    @Value("${auth.token.jwtSecret}")
    private String jwtSecret; // ключ для подписи JWT-токенов.

    @Value("${auth.token.expirationInMils}")
    private int expirationTime; // время жизни JWT-токена в миллисекундах

    public String generateTokenForUser(Authentication authentication){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // преобразование коллекции в список строк
        String role = userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

        // создание JWT токена
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("id", userPrincipal.getId())
                .claim("roles", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationTime))
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }

    // декодирует секретный ключ и возвращает ключ для подписки
    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // получение claim в зависимости от задачи
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // получение всех claim
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // получение логина пользователя
    public String getUsernameFromToken(String token){
        return extractClaim(token, Claims::getSubject);
    }

    // получение id пользователя
    public Long getIdFromToken(String token){
        String extractToken = token.substring(7);
        return extractClaim(extractToken, claim -> claim.get("id", Long.class));
    }

    // валидация пользователя
    public boolean validateToken(String token, UserDetails userDetails){
        try {
            final String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
                 | SignatureException | IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }
    }

    // проверка истечения срока jwt
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

}
