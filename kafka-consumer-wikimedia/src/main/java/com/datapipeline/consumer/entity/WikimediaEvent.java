package com.datapipeline.consumer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * NOTE: @Entity makes this a database table
 * WHY: JPA automatically creates table 'wikimedia_events'
 */
@Entity
@Table(name = "wikimedia_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WikimediaEvent {

    /**
     * NOTE: @Id marks primary key
     * @GeneratedValue with IDENTITY = auto-increment
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * NOTE: @Lob = Large Object for storing JSON
     * WHY: Wikimedia events are large JSON strings
     */
    @Lob
    @Column(name = "event_data", columnDefinition = "TEXT")
    private String eventData;

    /**
     * NOTE: Timestamp when event was stored in database
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * NOTE: @PrePersist runs automatically before saving
     * WHY: Sets timestamp without manual code
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Convenience constructor
     */
    public WikimediaEvent(String eventData) {
        this.eventData = eventData;
    }
}
