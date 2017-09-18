package com.schedule.quartz.job.jobdetail;

import com.schedule.quartz.job.AbstractJobBean;
import com.schedule.quartz.job.JobContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @auther zhihui.kzh
 * @create 16/8/1712:37
 */
@Component
public class DailyJob1 extends AbstractJobBean {
    private static AtomicInteger count = new AtomicInteger();

    @Override
    protected boolean executeInternal0(JobContext jobContext) {
        logger.info("DailyJob1 执行：" + count.getAndIncrement() + ",arguments=" + jobContext.getJobArguments());
        return true;
    }
}
