package com.datapipeline.producer.service;

import com.datapipeline.producer.handler.WikimediaEventHandler;
import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import com.launchdarkly.eventsource.background.BackgroundEventSource;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * NOTE: Service that manages the Wikimedia stream connection
 * WHY: Automatically starts when Spring Boot application starts
 */
@Service
@Slf4j
public class WikimediaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String wikimediaUrl;
    private final String topic;
    private BackgroundEventSource eventSource;

    public WikimediaProducerService(
            KafkaTemplate<String, String> kafkaTemplate,
            @Value("${wikimedia.stream.url}") String wikimediaUrl,
            @Value("${wikimedia.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.wikimediaUrl = wikimediaUrl;
        this.topic = topic;
    }

    /**
     * NOTE: @PostConstruct runs automatically after Spring creates this bean
     * WHY: Starts streaming immediately when application starts
     */
    @PostConstruct
    public void startProducer() {
        log.info("Starting Wikimedia event stream producer...");

        try {
            BackgroundEventHandler eventHandler = new WikimediaEventHandler(kafkaTemplate, topic);

            EventSource.Builder builder = new EventSource.Builder(
                    URI.create(wikimediaUrl)
            );

            eventSource = new BackgroundEventSource.Builder(eventHandler, builder).build();

            // NOTE: Start the stream (non-blocking)
            eventSource.start();

            log.info("Wikimedia producer started successfully on topic: {}", topic);

        } catch (Exception e) {
            log.error("Failed to start Wikimedia producer: {}", e.getMessage(), e);
        }
    }

    /**
     * NOTE: @PreDestroy runs when application shuts down
     * WHY: Ensures graceful shutdown, no data loss
     */
    @PreDestroy
    public void stopProducer() {
        if (eventSource != null) {
            try {
                log.info("Shutting down Wikimedia producer...");
                eventSource.close();
                TimeUnit.SECONDS.sleep(2);
                log.info("Wikimedia producer stopped successfully");
            } catch (Exception e) {
                log.error("Error stopping producer: {}", e.getMessage());
            }
        }
    }
}
