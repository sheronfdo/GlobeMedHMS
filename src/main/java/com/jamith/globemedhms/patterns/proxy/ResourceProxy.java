package com.jamith.globemedhms.patterns.proxy;

import com.jamith.globemedhms.core.entities.AuditLog;
import com.jamith.globemedhms.core.entities.Staff;
import com.jamith.globemedhms.infrastructure.repository.AuditLogRepository;
import com.jamith.globemedhms.patterns.mediator.ResourceAccessMediator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

public class ResourceProxy {
    private static final Logger logger = LogManager.getLogger(ResourceProxy.class);
    private final ResourceAccessMediator mediator = new ResourceAccessMediator();

    public void accessResource(Staff staff, String resource, String permissionRequired) {
        logger.info("ResourceProxy delegating access request for {} by {}", resource, staff.getUsername());
        mediator.accessResource(staff, resource, permissionRequired);
    }
}