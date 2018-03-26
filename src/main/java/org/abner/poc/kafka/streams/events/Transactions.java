package org.abner.poc.kafka.streams.events;

import lombok.Data;
import lombok.Value;

import java.util.Date;

@Data
public class Transactions {
    private Integer customerId;
    private String id;
    private Date date;
    private String paymentMethod;
    private String description;
    private Integer price;
    private String operation;
}
