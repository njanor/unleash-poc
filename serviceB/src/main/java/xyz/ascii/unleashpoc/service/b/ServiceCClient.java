package xyz.ascii.unleashpoc.service.b;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class ServiceCClient {

    private final WebClient webClient;

    public ServiceCClient(@Value("${service.c.url}") String serviceCUrl) {
        webClient = WebClient.create(serviceCUrl);
    }

    public Mono<String> getServiceCMessage() {
        return webClient.get().retrieve().bodyToMono(String.class);
    }
}
