package com.lu.schoolproject.service.Imp;

import com.lu.schoolproject.entitys.DynamicJob;
import com.lu.schoolproject.service.QuartzJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuartzJobServiceImpl implements QuartzJobService {

    private final Scheduler scheduler;

    /**
     * 添加任务
     */
    public void addJob(String name, String group, String cron) throws SchedulerException {
        // 创建任务详情
        JobDetail jobDetail = JobBuilder.newJob(DynamicJob.class)
                .withIdentity(name, group)
                .build();

        // 创建触发器
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(name + "_trigger", group)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();

        // 调度任务
        scheduler.scheduleJob(jobDetail, trigger);
        log.info("任务添加成功: {}.{}", group, name);
    }

    /**
     * 删除任务
     */
    public void deleteJob(String name, String group) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(name, group);
        scheduler.deleteJob(jobKey);
        log.info("任务删除成功: {}.{}", group, name);
    }

    /**
     * 暂停任务
     */
    public void pauseJob(String name, String group) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(name, group);
        scheduler.pauseJob(jobKey);
        log.info("任务暂停成功: {}.{}", group, name);
    }

    /**
     * 恢复任务
     */
    public void resumeJob(String name, String group) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(name, group);
        scheduler.resumeJob(jobKey);
        log.info("任务恢复成功: {}.{}", group, name);
    }
}