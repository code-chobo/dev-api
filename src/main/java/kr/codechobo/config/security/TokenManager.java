package kr.codechobo.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kr.codechobo.domain.Account;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/17
 */

@Component
public class TokenManager {

    private Key key;
    private static final long EXPIRATION_TIME = 864_000_000; // 10days

    public TokenManager(@Value("${app.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createToken(Account account) {
        return Jwts.builder()
                .setId(String.valueOf(account.getId()))
                .signWith(key)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }

    public Long extractAccountIdFromToken(String token) {
        return Long.parseLong(extractClaimFromToken(token, Claims::getId));
    }

    private Date extractExpirationDateFromToken(String token) {
        return extractClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T extractClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaimsFromToken(String token) {
        String jws = token.substring(7);

        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws).getBody();
        }catch (Exception e) {
            throw new BadCredentialsException("Invalid Token");
        }
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = extractExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
}
