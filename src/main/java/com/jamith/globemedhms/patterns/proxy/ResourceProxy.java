package com.jamith.globemedhms.patterns.proxy;

import com.jamith.globemedhms.core.entities.AuditLog;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.infrastructure.repository.AuditLogRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

public class ResourceProxy {
    private static final Logger logger = LogManager.getLogger(ResourceProxy.class);
    private final AuditLogRepository auditLogRepository = new AuditLogRepository();

    public void accessResource(Staff staff, String resource, String permissionRequired) {
        if (staff.hasPermission(permissionRequired)) {
            logger.info("Access granted to {} for staff {}", resource, staff.getUsername());
            auditLogRepository.save(new AuditLog(staff.getId(), "Accessed " + resource, LocalDateTime.now()));
        } else {
            logger.warn("Access denied to {} for staff {}", resource, staff.getUsername());
            auditLogRepository.save(new AuditLog(staff.getId(), "Denied access to " + resource, LocalDateTime.now()));
            throw new SecurityException("Access denied: Insufficient permissions");
        }
    }
}