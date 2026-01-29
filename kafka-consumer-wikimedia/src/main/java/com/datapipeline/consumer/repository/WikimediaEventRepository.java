package com.datapipeline.consumer.repository;

import com.datapipeline.consumer.entity.WikimediaEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * NOTE: JpaRepository provides CRUD operations automatically
 * WHY: No need to write SQL - Spring generates it
 */
@Repository
public interface WikimediaEventRepository extends JpaRepository<WikimediaEvent, Long> {

    /**
     * NOTE: Method name becomes SQL query automatically
     * findByCreatedAtAfter → SELECT * FROM wikimedia_events WHERE created_at > ?
     */
    List<WikimediaEvent> findByCreatedAtAfter(LocalDateTime dateTime);

    /**
     * Count events after certain date
     */
    long countByCreatedAtAfter(LocalDateTime dateTime);
}
