package com.example.demospringbootrsocketrpc;

import com.example.hello.HelloServiceServer;
import io.rsocket.rpc.rsocket.RequestHandlingRSocket;
import org.springframework.boot.rsocket.context.RSocketServerBootstrap;
import org.springframework.boot.rsocket.server.RSocketServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Configuration
public class RSocketConfig {

    @Bean
    public RSocketServerBootstrap rSocketServerBootstrap(RSocketServerFactory factory) {
        final HelloServiceImpl helloService = new HelloServiceImpl();
        final HelloServiceServer helloServiceServer = new HelloServiceServer(helloService, Optional.empty(), Optional.empty());
        return new RSocketServerBootstrap(factory, (setup, sendingSocket) -> Mono.just(new RequestHandlingRSocket(helloServiceServer)));
    }
}
