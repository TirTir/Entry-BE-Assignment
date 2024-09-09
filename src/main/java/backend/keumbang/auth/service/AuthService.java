package backend.keumbang.auth.service;

import backend.keumbang.auth.dto.RegisterRequestDto;
import backend.keumbang.auth.entity.User;
import backend.keumbang.auth.repository.UserRepository;
import backend.keumbang.common.constants.ErrorMessages;
import backend.keumbang.common.constants.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    public void RegisterService(RegisterRequestDto requestDto) throws IllegalAccessException {
        // 계정 중복 확인
        if (userRepository.existsByUserName(requestDto.getUserName())) {
            throw new IllegalAccessException(ErrorMessages.ALREADY_EXIST_USER);
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
}
