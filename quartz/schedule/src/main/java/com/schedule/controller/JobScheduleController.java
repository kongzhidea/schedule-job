package com.schedule.controller;

import com.alibaba.fastjson.JSON;
import com.schedule.annotation.Authorization;
import com.schedule.conste.JobStatusEnum;
import com.schedule.model.JobSchedule;
import com.schedule.model.User;
import com.schedule.param.JobScheduleParam;
import com.schedule.service.JobScheduleService;
import com.schedule.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
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

// 任务 状态信息？？ 任务历史？？  job实现接口,判断？？ todo
@Controller
@RequestMapping("job")
@Authorization
public class JobScheduleController {
    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    JobScheduleService jobScheduleService;


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
        job.setScheduleTime(cronExpression);

        job.setUptime(new Date());
        job.setUpdateUserId(admin.getId());
        job.setUpdateUserName(admin.getRealname());
        if (id > 0) {
            jobScheduleService.updateJobById(job);
        } else {
            job.setStatus(JobStatusEnum.RUN.getValue());
            job.setAddtime(new Date());
            jobScheduleService.addJob(job);
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
        logger.info(admin.getRealname() + "删除任务,job=" + JSON.toJSONString(job));
        jobScheduleService.deleteJobById(id);

        return JsonUtil.getOkJson().toString();
    }
}