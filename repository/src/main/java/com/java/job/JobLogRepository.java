package com.java.job;

import com.java.entities.job.JobLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EntityAuditEvent entity.
 */
@Repository
public interface JobLogRepository extends JpaRepository<JobLogEntity, String> {
}
