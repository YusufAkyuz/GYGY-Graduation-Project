package com.telcox.customerservice.repository;

import com.telcox.customerservice.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    boolean existsByTcknHash(String tcknHash);

    boolean existsByEmail(String email);

    Optional<Customer> findByIdAndDeletedFalse(UUID id);

    Page<Customer> findAllByDeletedFalse(Pageable pageable);
}
