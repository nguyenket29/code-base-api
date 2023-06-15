package com.java.schedule;

import com.java.entities.job.JobEntity;
import com.java.impl.job.JobDefault;
import com.java.impl.job.JobExample;
import com.java.job.JobRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@Slf4j
public class JobScheduler implements ApplicationContextAware {
    private ApplicationContext springContext;
    private final JobRepository jobRepository;

    public JobScheduler(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        springContext = applicationContext;
    }

    @PostConstruct
    public void scheduleJobs() throws ClassNotFoundException, SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
        jobFactory.setApplicationContext(springContext);
        scheduler.setJobFactory(jobFactory);
        // Xóa toàn bộ công việc hiện tại
        log.info("RESET SCHEDULE JOB");
        scheduler.clear();
        // Lấy danh sách công việc từ cơ sở dữ liệu
        List<JobEntity> jobInfos = jobRepository.findAll();
        log.info("Get All jobInfos : {}", jobInfos);
        if (jobInfos.size() > 0) {
            // Đăng ký và lên lịch cho mỗi công việc
            boolean ck = false;
            for (JobEntity jobInfo : jobInfos) {
                if (jobInfo.getEnabled().equals("true")) {
                    ck = true;
                    log.info("JOB EXISTS: {}", jobInfo);
                    JobDetail jobDetail = buildJobDetail(jobInfo);
                    Trigger trigger = buildTrigger(jobInfo);
                    scheduler.scheduleJob(jobDetail, trigger);
                }
            }
            if (ck) {
                scheduler.start();
            }
        }
    }

    // Phương thức để xây dựng JobDetail từ thông tin công việc
    private JobDetail buildJobDetail(JobEntity jobInfo) throws ClassNotFoundException {
        Class<? extends Job> jobClass;
        if (jobInfo.getApiEndpoint().equals("JobExample")) {
            jobClass = JobExample.class;
        } else {
            // Xử lý công việc khác (nếu cần)
            jobClass = JobDefault.class;
        }
        return JobBuilder.newJob(jobClass).withIdentity(jobInfo.getJobName(), String.valueOf(jobInfo.getId())).build();
    }

    // Phương thức để xây dựng Trigger từ thông tin công việc
    private Trigger buildTrigger(JobEntity jobInfo) {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(jobInfo.getJobName() + "_trigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(jobInfo.getCronExpression()))
                .build();
    }
}
