package com.java.job;

import com.java.entities.job.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EntityAuditEvent entity.
 */
@Repository
public interface JobRepository extends JpaRepository<JobEntity, String> {
}
