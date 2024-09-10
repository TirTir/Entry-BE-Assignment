package backend.resource.grpc;

import backend.keumbang.grpc.AuthRequest;
import backend.keumbang.grpc.AuthServiceGrpc;
import backend.keumbang.grpc.User;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class GrpcClientService {

    @GrpcClient("auth")
    private AuthServiceGrpc.AuthServiceBlockingStub authStub;

    public User auth(String accessToken) {
        AuthRequest request = AuthRequest.newBuilder()
                .setAccessToken(accessToken)
                .build();

        return authStub.auth(request);
    }
}
