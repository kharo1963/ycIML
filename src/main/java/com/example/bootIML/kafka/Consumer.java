package com.example.bootIML.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Consumer {

    private final KafkaListenerEndpointRegistry registry;

    //@KafkaListener(topics = "${cloudkarafka.topic}", id = "ycContainer", autoStartup = "false")
    @KafkaListener(topics = "${cloudkarafka.topic}", id = "iznfjgtx-consumers", autoStartup = "false")
    public void processMessage(String message,
                               @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                               @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics,
                               @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        MessageListenerContainer listenerContainer = registry.getListenerContainer("iznfjgtx-consumers");
        log.info(topics.get(0) + " " + partitions.get(0) + " " + offsets.get(0) + " " + message);
        listenerContainer.stop();
    }

    public void startMessage () {
        registry.getListenerContainer("iznfjgtx-consumers").start();
    }

}
