package com.java.impl.job;

import com.java.entities.job.JobLogEntity;
import com.java.job.JobLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.time.Instant;

@Slf4j
public class JobExample extends JobBase {
    private final JobLogRepository jobLogRepository;

    public JobExample(JobLogRepository jobLogRepository) {
        this.jobLogRepository = jobLogRepository;
    }

    @Override
    public void execute(JobExecutionContext context) {
        log.info("START B {}", context.getMergedJobDataMap());
        // Thực hiện công việc B tại thời điểm
        JobDataMap map = context.getMergedJobDataMap();

        // Code to run the job
        log.info("Start Scheduled Job Send JobPeriodicStatement {}", map);
        String jobName = context.getJobDetail().getKey().getName();
        String jobGroup = context.getJobDetail().getKey().getGroup();
        log.info("=>>>>>>>>>>>>>>>>>>>>>>>JOb Name : {}", jobName);
        log.info("=>>>>>>>>>>>>>>>>>>>>>>>JOb Group : {}", jobGroup);
        sendJob(jobName, jobGroup);
        log.info("Perform the work JobSendPeriodicStatement");
    }

    private void sendJob(String jobName, String jobId) {
        JobLogEntity jobLog = new JobLogEntity();
        jobLog.setJobId(Long.valueOf(jobId));
        jobLog.setLogTime(Instant.now());
        jobLogRepository.save(jobLog);
        // TODO: 15/6/2023 - Logic Run Job
    }
}
