package org.abner.poc.kafka.streams.events;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class PaymentEvent implements Event{

    private String id;
    private Integer customerId;
    private Date created;
    private String type;
    private String product;
    private int price;

    public PaymentEvent(){}

    public PaymentEvent(Integer customerId, Integer price, String product){
        this.customerId = customerId;
        this.id = UUID.randomUUID().toString();
        this.price = price;
        this.type = "CREDIT_CARD";
        this.created = new Date();
        this.product = product;
    }

    @Override
    public String getId(){
        return this.id;
    }
}
