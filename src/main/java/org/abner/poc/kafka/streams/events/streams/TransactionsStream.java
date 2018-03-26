package org.abner.poc.kafka.streams.events.streams;

import org.abner.poc.kafka.streams.events.Transactions;
import org.abner.poc.kafka.streams.events.mappers.Mapper;
import org.abner.poc.kafka.streams.events.mappers.TransactionResumeSerde;
import org.abner.poc.kafka.streams.utils.TopicConstants;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.abner.poc.kafka.streams.events.TransactionResume;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Component
public class TransactionsStream {

    private KafkaStreams stream;
    private final String bootstrapServer;
    private String queryStoreName;

    public TransactionsStream(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServer){
        this.bootstrapServer = bootstrapServer;
    }

    @PostConstruct
    public void runStream() {

        Properties config = new Properties();
        final String version = "0.2";

        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "transactions-stream" + version);
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass().getName());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, TransactionResumeSerde.class.getName());
        config.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, Transactions> accStream = streamsBuilder.stream(TopicConstants.ACC_TOPIC, Consumed.with(Serdes.String(), Serdes.String()))
                .mapValues(Mapper::accountCashEventFrom)
                .mapValues( Mapper::from);

        KStream<String, Transactions> transactionsKStream =  streamsBuilder.stream(TopicConstants.PAY_TOPIC, Consumed.with(Serdes.String(), Serdes.String()))
                .mapValues(Mapper::paymentEventFrom)
                .mapValues(Mapper::from)
                .merge(accStream);

        String path = Paths.get("").toAbsolutePath().toString();

        transactionsKStream.print(Printed.toFile(path + "/transactions.log"));


        KTable<Windowed<Integer>, TransactionResume> aggregationStream = transactionsKStream
                .map((key, transactions) -> KeyValue.pair(transactions.getCustomerId(), Mapper.from(transactions)))
                .groupByKey()
                .windowedBy(TimeWindows.of(TimeUnit.MINUTES.toMillis(5)))
                .reduce(TransactionResume::agg);

        aggregationStream.toStream().print(Printed.toFile(path + "/aggregations.log"));

        queryStoreName = aggregationStream.queryableStoreName();


        stream = new KafkaStreams(streamsBuilder.build(), config);
        stream.start();
    }

    public String getQueryStoreName(){
        return this.queryStoreName;
    }

    public KafkaStreams getKafkaStreams(){
        return this.stream;
    }

    @PreDestroy
    public void closeStream() {
        stream.close();
    }

}
