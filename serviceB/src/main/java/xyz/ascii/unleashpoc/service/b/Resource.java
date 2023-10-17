package xyz.ascii.unleashpoc.service.b;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.getunleash.Unleash;
import reactor.core.publisher.Mono;

@RestController
public class Resource {

    @Autowired
    private Unleash unleash;

    @Autowired
    private ServiceCClient serviceCClient;

    @GetMapping
    public String getLocalValue() {
        return addTimestamp(getMyLocalMessage());
    }

    private String getMyLocalMessage() {
        if (unleash.isEnabled("serviceB-toggle")) {
            return "The toggled value (local)!";
        } else {
            return "My default value (local)";
        }
    }

    @GetMapping("remote")
    public Mono<String> getDistributedValue() {
        return getMessage()
            .map(this::addTimestamp);
    }

    private String addTimestamp(String message) {
        return String.format("Current time: %s\n%s", OffsetDateTime.now(), message);
    }

    private Mono<String> getMessage() {
        StringBuilder messageBuilder = new StringBuilder();

        messageBuilder
            .append("Service B: ")
            .append(getMyDistributedMessage());

        return serviceCClient.getServiceCMessage()
            .map(message -> messageBuilder.append("Service C: ").append(message).toString());
    }

    private String getMyDistributedMessage() {
        if (unleash.isEnabled("distributed-toggle")) {
            return "The toggled value!";
        } else {
            return "My default value";
        }
    }
}
