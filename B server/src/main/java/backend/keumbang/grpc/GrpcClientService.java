package backend.keumbang.grpc;

import backend.keumbang.auth.entity.User;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GrpcClientService extends AuthServiceGrpc.AuthServiceImplBase {

    @Override
    public void auth(AuthRequest request, StreamObserver<AuthResponse> responseObserver) {
        String token = request.getAccessToken();
        AuthResponse.Builder responseBuilder = AuthResponse.newBuilder();

        // 토큰 검증 로직
        boolean isValid = validateToken(token);

        if (isValid) {
            // 토큰이 유효한 경우 사용자 정보 반환
            User user = User.newBuilder()
                    .setUserId("user123")
                    .setUsername("john.doe")
                    .setRole("admin")
                    .build();

            responseBuilder.setIsValid(true).setUser(user);
        } else {
            // 토큰이 유효하지 않은 경우
            responseBuilder.setIsValid(false);
        }

        // 응답 전송
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    public AuthResponse validateToken(String accessToken) {

    }
}