package backend.keumbang.jwt;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.IOException;

import static org.hibernate.query.sqm.tree.SqmNode.log;

public class JwtTokenFilter implements Filter {
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil, JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String jwtToken = resolveToken(httpRequest); // JWT 토큰
        String requestURI = httpRequest.getRequestURI(); // 요청 URI

        // 토큰 유효성 검사
        if (StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken)) {
            // Authentication 객체 생성
            Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug(String.format("Security Context에 %s 인증 정보를 저장했습니다. URI : %s", authentication.getName(), requestURI));
        } else {
            log.debug(String.format("유효한 JWT 토큰이 없습니다. URI: %s", requestURI));
        }

        //다음 필터로 넘기기
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) { // 헤더에 'Bearer' 여부 확인
            return bearerToken.substring(7); // Bearer 제외 토큰 값 반환
        }

        return null;
    }
}
