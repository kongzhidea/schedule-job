package com.schedule.model;

import com.schedule.conste.JobStatusEnum;

import java.io.Serializable;
import java.util.Date;

public class JobSchedule implements Serializable {
    private static final long serialVersionUID = -1l;

    private long id;

    private String jobName; // job名称，对应的任务类
    private String jobGroup; // 任务组，分组使用

    private int status; // JobStatusEnum
    private String description; // 备注

    private String scheduleTime; // 调度表达式

    private int updateUserId;
    private String updateUserName;
    private Date addtime;
    private Date uptime;

    private String arguments; // 运行参数

    private Date nextFireTime; // 下一次执行时间，实时计算

    public String getStatusDesc() {
        JobStatusEnum e = JobStatusEnum.getJobStatusEnumByStatus(status);
        if (e == null) {
            return null;
        }
        return e.getDesc();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public int getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(int updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Date getUptime() {
        return uptime;
    }

    public void setUptime(Date uptime) {
        this.uptime = uptime;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }
}
