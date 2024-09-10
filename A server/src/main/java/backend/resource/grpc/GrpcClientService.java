package backend.resource.grpc;

import backend.keumbang.grpc.AuthRequest;
import backend.keumbang.grpc.AuthResponse;
import backend.keumbang.grpc.AuthServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class GrpcClientService {

    @GrpcClient("authService")
    private AuthServiceGrpc.AuthServiceBlockingStub authStub;

    public String verifyToken(AuthRequest authRequest) {
        // 서버 B에 gRPC 요청을 보내 토큰 검증
        AuthResponse response = authStub.auth(authRequest);

        // 토큰이 유효하면 사용자 정보를 반환
        if (response.getIsValid()) {
            return String.format("Token is valid. User: %s, Role: %s", response.getUsername(), response.getRole());
        } else {
            return "Invalid token";
        }
    }
}
