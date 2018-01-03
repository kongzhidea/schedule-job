### perceptor 延时任务平台
* 业务方向平台提交任务，平台每秒轮询，到任务执行后，发metaq到客户端，客户端消费消息，执行任务。

 
#### 常见问题
```
1. 单台数据库可支持qps为：1000，如果要支持更多，需要分表分库。
2. 定时删除过期长久的任务，减少数据库堆积量。
3. 修改任务状态后必然发出metaq消息通知客户端，使用乐观锁方式。
4. 通知客户端采用metaq方式。
```

```
客户端提交任务主要参数：
    private String application; // 应用名 必填
    private String jobName; // 任务执行的类，必填
    private String arguments; // 运行参数，选填
    private String description; // 任务描述，选填
    private Date fireTime; // 执行时间戳

如果有多次延时任务，则需要指定执行时间，间隔时间，和最大执行次数，服务端处理时候，直接在数据库中根据各执行时间插入n条记录即可(看情况是否需要支持)。
多次延时任务也可以由业务方根据一次性任务自己来完成。

定时任务轮询：
/**
 * 如果有已执行，但是没有发送给mq的，需要额外特殊处理。（目前没有实现）
 * <p>
 * PollingJob.work 需要实现全局锁，方式多台机器在同一时刻执行同一个任务。
 *
 * 单台数据库可支持qps为：1000，如果要支持更多，需要分表分库。
 */
public class PollingJob {
    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    PerceptorTaskService perceptorTaskService;


    public void work() {
        long currentTime = System.currentTimeMillis() / 1000; // 精确到秒

        boolean locked = GlobakLock.trylock("polljob_" + currentTime, 5); // 全局锁，锁持续时间5秒，如果没有抢占锁，则立即返回false。
        if (!locked) {
            return;
        }

        int limit = 1; // 查询最近1分钟内没有应该执行的任务。
        long startTime = currentTime - 60 * limit;

        List<PerceptorTask> taskList = perceptorTaskService.getUnDoneTaskListByFireTime(startTime, currentTime);
        if (taskList.size() == 0) {
            return;
        }
        logger.info("待执行任务：" + taskList.size());
        for (PerceptorTask task : taskList) {
            try {
                int count = perceptorTaskService.setTaskStatus(task.getId(), TaskStatus.DONE.getId(), TaskStatus.UNDONE.getId());
                if (count == 0) {
                    logger.warn("更新状态失败,id=" + task.getId());
                    continue;
                }

                KafkaAdapter producer = KafkaAdapter.getInstance();
                TaskInfo taskInfo = TaskTransform.transTaskInfo(task);

                producer.sendMessage(task.getApplication(), JSON.toJSONString(taskInfo));

                int send = perceptorTaskService.setTaskStatus(task.getId(), TaskStatus.SEND.getId(), TaskStatus.DONE.getId());
                logger.debug("定时任务执行完成：id=" + task.getId() + ",更新发送状态：" + (send == 1));
            } catch (Exception e) {
                logger.error(task.getId() + "," + e.getMessage(), e);
            }

        }
    }
}
```
