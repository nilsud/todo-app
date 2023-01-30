package com.example.todonew.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
@Slf4j
public class JwtService {
    private final SecurityConfig securityConfig;

    public JwtService(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }
    public Authentication verifyToken(String token) {
        if (StringUtils.isNotEmpty(token) && token.startsWith("Bearer")) {
            try {
                byte[] signingKey = securityConfig.getJwtSecret().getBytes();

                Jws<Claims> parsedToken = Jwts.parser()
                        .setSigningKey(signingKey)
                        .parseClaimsJws(token.replace("Bearer ", ""));

                String subject = parsedToken
                        .getBody()
                        .getSubject();

                if (StringUtils.isNotEmpty(subject)) {
                    return new UsernamePasswordAuthenticationToken(subject, null, null);
                }
            } catch (ExpiredJwtException exception) {
                log.warn("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
            } catch (UnsupportedJwtException exception) {
                log.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
            } catch (MalformedJwtException exception) {
                log.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
            } catch (SignatureException exception) {
                log.warn("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
            } catch (IllegalArgumentException exception) {
                log.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
            }
        }
        return null;
    }

    public String createToken(String subject) {
        byte[] signingKey = securityConfig.getJwtSecret().getBytes();
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .setHeaderParam("typ", "JWT")
                .setIssuer("todo-app")
                .setAudience("todo-app")
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 864000000))
                .compact();
    }
}
