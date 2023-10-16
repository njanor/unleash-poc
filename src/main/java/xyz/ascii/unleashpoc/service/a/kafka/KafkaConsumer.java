package xyz.ascii.unleashpoc.service.a.kafka;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.getunleash.Unleash;
import jakarta.annotation.PostConstruct;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.util.retry.Retry;

@Service
public class KafkaConsumer {
    
    @Autowired
    KafkaReceiver<Integer, String> kafkaReceiver;

    @Autowired
    Unleash unleash;

    @PostConstruct
    void consumeKafkaTopic() {
        kafkaReceiver.receive()
        //.retryWhen(Retry.backoff(0, null))
            .doOnNext(record -> System.out.printf("%s: Processing record %s\nValue %s", LocalDateTime.now(), record.offset(), record.value()))
            .doOnNext(record -> record.receiverOffset().acknowledge())
            .count()
            .subscribe();
    }
}
