package com.telcox.usageservice.repository;

import com.telcox.usageservice.domain.UsageBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsageBalanceRepository extends JpaRepository<UsageBalance, UUID> {
}
