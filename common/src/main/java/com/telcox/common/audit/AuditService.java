package com.telcox.common.audit;

public class AuditService {

    private final AuditLogEntryRepository repository;

    public AuditService(AuditLogEntryRepository repository) {
        this.repository = repository;
    }

    public void record(String entityName, String entityId, String action, String actorUserId, String details) {
        repository.save(new AuditLogEntry(entityName, entityId, action, actorUserId, details));
    }
}
