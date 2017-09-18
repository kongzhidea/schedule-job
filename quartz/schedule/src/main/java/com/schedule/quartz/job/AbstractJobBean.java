package com.schedule.quartz.job;

import com.schedule.conste.JobHistoryStatusEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 定时任务需要继承此类，实现executeInternal0方法，如果抛出异常则认为执行失败。
 */
public abstract class AbstractJobBean extends QuartzJobBean {
    private int jobStatus; // JobHistoryStatusEnum

    private JobContext jobContext;

    protected Log logger = LogFactory.getLog(this.getClass());

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if (printLog()) {
            logger.info(this.getClass().getName() + " start");
        }

        try {
            boolean result = executeInternal0(jobContext);
            if (result) {
                setJobStatus(JobHistoryStatusEnum.SUCCESS.getValue());
            } else {
                setJobStatus(JobHistoryStatusEnum.FAILED.getValue());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setJobStatus(JobHistoryStatusEnum.FAILED.getValue());
        }

        if (printLog()) {
            logger.info(this.getClass().getName() + " end");
        }

        // 如果任务执行失败，可以在此处发送邮件或者短信提醒。
        if (getJobStatus() != JobHistoryStatusEnum.SUCCESS.getValue()) {
            logger.info("任务失败:" + jobContext.getJobSchedule().getJobName());
        }
    }

    protected abstract boolean executeInternal0(JobContext context);

    // 打印开始和结束log
    protected boolean printLog() {
        return true;
    }

    public int getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(int jobStatus) {
        this.jobStatus = jobStatus;
    }

    public JobContext getJobContext() {
        return jobContext;
    }

    public void setJobContext(JobContext jobContext) {
        this.jobContext = jobContext;
    }
}
