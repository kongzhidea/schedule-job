
```
quartz集成mysql。



自定义任务：
1.jobName和jobGroup唯一索引，相同group相同Name的任务只能同时存在1个。
2.需要继承 AbstractJobBean， 实现executeInternal0方法，如果抛出异常则认为执行失败。
3.在spring中注入bean(@Component), jobName为对应的bean的名称，默认为类名(首字母小写)。
4.如果任务执行失败，可以在AbstractJobBean中发送邮件或者短信提醒。
5.QuartzJobFactory： 有状态任务，同一个任务下表示必须等到前一个线程处理完毕后才再启一个新的线程。
```