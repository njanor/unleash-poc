package xyz.ascii.unleashpoc.service.a;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.getunleash.DefaultUnleash;
import io.getunleash.Unleash;
import io.getunleash.util.UnleashConfig;

@Configuration
public class UnleashConfiguration {

    @Value("${unleash.apikey}")
    private String unleashApiKey;
    
    @Bean
    public Unleash unleash() {
        UnleashConfig config = UnleashConfig.builder()
        .appName("xyz.ascii.unleashpoc.service.a")
        .instanceId("1")
        .unleashAPI("http://unleash:4242/api/")
        .environment("development")
        .apiKey(unleashApiKey)
        .build();

        return new DefaultUnleash(config);
    }
}
