package kr.codechobo.config.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/17
 */

@Component
public class TokenManager {

    private Key key;

    public TokenManager(@Value("${app.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createToken(String nickname) {
        return Jwts.builder()
                .signWith(key)
                .claim("nickname", nickname)
                .compact();
    }
}
