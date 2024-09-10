package backend.keumbang.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    private final SecretKey secretKey;

    @Autowired
    public JwtTokenUtil(JwtTokenProvider jwtTokenProvider) {
        this.secretKey = jwtTokenProvider.getSecretKey();  // JwtTokenProvider에서 시크릿 키를 가져옴
    }

    /**
     * 토큰에서 사용자 아이디 추출
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 토큰에서 사용자 역할 추출
     * @param token
     * @return
     */
    public String getRoleFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("role", String.class)); // "role" 클레임에서 역할을 추출
    }

    /**
     * 토큰 만료 시간 확인
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 토큰에서 특정 클레임 추출
     * @param token
     * @param claimsResolver 클레임을 추출하는 함수
     * @return 클레임
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 토큰에서 클레임 추출
     * @param token
     * @return 클레임
     */
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
