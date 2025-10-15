package com.lu.schoolproject.controller;

import com.lu.schoolproject.service.Imp.QuartzJobServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class QuartzJobController {

    @Autowired
    private QuartzJobServiceImpl quartzJobService;

    @PostMapping("/add")
    public String addJob(@RequestParam String name,
                         @RequestParam String group,
                         @RequestParam String cron) {
        try {
            quartzJobService.addJob(name, group, cron);
            return "任务添加成功";
        } catch (Exception e) {
            return "添加任务失败：" + e.getMessage();
        }
    }

    @DeleteMapping("/delete")
    public String deleteJob(@RequestParam String name,
                            @RequestParam String group) {
        try {
            quartzJobService.deleteJob(name, group);
            return "任务删除成功";
        } catch (Exception e) {
            return "删除任务失败：" + e.getMessage();
        }
    }

    @PostMapping("/pause")
    public String pauseJob(@RequestParam String name,
                           @RequestParam String group) {
        try {
            quartzJobService.pauseJob(name, group);
            return "任务暂停成功";
        } catch (Exception e) {
            return "暂停任务失败：" + e.getMessage();
        }
    }

    @PostMapping("/resume")
    public String resumeJob(@RequestParam String name,
                            @RequestParam String group) {
        try {
            quartzJobService.resumeJob(name, group);
            return "任务恢复成功";
        } catch (Exception e) {
            return "恢复任务失败：" + e.getMessage();
        }
    }
}
