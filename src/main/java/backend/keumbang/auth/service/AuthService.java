package backend.keumbang.auth.service;

import backend.keumbang.auth.dto.LoginRequestDto;
import backend.keumbang.auth.dto.RegisterRequestDto;
import backend.keumbang.auth.dto.TokenResponseDto;
import backend.keumbang.auth.entity.User;
import backend.keumbang.auth.repository.UserRepository;
import backend.keumbang.common.constants.ErrorMessages;
import backend.keumbang.common.constants.UserRole;
import backend.keumbang.common.exceptions.AlreadyExistUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public void RegisterService(RegisterRequestDto requestDto){
        // 계정 중복 확인
        if (userRepository.existsByUserName(requestDto.getUserName())) {
            throw new AlreadyExistUserException(ErrorMessages.ALREADY_EXIST_USER);
        }

        UserRole role = requestDto.getRole() != null ? requestDto.getRole() : UserRole.USER;

        // 유저 생성
        User newUser = User.builder()
                .userName(requestDto.getUserName())
                .password(encoder.encode(requestDto.getPassword())) // 비밀번호 암호화
                .role(role)
                .build();

        userRepository.save(newUser);
    }

    public void LoginService(LoginRequestDto requestDto) {
        // 계정 여부 확인
        User user = userRepository.findByUserName(requestDto.getUserName())
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND));

        boolean passwordMatches = encoder.matches(requestDto.getPassword(), user.getPassword());
        if (passwordMatches) {

            // 토큰 발급
            TokenResponseDto token = tokenService.getAuthToken(user.getUserName(), requestDto.getPassword(), user.getRole());

            return new TokenResponseDto(token.getAccessToken(), token.getRefreshToken());
        } else {
            throw new BadCredentialsException(ErrorMessages.WRONG_PASSWORD);
        }
    }
}
