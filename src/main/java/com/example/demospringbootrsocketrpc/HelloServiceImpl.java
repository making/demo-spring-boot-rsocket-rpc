package com.example.demospringbootrsocketrpc;

import com.example.hello.HelloRequest;
import com.example.hello.HelloResponse;
import com.example.hello.HelloService;
import io.netty.buffer.ByteBuf;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * https://grpc.io/docs/quickstart/java
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public Mono<HelloResponse> sayHello(HelloRequest message, ByteBuf metadata) {
        final HelloResponse response = HelloResponse.newBuilder()
            .setReply(String.format("Hello %s!", message.getGreeting()))
            .build();
        return Mono.just(response);
    }

    @Override
    public Flux<HelloResponse> lotsOfReplies(HelloRequest message, ByteBuf metadata) {
        return Flux.range(0, 10)
            .map(i -> HelloResponse.newBuilder()
                .setReply(String.format("[%d] Hello %s!", i, message.getGreeting()))
                .build());
    }
}
