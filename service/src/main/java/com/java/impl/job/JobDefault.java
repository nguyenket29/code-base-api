package com.java.impl.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobDefault extends JobBase {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Thực hiện công việc A tại thời điểm 8 giờ
        System.out.println("Thực hiện công việc A");
    }
}
