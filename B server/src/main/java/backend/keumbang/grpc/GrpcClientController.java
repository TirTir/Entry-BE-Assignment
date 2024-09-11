package backend.keumbang.grpc;

import backend.keumbang.auth.dto.AuthRequestDto;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GrpcClientController {

    private final GrpcClientService grpcClientService;

    @PostMapping("/auth")
    public void Auth(AuthRequestDto requestDto) {
        // AuthRequest 객체 생성
        AuthRequest authRequest = AuthRequest.newBuilder()
                .setAccessToken(requestDto.getAccessToken())
                .build();

        // gRPC StreamObserver
        StreamObserver<AuthResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(AuthResponse authResponse) {

            }

            @Override
            public void onError(Throwable throwable) {
                // 에러 처리
                System.err.println("Error: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                // 완료 처리
                System.out.println("Request completed.");
            }
        };

        grpcClientService.auth(authRequest, responseObserver);
    }
}