package backend.keumbang.auth.controller;

import backend.keumbang.auth.dto.RegisterRequestDto;
import backend.keumbang.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public void signup(@RequestBody @Valid RegisterRequestDto requestDto){
        log.info("[회원가입 요청] 회원명: {}", requestDto.getUserName());
        authService.RegisterService(requestDto);
    }
}
