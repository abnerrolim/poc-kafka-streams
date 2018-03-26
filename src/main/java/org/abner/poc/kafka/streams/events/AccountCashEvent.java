package org.abner.poc.kafka.streams.events;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class AccountCashEvent implements Event {

    private Integer customerId;
    private String id;
    private Date created;
    private String type;
    private String reason;
    private Integer price;

    public AccountCashEvent(){}

    public AccountCashEvent(Integer customerId, Integer price, String reason) {
        this.customerId = customerId;
        this.id = UUID.randomUUID().toString();
        this.price = price;
        this.type = "MONEY";
        this.reason = reason;
        created = new Date();
    }

    @Override
    public String getId() {
        return this.id;
    }

}
