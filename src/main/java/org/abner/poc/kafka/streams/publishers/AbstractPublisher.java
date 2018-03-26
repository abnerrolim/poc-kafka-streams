package org.abner.poc.kafka.streams.publishers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.abner.poc.kafka.streams.events.Event;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class AbstractPublisher implements Runnable{
    private KafkaTemplate<String, String> kafkaTemplate;

    private Random random = new Random();
    private ObjectMapper mapper = new ObjectMapper();

    private static final List<String> products = Arrays.asList(
        "Pacote 1", "Pacote 2", "Internet 30", "Promo 34329",
        "Campanha Doação", "Plano 100", "Plano 300", "Plano Default"
    );

    private static final List<String> creditReasons = Arrays.asList(
            "Bonus", "Premio"
    );

    public AbstractPublisher(KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public abstract void run();

    public abstract String getTopic();



    protected <E extends Event> void  publish(E payload){
        try {
            String object = mapper.writeValueAsString(payload);
            kafkaTemplate.send(getTopic(), payload.getId(), object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    protected Integer getCustomerId(){
        return random.nextInt(20) + 1;
    }

    protected Integer getTransaction(){
        return random.ints(-1000, 1000).findFirst().getAsInt();
    }

    protected Integer getPositiveTransaction(){
        return random.nextInt(1000);
    }


    protected String getProduct(){
        return products.get(random.nextInt(8));
    }

    protected String getCreditReasons(){
        return creditReasons.get(random.nextInt(2));
    }

    protected String getDebitReasons(){
        return getProduct();
    }
}
