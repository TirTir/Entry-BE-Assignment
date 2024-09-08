package backend.keumbang.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long accessExpirationTime;
    private final long refreshExpirationTime;

    @Autowired
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token.access-expiration-time}") long accessExpirationTime,
            @Value("${jwt.token.refresh-expiration-time}") long refreshExpirationTime) {

        // 시크릿 값을 복호화하여 SecretKey 생성
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);

        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
    }

    public SecretKey getSecretKey() {
        return this.secretKey;  // 시크릿 키 반환
    }

    /**
     * Access 토큰 생성
     */
    public String createAccessToken(Authentication authentication){
        Claims claims = Jwts.claims().setSubject(authentication.getName()).build();

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessExpirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey, SignatureAlgorithm.HS512) // HMAC + SHA512
                .compact();
    }

    /**
     * Refresh 토큰 생성
     */
    public String createRefreshToken(Authentication authentication){
        Claims claims = Jwts.claims().setSubject(authentication.getName()).build();

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshExpirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey, SignatureAlgorithm.HS512) // HMAC + SHA512
                .compact();
    }

    /**
     * 토큰 유효성 검사
     */
    public boolean validateToken(String jwtToken){
        try {
            Jwts.parser()
                    .setSigningKey(secretKey.getEncoded())  // 서명 검증에 사용할 시크릿 키 설정
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();  // 서명 검증
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            e.printStackTrace();
        }

        return false;
    }
}
