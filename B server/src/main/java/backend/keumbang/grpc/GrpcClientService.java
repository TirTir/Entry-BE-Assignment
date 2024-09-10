package backend.keumbang.grpc;

import backend.keumbang.jwt.JwtTokenProvider;
import backend.keumbang.jwt.JwtTokenUtil;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class GrpcClientService extends AuthServiceGrpc.AuthServiceImplBase {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public GrpcClientService(JwtTokenProvider jwtTokenProvider, JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public void auth(AuthRequest request, StreamObserver<AuthResponse> responseObserver) {
        String accessToken = request.getAccessToken();
        AuthResponse.Builder responseBuilder = AuthResponse.newBuilder();

        // 토큰 유효성 검사
        if (jwtTokenProvider.validateToken(accessToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(accessToken);
            String role = jwtTokenUtil.getRoleFromToken(accessToken);

            responseBuilder.setIsValid(true)
                    .setUsername(username)
                    .setRole(role);
        } else {
            responseBuilder.setIsValid(false);
        }


        // 응답 전송
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}