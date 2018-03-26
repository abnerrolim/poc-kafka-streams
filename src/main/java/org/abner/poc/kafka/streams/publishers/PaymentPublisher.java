package org.abner.poc.kafka.streams.publishers;

import org.abner.poc.kafka.streams.events.PaymentEvent;
import org.abner.poc.kafka.streams.utils.TopicConstants;
import org.springframework.kafka.core.KafkaTemplate;

public class PaymentPublisher extends AbstractPublisher {


    public PaymentPublisher(KafkaTemplate<String, String> kafkaTemplate){
        super(kafkaTemplate);
    }

    @Override
    public void run(){
        while(true){
            PaymentEvent event = new PaymentEvent(getCustomerId(), getPositiveTransaction(), getProduct());
            System.out.println("Publishing account cash event. id:" + event.getId());
            publish(event);
            try {
                Thread.sleep(500l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getTopic(){
        return TopicConstants.PAY_TOPIC;
    }
}
