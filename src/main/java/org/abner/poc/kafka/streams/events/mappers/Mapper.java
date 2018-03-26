package org.abner.poc.kafka.streams.events.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.abner.poc.kafka.streams.events.Transactions;
import org.abner.poc.kafka.streams.events.AccountCashEvent;
import org.abner.poc.kafka.streams.events.PaymentEvent;
import org.abner.poc.kafka.streams.events.TransactionResume;

import java.io.IOException;

public class Mapper {

    private Mapper(){}
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static Transactions from(AccountCashEvent accountCashEvent){
        String operation = accountCashEvent.getPrice() < 0 ? "DEBIT" : "CREDIT";
        Transactions transactions = new Transactions();
        transactions.setCustomerId(accountCashEvent.getCustomerId());
        transactions.setId(accountCashEvent.getId());
        transactions.setPrice(accountCashEvent.getPrice());
        transactions.setDate(accountCashEvent.getCreated());
        transactions.setDescription(accountCashEvent.getReason());
        transactions.setOperation(operation);
        transactions.setPaymentMethod(accountCashEvent.getType());
        return transactions;
    }

    public static Transactions from(PaymentEvent paymentEvent){
        Transactions transactions = new Transactions();
        transactions.setCustomerId(paymentEvent.getCustomerId());
        transactions.setId(paymentEvent.getId());
        transactions.setPrice(paymentEvent.getPrice());
        transactions.setDate(paymentEvent.getCreated());
        transactions.setDescription(paymentEvent.getProduct());
        transactions.setOperation("CREDIT");
        transactions.setPaymentMethod(paymentEvent.getType());
        return transactions;
    }

    public static TransactionResume from(Transactions transactions){
        TransactionResume transactionResume = new TransactionResume();
        transactionResume.setCustomerId(transactions.getCustomerId());
        transactionResume.setPrice(transactions.getPrice());
        return transactionResume;
    }

    public static AccountCashEvent accountCashEventFrom(String jsonString){
        try {
            return MAPPER.readValue(jsonString, AccountCashEvent.class);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static PaymentEvent paymentEventFrom(String jsonString){
        try {
            return MAPPER.readValue(jsonString, PaymentEvent.class);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
