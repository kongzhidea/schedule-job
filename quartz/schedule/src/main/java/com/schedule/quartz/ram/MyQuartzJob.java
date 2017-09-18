package com.schedule.quartz.ram;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 是否允许任务并发执行。
 * <p>
 * 1.MethodInvokingJobDetailFactoryBean 中设置concurrent，默认true。当值为false时，表示必须等到前一个线程处理完毕后才再启一个新的线程。 不用实现接口，通过自定义方法的方式做。
 * <p>
 * 有状态job表示必须等到前一个线程处理完毕后才再启一个新的线程。
 */
public class MyQuartzJob {
    private Log logger = LogFactory.getLog(this.getClass());


    public void work() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("work");

    }

}
