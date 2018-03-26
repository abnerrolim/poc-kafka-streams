package org.abner.poc.kafka.streams.controller;

import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.apache.kafka.streams.state.WindowStoreIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.abner.poc.kafka.streams.events.TransactionResume;
import org.abner.poc.kafka.streams.events.streams.TransactionsStream;

@RestController
public class QueryAgg {

    @Autowired
    private TransactionsStream stream;


    @GetMapping("/agg/{customerId}")
    public WindowStoreIterator<TransactionResume> aggByCustomer(@PathVariable("customerId") Integer customerId) {
        ReadOnlyWindowStore<Integer, TransactionResume> store= stream.getKafkaStreams().store(stream.getQueryStoreName(), QueryableStoreTypes.windowStore());
        long fromBeginningOfTimeMs = 0;
        long toNowInProcessingTimeMs = System.currentTimeMillis();
        return store.fetch(customerId, fromBeginningOfTimeMs, toNowInProcessingTimeMs);
    }
}
