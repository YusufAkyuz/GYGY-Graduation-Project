package com.telcox.common.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    @Query("select e from OutboxEvent e where e.processedAt is null order by e.createdAt asc")
    List<OutboxEvent> findUnprocessedBatch(org.springframework.data.domain.Pageable pageable);
}
