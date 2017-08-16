package com.schedule.controller;

import com.alibaba.fastjson.JSON;
import com.schedule.annotation.Authorization;
import com.schedule.conste.JobStatusEnum;
import com.schedule.model.JobHistory;
import com.schedule.model.JobSchedule;
import com.schedule.model.User;
import com.schedule.param.JobHistoryParam;
import com.schedule.param.JobScheduleParam;
import com.schedule.quartz.job.AbstractJobBean;
import com.schedule.quartz.job.QuartzJobFactory;
import com.schedule.service.JobHistoryService;
import com.schedule.service.JobScheduleService;
import com.schedule.utils.ApplicationContextHelper;
import com.schedule.utils.IpUtil;
import com.schedule.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("job")
@Authorization
public class JobScheduleController {
    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    JobScheduleService jobScheduleService;

    @Autowired
    JobHistoryService jobHistoryService;

    @Autowired
    private Scheduler scheduler;

    // 任务列表
    @RequestMapping("list")
    public String list(User user,
                       JobScheduleParam param,
                       @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                       @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
                       HttpServletRequest request,
                       HttpServletResponse response, Model model) {
        page = (page == null || page <= 0) ? 1 : page;
        limit = (limit == null || limit <= 0) ? 20 : limit;

        int start = (page - 1) * limit;
        List<JobSchedule> list = jobScheduleService.search(param, start, limit);
        int count = jobScheduleService.count(param);

        // 从quartz中获取下一次执行的时间。 此处认为quartz库和业务库是同一个。
        for (JobSchedule jobSchedule : list) {
            JdbcTemplate jdbcTemplate = ApplicationContextHelper.getBean(JdbcTemplate.class);

            List<Long> fireTime = jdbcTemplate.queryForList("select NEXT_FIRE_TIME from qrtz_triggers where JOB_NAME=? and JOB_GROUP=?",
                    Long.class, jobSchedule.getJobName(), jobSchedule.getJobGroup());
            if (fireTime.size() == 1) {
                jobSchedule.setNextFireTime(new Date(fireTime.get(0)));
            }
        }

        model.addAttribute("list", list);
        model.addAttribute("count", count);

        model.addAttribute("param", param);
        model.addAttribute("page", page);
        model.addAttribute("limit", limit);
        model.addAttribute("start", start);

        int totalPage = (count + limit - 1) / limit;
        model.addAttribute("totalPage", totalPage);

        return "job/list";
    }

    // 任务执行历史列表
    @RequestMapping("history/list")
    public String list_history(User user,
                               JobHistoryParam param,
                               @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                               @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
                               HttpServletRequest request,
                               HttpServletResponse response, Model model) {
        page = (page == null || page <= 0) ? 1 : page;
        limit = (limit == null || limit <= 0) ? 20 : limit;

        int start = (page - 1) * limit;
        List<JobHistory> list = jobHistoryService.search(param, start, limit);
        int count = jobHistoryService.count(param);

        model.addAttribute("list", list);
        model.addAttribute("count", count);

        model.addAttribute("param", param);
        model.addAttribute("page", page);
        model.addAttribute("limit", limit);
        model.addAttribute("start", start);

        int totalPage = (count + limit - 1) / limit;
        model.addAttribute("totalPage", totalPage);

        return "job/history/list";
    }

    // view
    @RequestMapping("view")
    public String view(
            @RequestParam("id") long id,
            HttpServletRequest request,
            HttpServletResponse response, Model model) {
        if (id > 0) {
            JobSchedule job = jobScheduleService.getJobById(id);
            model.addAttribute("job", job);
        }
        model.addAttribute("id", id);
        return "job/view";
    }

