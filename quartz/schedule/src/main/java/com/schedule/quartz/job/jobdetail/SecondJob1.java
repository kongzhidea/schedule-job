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
public class SecondJob1 extends AbstractJobBean {
    private static AtomicInteger count = new AtomicInteger();

    @Override
    protected boolean printLog() {
        return false;
    }

    @Override
    protected boolean executeInternal0(JobContext jobContext) {
        try {
            Thread.sleep(1000 * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("SecondJob1 执行：" + count.getAndIncrement() + ",arguments=" + jobContext.getJobArguments());
        return true;
    }
}
