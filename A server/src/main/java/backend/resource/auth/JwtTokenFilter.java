package backend.resource.auth;

import backend.keumbang.grpc.AuthResponse;
import backend.resource.grpc.GrpcClientService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
@Component
public class JwtTokenFilter implements Filter {
    private final GrpcClientService grpcClientService;

    public JwtTokenFilter(GrpcClientService grpcClientService) {
        this.grpcClientService = grpcClientService;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String accessToken = resolveToken(httpRequest); // JWT 토큰
        String requestURI = httpRequest.getRequestURI(); // 요청 URI

        // 토큰 여부 확인
        if (StringUtils.hasText(accessToken)) {
            // gRPC 호출
            AuthResponse authResponse = grpcClientService.verifyToken(accessToken);

            CustomAuthentication authentication = new CustomAuthentication(authResponse.getUsername(), authResponse.getRole());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug(String.format("Security Context에 %s 인증 정보를 저장했습니다. URI : %s", authResponse.getUsername(), requestURI));
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
