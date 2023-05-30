package com.java.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.AbstractAuditingEntity;
import com.java.repository.EntityAuditEventRepository;
import com.java.entities.EntityAuditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Async Entity Audit Event writer
 * This is invoked by Hibernate entity listeners to write audit event for entities
 */
@Component
public class AsyncEntityAuditEventWriter {

    private final Logger log = LoggerFactory.getLogger(AsyncEntityAuditEventWriter.class);

    private final EntityAuditEventRepository auditEventRepository;

    private final ObjectMapper objectMapper;

    public AsyncEntityAuditEventWriter(EntityAuditEventRepository auditEventRepository, ObjectMapper objectMapper) {
        this.auditEventRepository = auditEventRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Writes audit events to DB asynchronously in a new thread
     */
    @Async
    public void writeAuditEvent(Object target, EntityAuditAction action) {
        log.debug("-------------- Post {} audit  --------------", action.value());
        try {
            EntityAuditEvent auditedEntity = prepareAuditEntity(target, action);
            if (auditedEntity != null) {
                auditEventRepository.save(auditedEntity);
            }
        } catch (Exception e) {
            log.error("Exception while persisting audit entity for {} error: {}", target, e);
        }
    }

    /**
     * Method to prepare auditing entity
     */
    private EntityAuditEvent prepareAuditEntity(final Object entity, EntityAuditAction action) {
        EntityAuditEvent auditedEntity = new EntityAuditEvent();
        Class<?> entityClass = entity.getClass(); // Retrieve entity class with reflection
        auditedEntity.setAction(action.value());
        auditedEntity.setEntityType(getEntityName(entityClass.getName()));
        auditedEntity.setEntityLabel(getEntityName(entityClass.getName()));
        String entityId;
        String entityData;
        log.trace("Getting Entity Id and Content");
        try {
            String idFieldName = getClassName(entityClass.getSimpleName());
            Field privateLongField = entityClass.getDeclaredField(idFieldName);
            privateLongField.setAccessible(true);
            entityId = (String) privateLongField.get(entity);
            privateLongField.setAccessible(false);
            entityData = objectMapper.writeValueAsString(entity);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException |
                IOException e) {
            log.error("Exception while getting entity ID and content {}", e.getMessage());
            // returning null as we don't want to raise an application exception here
            return null;
        }
        auditedEntity.setEntityId(entityId);
        auditedEntity.setEntityValue(entityData);
        final AbstractAuditingEntity abstractAuditEntity = (AbstractAuditingEntity) entity;
        if (EntityAuditAction.CREATE.equals(action)) {
            auditedEntity.setModifiedBy(abstractAuditEntity.getCreatedBy());
            auditedEntity.setModifiedDate(abstractAuditEntity.getCreatedDate());
            auditedEntity.setCommitVersion(1);
        } else {
            auditedEntity.setModifiedBy(abstractAuditEntity.getLastModifiedBy());
            auditedEntity.setModifiedDate(abstractAuditEntity.getLastModifiedDate());
            calculateVersion(auditedEntity);
        }
        log.trace("Audit Entity --> {} ", auditedEntity);
        return auditedEntity;
    }

    private void calculateVersion(EntityAuditEvent auditedEntity) {
        log.trace("Version calculation. for update/remove");
        Integer lastCommitVersion = auditEventRepository
                .findMaxCommitVersion(auditedEntity.getEntityType(), auditedEntity.getEntityId());

        log.trace("Last commit version of entity => {}", lastCommitVersion);
        if (lastCommitVersion != null && lastCommitVersion != 0) {
            log.trace("Present. Adding version..");
            auditedEntity.setCommitVersion(lastCommitVersion + 1);
        } else {
            log.trace("No entities.. Adding new version 1");
            auditedEntity.setCommitVersion(1);
        }
    }

    private String getClassName(String name) {
        String idFieldName = name + "Id";
        String first = idFieldName.substring(0, 1).toLowerCase();
        return first + idFieldName.substring(1);
    }

    private static String getEntityName(String name) {
        return "java" + name.split("java")[1];
    }

}
