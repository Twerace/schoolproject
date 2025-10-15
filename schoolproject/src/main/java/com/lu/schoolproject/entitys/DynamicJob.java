package com.lu.schoolproject.entitys;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DynamicJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String jobName = context.getJobDetail().getKey().getName();
        System.out.println("动态任务执行：" + jobName + " - " + System.currentTimeMillis());
    }
}
