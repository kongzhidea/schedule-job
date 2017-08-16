package com.schedule.model;

import com.schedule.conste.JobHistoryStatusEnum;

import java.util.Date;

public class JobHistory {
    private long id;
    private String jobName;
    private String jobGroup;
    private int jobStatus; // JobHistoryStatusEnum
    private String hostName; // 执行机器 主机名
    private String ip; // 执行机器 ip
    private Date startTime;
    private Date endTime;
    private int updateUserId;
    private String updateUserName;


    public String getJobStatusDesc() {
        JobHistoryStatusEnum e = JobHistoryStatusEnum.getJobStatusEnumByStatus(jobStatus);
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

    public int getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(int jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
