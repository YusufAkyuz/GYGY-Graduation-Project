package com.telcox.paymentservice.repository;

import com.telcox.paymentservice.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    boolean existsByOrderId(UUID orderId);

    Optional<Payment> findByOrderId(UUID orderId);
}
