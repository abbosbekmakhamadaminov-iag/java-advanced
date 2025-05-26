package com.epam.javadvanced.service;

import com.epam.javadvanced.proto.PingPongServiceGrpc;
import com.epam.javadvanced.proto.PingRequest;
import com.epam.javadvanced.proto.PongResponse;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PingPongServiceImpl extends PingPongServiceGrpc.PingPongServiceImplBase {

    @Override
    public void sendPing(PingRequest request, StreamObserver<PongResponse> responseObserver) {
        log.info("Received ping request: {}", request.getMessage());

        PongResponse response = PongResponse.newBuilder()
                .setMessage("Pong")
                .setTimestamp(System.currentTimeMillis())
                .setServerId("javadvanced-grpc")
                .setSuccess(Boolean.TRUE)
                .build();

        log.info("Sending pong response: {}", response.getMessage());

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
