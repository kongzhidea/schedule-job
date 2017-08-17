package com.schedule.quartz.job;

import com.schedule.conste.JobHistoryStatusEnum;
import com.schedule.model.JobHistory;
import com.schedule.model.JobSchedule;
import com.schedule.service.JobHistoryService;
import com.schedule.service.JobScheduleService;
import com.schedule.utils.ApplicationContextHelper;
import com.schedule.utils.IpUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 有状态任务，表示必须等到前一个线程处理完毕后才再启一个新的线程。
 */
@DisallowConcurrentExecution
public class QuartzJobFactory implements Job {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private JobHistoryService jobHistoryService = ApplicationContextHelper.getBean(JobHistoryService.class);
    private JobScheduleService jobScheduleService = ApplicationContextHelper.getBean(JobScheduleService.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            int length = context.getFireInstanceId().length() - 26;
            String hostName = context.getFireInstanceId().substring(0, length);

            JobSchedule scheduleJob = (JobSchedule) context.getMergedJobDataMap().get("scheduleJob");

            runJob(scheduleJob, hostName, context);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void runJob(JobSchedule scheduleJob, String hostName, JobExecutionContext context) throws JobExecutionException {
        // 任务执行历史记录
        JobHistory jobHistory = new JobHistory();
        jobHistory.setJobName(scheduleJob.getJobName());
        jobHistory.setJobGroup(scheduleJob.getJobGroup());
        jobHistory.setJobStatus(JobHistoryStatusEnum.RUNING.getValue());
        jobHistory.setStartTime(new Date());
        jobHistory.setHostName(hostName);

        jobHistory.setIp(IpUtil.getLocalAddr());

        jobHistory.setUpdateUserId(scheduleJob.getUpdateUserId());
        jobHistory.setUpdateUserName(scheduleJob.getUpdateUserName());

        jobHistoryService.addJobHistory(jobHistory);


        // 任务运行时参数
        JobContext jobContext = new JobContext();
        jobContext.setHostName(hostName);

        // 重新设置arguments
        JobSchedule ori = jobScheduleService.getJobByNameGroup(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduleJob.setArguments(ori.getArguments());

        jobContext.setJobSchedule(scheduleJob);



        AbstractJobBean job = (AbstractJobBean) ApplicationContextHelper.getBean(scheduleJob.getJobName());
        job.setJobContext(jobContext);

        if (context == null) {
            job.executeInternal(context); // 运行单次任务
        } else {
            job.execute(context); // 运行任务
        }

        jobHistory.setJobStatus(job.getJobStatus()); // 更新任务运行结果
        jobHistory.setEndTime(new Date());

        jobHistoryService.updateJobHistoryById(jobHistory);
    }
}