    // 任务表达式详解
    @RequestMapping("cron_expression")
    public String cron_expression(
            HttpServletRequest request,
            HttpServletResponse response, Model model,
            @RequestParam(value = "cronExpression", required = false) String cronExpression) {
        String parsed = null;

        if (StringUtils.isNotBlank(cronExpression)) {
            try {
                CronExpression cron = new CronExpression(cronExpression);
                parsed = cron.getExpressionSummary();
            } catch (ParseException e) {
                parsed = "任务表达式解析失败！";
            }
        }

        model.addAttribute("scheduleTime", cronExpression);
        model.addAttribute("parsed", parsed);
        return "job/cron_expression";
    }

    // 保存任务，
    // 编辑时候不修改状态，状态单独修改。 添加时候 状态默认为已调度
    @RequestMapping("save")
    @ResponseBody
    public String save(User admin,
                       HttpServletRequest request,
                       HttpServletResponse response,
                       @RequestParam("id") Long id,
                       @RequestParam("jobName") String jobName,
                       @RequestParam("jobGroup") String jobGroup,
                       @RequestParam("description") String description,
                       @RequestParam("arguments") String arguments,
                       @RequestParam("scheduleTime") String cronExpression
    ) {
        id = id == null ? 0 : id;
        try {
            Assert.hasLength(jobName, "请输入任务名");
            Assert.hasLength(jobGroup, "请输入任务组");
            Assert.hasLength(description, "请输入任务描述");
            Assert.hasLength(cronExpression, "请输入任务表达式");

            new CronExpression(cronExpression); // 解析任务表达式
        } catch (ParseException e) {
            return JsonUtil.getJson(1, cronExpression + " 任务表达式解析失败！").toString();
        } catch (Exception e) {
            return JsonUtil.getJson(1, e.getMessage()).toString();
        }

        AbstractJobBean abstractJobBean = (AbstractJobBean) ApplicationContextHelper.getBean(jobName);
        if (abstractJobBean == null) {
            return JsonUtil.getJson(1, "系统中找不到对应的任务，无法添加。").toString();
        }

        JobSchedule job = new JobSchedule();
        if (id > 0) {
            job = jobScheduleService.getJobById(id);
            if (job == null) {
                return JsonUtil.getJson(1, "任务不存在").toString();
            }
        }

        job.setJobName(jobName);
        job.setJobGroup(jobGroup);
        job.setDescription(description);
        job.setArguments(arguments);

        job.setScheduleTime(cronExpression);

        job.setUptime(new Date());
        job.setUpdateUserId(admin.getId());
        job.setUpdateUserName(admin.getRealname());


        try {
            if (id > 0) {
                updateScheduleJob(job);

                jobScheduleService.updateJobById(job);
            } else {
                addScheduleJob(job);

                job.setStatus(JobStatusEnum.RUN.getValue());
                job.setAddtime(new Date());
                jobScheduleService.addJob(job);

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return JsonUtil.getJson(1, e.getMessage()).toString();
        }

        return JsonUtil.getOkJsonResultString(job);
    }

    // 删除任务
    @RequestMapping("delete")
    @ResponseBody
    public String delete(User admin,
                         HttpServletRequest request,
                         HttpServletResponse response,
                         @RequestParam("id") long id
    ) {
        JobSchedule job = jobScheduleService.getJobById(id);
        if (job == null) {
            return JsonUtil.getJson(1, "任务不存在").toString();
        }

        try {
            deleteScheduleJob(job);

            logger.info(admin.getRealname() + "删除任务,job=" + JSON.toJSONString(job));
            jobScheduleService.deleteJobById(id);

            return JsonUtil.getOkJson().toString();
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return JsonUtil.getJson(1, e.getMessage()).toString();
        }
    }


    // 暂定任务
    @RequestMapping("pause")
    @ResponseBody
    public String pause(User admin,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestParam("id") long id
    ) {
        JobSchedule job = jobScheduleService.getJobById(id);
        if (job == null) {
            return JsonUtil.getJson(1, "任务不存在").toString();
        }

        if (job.getStatus() == JobStatusEnum.HANGUP.getValue()) {
            return JsonUtil.getJson(1, "不可重复暂停任务").toString();
        }

        try {
            pauseScheduleJob(job);

            logger.info(admin.getRealname() + "暂定任务,job=" + JSON.toJSONString(job));

            job.setStatus(JobStatusEnum.HANGUP.getValue());
            jobScheduleService.updateJobById(job);

            return JsonUtil.getOkJson().toString();
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return JsonUtil.getJson(1, e.getMessage()).toString();
        }
    }


    // 恢复任务
    @RequestMapping("resume")
    @ResponseBody
    public String resume(User admin,
                         HttpServletRequest request,
                         HttpServletResponse response,
                         @RequestParam("id") long id
    ) {
        JobSchedule job = jobScheduleService.getJobById(id);
        if (job == null) {
            return JsonUtil.getJson(1, "任务不存在").toString();
        }

        if (job.getStatus() == JobStatusEnum.RUN.getValue()) {
            return JsonUtil.getJson(1, "不可重复恢复任务").toString();
        }

        try {
            resumeScheduleJob(job);

            logger.info(admin.getRealname() + "恢复任务,job=" + JSON.toJSONString(job));
            job.setStatus(JobStatusEnum.RUN.getValue());
            jobScheduleService.updateJobById(job);

            return JsonUtil.getOkJson().toString();
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return JsonUtil.getJson(1, e.getMessage()).toString();
        }
    }


    // 执行一次
    @RequestMapping("runOneTime")
    @ResponseBody
    public String runOneTime(User admin,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             @RequestParam("id") long id
    ) {
        JobSchedule job = jobScheduleService.getJobById(id);
        if (job == null) {
            return JsonUtil.getJson(1, "任务不存在").toString();
        }

        try {
            String hostName = IpUtil.getLocalHostName() + "-1";
            new QuartzJobFactory().runJob(job, hostName, null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return JsonUtil.getOkJson().toString();
    }

    private void addScheduleJob(JobSchedule job) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());

        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());

        logger.info("job name is " + job.getJobName() + "，job group is" + job.getJobGroup() + "，triggerKey is " + triggerKey);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        if (null == trigger) {

            JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class)
                    .withIdentity(job.getJobName(), job.getJobGroup()).build();
            jobDetail.getJobDataMap().put("scheduleJob", job);

            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getScheduleTime());

            trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            throw new RuntimeException("trigger 重复添加:" + job.getJobName() + "-" + job.getJobGroup());
        }
    }

    private void updateScheduleJob(JobSchedule job) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());

        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());

        logger.info("job name is " + job.getJobName() + "，job group is" + job.getJobGroup() + "，triggerKey is " + triggerKey);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        // Trigger已存在，那么更新相应的定时设置
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getScheduleTime());

        // 按新的cronExpression表达式重新构建trigger
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

        // 按新的trigger重新设置job执行
        scheduler.rescheduleJob(triggerKey, trigger);

    }

    private void deleteScheduleJob(JobSchedule job) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());

        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());

        logger.info("job name is " + job.getJobName() + "，job group is" + job.getJobGroup() + "，triggerKey is " + triggerKey);

        scheduler.pauseTrigger(triggerKey);
        scheduler.unscheduleJob(triggerKey);
        scheduler.deleteJob(jobKey);
    }

    private void pauseScheduleJob(JobSchedule job) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());

        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());

        logger.info("job name is " + job.getJobName() + "，job group is" + job.getJobGroup() + "，triggerKey is " + triggerKey);

        scheduler.pauseTrigger(triggerKey);
        scheduler.pauseJob(jobKey);
    }

    private void resumeScheduleJob(JobSchedule job) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());

        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());

        logger.info("job name is " + job.getJobName() + "，job group is" + job.getJobGroup() + "，triggerKey is " + triggerKey);

        scheduler.resumeTrigger(triggerKey);
        scheduler.resumeJob(jobKey);
    }
}