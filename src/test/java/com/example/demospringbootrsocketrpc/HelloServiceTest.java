package com.example.demospringbootrsocketrpc;

import com.example.hello.HelloRequest;
import com.example.hello.HelloResponse;
import com.example.hello.HelloService;
import com.example.hello.HelloServiceClient;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.EmptyByteBuf;
import io.rsocket.RSocket;
import org.junit.jupiter.api.Test;
import org.springframework.boot.rsocket.context.LocalRSocketServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.TestConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;

@SpringBootTest(properties = "spring.rsocket.server.port=0")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class HelloServiceTest {

    private final HelloService helloService;

    HelloServiceTest(@LocalRSocketServerPort int port, RSocketRequester.Builder builder) {
        final RSocket rsocket = builder.connectWebSocket(URI.create("ws://localhost:" + port + "/rsocket"))
            .map(RSocketRequester::rsocket)
            .block();
        this.helloService = new HelloServiceClient(rsocket);
    }

    @Test
    void sayHello() {
        final HelloRequest request = HelloRequest.newBuilder().setGreeting("RSocket").build();
        final Mono<HelloResponse> response = this.helloService.sayHello(request, new EmptyByteBuf(ByteBufAllocator.DEFAULT));

        StepVerifier.create(response)
            .expectNext(HelloResponse.newBuilder().setReply("Hello RSocket!").build())
            .expectComplete()
            .verify();
    }

    @Test
    void lotsOfReplies() {
        final HelloRequest request = HelloRequest.newBuilder().setGreeting("RSocket").build();
        final Flux<HelloResponse> response = this.helloService.lotsOfReplies(request, new EmptyByteBuf(ByteBufAllocator.DEFAULT));

        StepVerifier.create(response)
            .expectNext(HelloResponse.newBuilder().setReply("[0] Hello RSocket!").build())
            .expectNext(HelloResponse.newBuilder().setReply("[1] Hello RSocket!").build())
            .expectNext(HelloResponse.newBuilder().setReply("[2] Hello RSocket!").build())
            .expectNext(HelloResponse.newBuilder().setReply("[3] Hello RSocket!").build())
            .expectNext(HelloResponse.newBuilder().setReply("[4] Hello RSocket!").build())
            .expectNext(HelloResponse.newBuilder().setReply("[5] Hello RSocket!").build())
            .expectNext(HelloResponse.newBuilder().setReply("[6] Hello RSocket!").build())
            .expectNext(HelloResponse.newBuilder().setReply("[7] Hello RSocket!").build())
            .expectNext(HelloResponse.newBuilder().setReply("[8] Hello RSocket!").build())
            .expectNext(HelloResponse.newBuilder().setReply("[9] Hello RSocket!").build())
            .expectComplete()
            .verify();
    }
}
