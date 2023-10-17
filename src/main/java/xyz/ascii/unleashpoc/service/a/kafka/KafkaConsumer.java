package xyz.ascii.unleashpoc.service.a.kafka;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.getunleash.Unleash;
import jakarta.annotation.PostConstruct;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.util.retry.RetrySpec;

@Service
public class KafkaConsumer {
    
    @Autowired
    KafkaReceiver<Integer, String> kafkaReceiver;

    @Autowired
    Unleash unleash;

    @PostConstruct
    void consumeKafkaTopic() {
        kafkaReceiver.receive()
            .<ReceiverRecord<Integer, String>>handle((item, sink) -> {
                if (unleash.isEnabled("kafka-toggle")) {
                    sink.next(item);
                } else {
                    sink.error(new RuntimeException("Not subscribed"));
                }
            })
            .doOnError(error -> System.out.println("'kafka-toggle' not enabled yet"))
            .retryWhen(RetrySpec.fixedDelay(1000, Duration.ofSeconds(10)))
            .doOnNext(record -> System.out.printf("%s: Processing record %s\nValue %s", LocalDateTime.now(), record.offset(), record.value()))
            .doOnNext(record -> record.receiverOffset().acknowledge())
            .count()
            .subscribe();
    }
}
