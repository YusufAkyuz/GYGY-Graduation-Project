package com.telcox.subscriptionservice.repository;

import com.telcox.subscriptionservice.domain.MsisdnPoolEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MsisdnPoolRepository extends JpaRepository<MsisdnPoolEntry, String> {

    /**
     * FOR UPDATE SKIP LOCKED: eşzamanlı aktivasyonlarda aynı MSISDN'in iki kez tahsis
     * edilmesini engeller (her transaction farklı bir satırı kilitler).
     */
    @Query(value = "SELECT msisdn FROM msisdn_pool WHERE allocated = false ORDER BY msisdn LIMIT 1 FOR UPDATE SKIP LOCKED",
            nativeQuery = true)
    Optional<String> lockNextAvailable();

    @Modifying
    @Query("UPDATE MsisdnPoolEntry m SET m.allocated = true WHERE m.msisdn = :msisdn")
    void markAllocated(String msisdn);
}
