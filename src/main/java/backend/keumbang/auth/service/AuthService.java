package backend.keumbang.auth.service;

import backend.keumbang.auth.dto.RegisterRequestDto;
import backend.keumbang.auth.entity.User;
import backend.keumbang.auth.repository.UserRepository;
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

    public void RegisterService(RegisterRequestDto requestDto) {
        // 유저 생성
        User newUser = User.builder()
                .userName(requestDto.getUserName())
                .password(encoder.encode(requestDto.getPassword())) // 비밀번호 암호화
                .build();

        userRepository.save(newUser);
    }
}
