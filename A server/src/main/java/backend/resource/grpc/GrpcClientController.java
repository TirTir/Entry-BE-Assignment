package backend.resource.grpc;

import backend.keumbang.grpc.AuthRequest;
import backend.resource.auth.dto.AuthRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GrpcClientController {
    private final GrpcClientService grpcClientService;

    @PostMapping("/auth/verify")
    public ResponseEntity<String> validationToken(@RequestBody AuthRequestDto requestDto) {
        AuthRequest authRequest = AuthRequest.newBuilder()
                .setAccessToken(requestDto.getAccessToken())
                .build();

        // 서버 B로 gRPC 호출을 통해 토큰 검증 요청
        String result = grpcClientService.verifyToken(authRequest);

        return ResponseEntity.ok(result);
    }
}
