package org.abner.poc.kafka.streams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.abner.poc.kafka.streams.publishers.AccountCashPublisher;
import org.abner.poc.kafka.streams.publishers.PaymentPublisher;


@SpringBootApplication
public class SpringKafkaApplication {


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startPublishing() {
        System.out.println("Starting event publishing...");
        new Thread(new PaymentPublisher(kafkaTemplate)).start();
        new Thread(new AccountCashPublisher(kafkaTemplate)).start();
    }
}
