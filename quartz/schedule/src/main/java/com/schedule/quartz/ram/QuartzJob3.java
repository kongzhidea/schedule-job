package com.schedule.quartz.ram;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;


/**
 * 是否允许任务并发执行。
 * <p>
 * 2.Job和QuartzJobBean默认为无状态job， 如果@DisallowConcurrentExecution加上注解，则为有状态job。
 * <p>
 * 有状态job表示必须等到前一个线程处理完毕后才再启一个新的线程。
 */
public class QuartzJob3 extends QuartzJobBean {

    private Log logger = LogFactory.getLog(this.getClass());

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("job3");
    }
}