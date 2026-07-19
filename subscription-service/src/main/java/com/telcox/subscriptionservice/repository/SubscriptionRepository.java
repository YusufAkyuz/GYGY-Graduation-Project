package com.telcox.subscriptionservice.repository;

import com.telcox.subscriptionservice.domain.Subscription;
import com.telcox.subscriptionservice.domain.SubscriptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    boolean existsByOrderId(UUID orderId);

    Optional<Subscription> findByOrderId(UUID orderId);

    Page<Subscription> findAllByCustomerId(UUID customerId, Pageable pageable);

    Page<Subscription> findAllByStatus(SubscriptionStatus status, Pageable pageable);
}
