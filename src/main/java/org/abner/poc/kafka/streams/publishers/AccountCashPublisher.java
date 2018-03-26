package org.abner.poc.kafka.streams.publishers;

import org.abner.poc.kafka.streams.utils.TopicConstants;
import org.springframework.kafka.core.KafkaTemplate;
import org.abner.poc.kafka.streams.events.AccountCashEvent;

public class AccountCashPublisher extends AbstractPublisher {


    public AccountCashPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        super(kafkaTemplate);
    }

    @Override
    public void run() {
        while (true) {
            int transaction = getTransaction();
            String reason = transaction < 0 ? getDebitReasons() : getCreditReasons();
            AccountCashEvent event = new AccountCashEvent(getCustomerId(), transaction, reason);
            System.out.println("Publishing account cash event. id:" + event.getId());
            publish(event);
            try {
                Thread.sleep(500l);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getTopic() {
        return TopicConstants.ACC_TOPIC;
    }
}
