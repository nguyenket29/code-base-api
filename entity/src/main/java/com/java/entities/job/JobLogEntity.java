package com.java.entities.job;

import com.java.AbstractAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

/**
 * A JobLogs.
 */
@Entity
@Table(name = "job_logs")
public class JobLogEntity extends AbstractAuditingEntity {
    @Column(name = "merchant_id")
    private Long merchantId;

    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "log_time")
    private Instant logTime;

    @Column(name = "status_code")
    private String statusCode;

    @Column(name = "response_data")
    private String responseData;

    @Column(name = "error_message")
    private String errorMessage;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Instant getLogTime() {
        return logTime;
    }

    public void setLogTime(Instant logTime) {
        this.logTime = logTime;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
