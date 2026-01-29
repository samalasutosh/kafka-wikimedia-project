package com.datapipeline.producer.handler;

import com.launchdarkly.eventsource.MessageEvent;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * NOTE: This handler receives real-time events from Wikimedia stream
 * WHY: Bridges Wikimedia SSE stream to Kafka
 */
@Slf4j
public class WikimediaEventHandler implements BackgroundEventHandler {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public WikimediaEventHandler(KafkaTemplate<String, String> kafkaTemplate, String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    /**
     * NOTE: Called automatically when Wikimedia sends an event
     * WHY: This is where we receive data and send it to Kafka
     */
    @Override
    public void onMessage(String event, MessageEvent messageEvent) {
        String data = messageEvent.getData();

        log.info("Received event from Wikimedia: {} bytes", data.length());

        // NOTE: Send to Kafka asynchronously
        // WHY: Non-blocking - can handle thousands of messages per second
        kafkaTemplate.send(topic, data);
    }

    @Override
    public void onOpen() {
        log.info("Wikimedia stream connection opened");
    }

    @Override
    public void onClosed() {
        log.warn("Wikimedia stream connection closed");
    }

    @Override
    public void onError(Throwable t) {
        log.error("Error in Wikimedia stream: {}", t.getMessage());
    }

    @Override
    public void onComment(String comment) {
        // Keep-alive comments - ignore
    }
}
