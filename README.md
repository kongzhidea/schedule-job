# schedule-job
* 定时任务执行，如quartz、
* 常驻任务：quartz/schedule，spring-scheduled
* perceptor 延时任务平台，业务方向平台提交任务，平台每秒轮询，到任务执行后，发metaq到客户端，客户端消费消息，执行任务。
