package com.telcox.subscriptionservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "msisdn_pool")
public class MsisdnPoolEntry {

    @Id
    private String msisdn;

    @Column(nullable = false)
    private boolean allocated = false;

    protected MsisdnPoolEntry() {
    }

    public MsisdnPoolEntry(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public boolean isAllocated() {
        return allocated;
    }

    public void allocate() {
        this.allocated = true;
    }
}
