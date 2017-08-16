package com.schedule.quartz.job;

import com.schedule.model.JobSchedule;

/**
 *
 * 任务参数：jobSchedule.arguments
 *
 * @auther zhihui.kzh
 * @create 16/8/1712:41
 */
public class JobContext {
    private String hostName; // 执行主机名称

    private JobSchedule jobSchedule; // 执行时任务定义。

    // 任务运行参数
    public String getJobArguments(){
        return jobSchedule.getArguments();
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public JobSchedule getJobSchedule() {
        return jobSchedule;
    }

    public void setJobSchedule(JobSchedule jobSchedule) {
        this.jobSchedule = jobSchedule;
    }
}
