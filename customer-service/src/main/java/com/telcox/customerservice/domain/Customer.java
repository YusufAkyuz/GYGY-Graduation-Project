package com.telcox.customerservice.domain;

import com.telcox.common.security.pii.EncryptedStringConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * MVP kapsamı bireysel müşteridir (Bölüm 5: Scope In). TCKN alanı AES-GCM ile şifrelenir
 * (SR-05); tcknHash ise deterministik uniqueness/arama için ayrı tutulur.
 */
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "tckn_encrypted", nullable = false, length = 512)
    private String tckn;

    @Column(name = "tckn_hash", nullable = false, unique = true, length = 64)
    private String tcknHash;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status = CustomerStatus.PENDING;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<IdentityDocument> documents = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted = false;

    private Instant deletedAt;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    private Instant updatedAt = Instant.now();

    @Version
    private long version;

    protected Customer() {
    }

    public Customer(String firstName, String lastName, String tckn, String tcknHash, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.tckn = tckn;
        this.tcknHash = tcknHash;
        this.email = email;
        this.phone = phone;
    }

    public void addAddress(Address address) {
        address.assignTo(this);
        this.addresses.add(address);
    }

    public void addDocument(IdentityDocument document) {
        document.assignTo(this);
        this.documents.add(document);
    }

    public void approveKyc() {
        this.status = CustomerStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public void rejectKyc(String reason) {
        this.status = CustomerStatus.REJECTED;
        this.rejectionReason = reason;
        this.updatedAt = Instant.now();
    }

    public void softDelete() {
        this.deleted = true;
        this.deletedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTckn() {
        return tckn;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public List<IdentityDocument> getDocuments() {
        return documents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
