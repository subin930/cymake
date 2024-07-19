package CY.cymake.Security;

/*
 * Jwt 관련 메서드를 제공하는 클래스
 */

import CY.cymake.Domain.Auth.Dto.CustomUserInfoDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {
    private final Key key;

    private static final long accessTokenExpTime = 1000 * 60 * 30; // 30분
    private static final long refreshTokenExpTime = 1000 * 60 * 60 * 24 * 7; // 7일
    public JwtUtil(
            @Value("${jwt.secret}") String secretKey
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    /*
     * Access Token 생성
     */
    public String createAccessToken(CustomUserInfoDto user) {
        return createToken(user, accessTokenExpTime);
    }

    /*
     * Refresh Token 생성
     */
    public String createRefreshToken(CustomUserInfoDto user) {
        return createToken(user, refreshTokenExpTime);
    }

    /*
     * jwt 생성
     */
    private String createToken(CustomUserInfoDto user, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("companyCode", user.getCompanyCode());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());

        ZonedDateTime now = ZonedDateTime.now(); //현재 시간
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime); //현재시간 + 유효시간(expireTime)

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    /*
     * Token 에서 id 추출
     */
    public String getId(String token) {
        return parseClaims(token).get("id", String.class);
    }

    /*
     * JWT 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    /*
     * JWT Claims 추출
     */
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
    public String getExpireIn() {
        return Long.toString(accessTokenExpTime);
    }

}
