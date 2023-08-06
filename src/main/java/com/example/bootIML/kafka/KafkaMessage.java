package com.example.bootIML.kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KafkaMessage {
    private final Integer id;

    private final String message;

    @JsonCreator
    public KafkaMessage(@JsonProperty("id") Integer id,
                         @JsonProperty("message") String message) {
        this.id = id;
        this.message = message;
    }

    public Integer getId() {
        return this.id;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return "KafkaMessage{id=" + this.id + ", message='" + this.message + "'}";
    }

}
