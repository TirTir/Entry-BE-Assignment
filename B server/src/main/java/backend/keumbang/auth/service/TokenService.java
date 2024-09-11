package backend.keumbang.auth.service;

import backend.keumbang.auth.dto.TokenResponseDto;
import backend.keumbang.auth.entity.RefreshToken;
import backend.keumbang.auth.repository.RefreshTokenRepository;
import backend.keumbang.common.constants.ErrorMessages;
import backend.keumbang.common.constants.UserRole;
import backend.keumbang.common.exceptions.GeneralException;
import backend.keumbang.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenResponseDto getAuthToken(String userName, String password, UserRole role) {
        try {
            // 인증 시도 로그
            log.info("Attempting authentication for user: {}", userName);

            // 권한 생성 (아이디, 비밀번호)
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
            log.info("authenticationToken: {}", authenticationToken);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            log.info("Authentication successful ID: {}", userName);

            // Token 발급
            String accessToken = jwtTokenProvider.createAccessToken(authentication, role.name());
            log.info("Access token generated: {}", accessToken);

            String refreshToken = jwtTokenProvider.createRefreshToken(authentication, role.name());
            log.info("Refresh token generated: {}", refreshToken);

            // RefreshToken 저장
            RefreshToken redis = new RefreshToken(refreshToken, accessToken);
            refreshTokenRepository.save(redis);
            log.info("RefreshToken saved ID: {}", userName);

            return new TokenResponseDto(accessToken, refreshToken);
        } catch (GeneralException e) {
            log.error("Error in getAuthToken ID: {}", userName, e);
            throw e;
        }
    }

    public TokenResponseDto getRefreshToken(String refreshToken) throws Exception {
        try {
            // redis 엔티티 조회
            RefreshToken token = refreshTokenRepository.findById(refreshToken)
                    .orElseThrow(() -> new Exception(ErrorMessages.JWT_EXPIRED));

            // 권한 추출
            Authentication authentication = jwtTokenProvider.getAuthentication(token.getAccessToken());
            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElseThrow(() -> new GeneralException(ErrorMessages.ROLE_NOT_FOUND, HttpStatus.NOT_FOUND));

            // redis AccessToken 업데이트
            String newAccessToken = jwtTokenProvider.createAccessToken(authentication, role);
            token.updateAccessToken(newAccessToken);
            refreshTokenRepository.save(token);

            return new TokenResponseDto(newAccessToken, token.getRefreshToken());
        } catch (Exception e) {
            log.error("Error in getRefreshToken ID: {}", e);
            throw e;
        }
    }
}
