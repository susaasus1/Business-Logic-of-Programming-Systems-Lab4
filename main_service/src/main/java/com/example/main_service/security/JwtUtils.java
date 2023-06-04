package com.example.main_service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Component
@Slf4j
public class JwtUtils {
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Value("${token.JWTExpirationMS}")
    private long jwtExpirationMs;

    @Value("${token.issuer}")
    private String issuer;
    @Value("${token.RTExpirationMs}")
    private long refreshExpirationMS;

    public String generateToken(String login, Collection<? extends GrantedAuthority> roles, long time,
                                String email) {
        Instant now = Instant.now();
        ZoneId utcZone = ZoneId.of("UTC");
        ZonedDateTime utcNow = ZonedDateTime.ofInstant(now, utcZone);
        ZonedDateTime utcExpiration = utcNow.plus(Duration.ofMillis(time));
        List<String> authorities = roles.stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("authorities", authorities);
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setIssuedAt(Date.from(utcNow.toInstant()))
                .setExpiration(Date.from(utcExpiration.toInstant()))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateJWTToken(String login, Collection<? extends GrantedAuthority> roles,
                                   String email) {
        return generateToken(login, roles, jwtExpirationMs, email);
    }


    public String generateRefreshToken(String login, Collection<? extends GrantedAuthority> roles,
                                       String email) {
        return generateToken(login, roles, refreshExpirationMS, email);
    }


    public boolean validateJwtToken(String jwt) {
        try {
            if (!Jwts.parserBuilder().setSigningKey(key).build().
                    parseClaimsJws(jwt).getBody().getIssuer().equals(issuer))
                throw new SignatureException("Invalid JWT signature!");
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }


    public String getLoginFromJwtToken(String jwt) {

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody().getSubject();
    }

    public Collection<? extends GrantedAuthority> getAuthoritiesFromToken(String jwt) {
        List<String> authoritiesStr = (List<String>) Jwts.parserBuilder().setSigningKey(key).build().
                parseClaimsJws(jwt).getBody().get("authorities");
        List<GrantedAuthority> authorities = authoritiesStr.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }

    public String getEmailFromToken(String jwt){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody()
                .get("email").toString();
    }
}
