
### xml配置
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:task="http://www.springframework.org/schema/task"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context-3.0.xsd
    http://www.springframework.org/schema/task
    http://www.springframework.org/schema/task/spring-task-3.1.xsd">

    <!-- 默认线程池大小为1 -->
    <!--<task:annotation-driven/>-->

    <!-- 自定义线程池 -->
    <task:annotation-driven scheduler="myScheduler"/>
    <task:scheduler id="myScheduler" pool-size="50"/>
    
    <!-- 注意配置：context:component-scan，如果已经配置过，此文件不再需要，springboot不用配置：context:component-scan --> 
</beans>
```

### java实现
```
package com.kk.schedule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 默认任务为有状态任务。
 *
 * 线上系统需要使用全局锁来方式解决同一个定时任务在多台线上服务的问题。
 */
@Component
public class MyTestService {
    private Log logger = LogFactory.getLog(this.getClass());

    @Scheduled(cron = "0/5 * *  * * ? ")   //每5秒执行一次
    public void myTest() {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("测试1");
    }

    @Scheduled(cron = "0/10 * *  * * ? ")
    public void myTest2() {
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("测试2");
    }

    @Scheduled(cron = "0/15 * *  * * ? ")
    public void myTest3() {
        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("测试3");
    }
}
```
