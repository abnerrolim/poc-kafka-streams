package org.abner.poc.kafka.streams.events;

import lombok.Data;

@Data
public class TransactionResume {
    private Integer customerId;
    private Integer price;

    public static TransactionResume of(Integer customerId, Integer price){
        TransactionResume t = new TransactionResume();
        t.setPrice(price);
        t.setCustomerId(customerId);
        return t;
    }

    public static TransactionResume agg(final TransactionResume t1, final TransactionResume t2){
        if( !t1.getCustomerId().equals(t2.getCustomerId()) )
            throw new IllegalArgumentException("Only same customers can be aggregate");

        int price = t1.getPrice() + t2.getPrice();
        TransactionResume t = new TransactionResume();
        t.setPrice(price);
        t.setCustomerId(t1.getCustomerId());
        return t;
    }
}
