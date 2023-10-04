package xyz.ascii.unleashpoc.service.a;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.getunleash.Unleash;
import reactor.core.publisher.Mono;

@RestController
public class Resource {

    @Autowired
    private Unleash unleash;

    @GetMapping
    public Mono<String> getValue() {
        if (unleash.isEnabled("my-toggle")) {
            return Mono.just("The toggled value!");
        } else {
            return Mono.just("My default value");
        }
    }
}
