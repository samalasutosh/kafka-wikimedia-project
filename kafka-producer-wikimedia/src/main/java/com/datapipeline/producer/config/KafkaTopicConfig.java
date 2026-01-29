package com.datapipeline.producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * NOTE: This class creates a Kafka topic programmatically
 * WHY: Ensures topic exists before producer sends messages
 */
@Configuration
public class KafkaTopicConfig {

    @Value("${wikimedia.kafka.topic}")
    private String topicName;

    /**
     * NOTE: @Bean tells Spring to create and manage this topic
     * Spring Kafka will automatically create this topic in Kafka when app starts
     */
    @Bean
    public NewTopic wikimediaTopic() {
        return TopicBuilder.name(topicName)
                // NOTE: 3 partitions allow 3 consumers to read in parallel
                .partitions(3)
                // NOTE: Replication factor 1 (we only have 1 broker)
                .replicas(1)
                .build();
    }
}
