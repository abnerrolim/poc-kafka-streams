package org.abner.poc.kafka.streams.events;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Event {

    @JsonIgnore
    String getId();
}
