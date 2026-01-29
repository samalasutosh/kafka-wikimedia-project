package com.datapipeline.consumer.service;

import com.datapipeline.consumer.entity.WikimediaEvent;
import com.datapipeline.consumer.repository.WikimediaEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * NOTE: This service consumes messages from Kafka
 * WHY: @KafkaListener automatically polls Kafka and calls method for each message
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WikimediaConsumerService {

    private final WikimediaEventRepository repository;

    /**
     * NOTE: @KafkaListener is the heart of Kafka consumer
     * - Automatically subscribes to topic
     * - Polls for new messages
     * - Calls this method for each message
     *
     * WHY: Declarative approach - no manual polling needed
     */
    @KafkaListener(
            topics = "${wikimedia.kafka.topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(String eventData) {
        try {
            log.info("Consumed event: {} bytes", eventData.length());

            // NOTE: Create entity and save to PostgreSQL
            WikimediaEvent event = new WikimediaEvent(eventData);
            WikimediaEvent savedEvent = repository.save(event);

            log.info("Saved event to database with ID: {}", savedEvent.getId());

        } catch (Exception e) {
            log.error("Failed to process message: {}", e.getMessage(), e);
        }
    }
}
