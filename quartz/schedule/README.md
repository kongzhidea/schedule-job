### quartz
* 基础使用
* 集成mysql



```
quartz集成mysql。



自定义任务：
1.jobName和jobGroup唯一索引，相同group相同Name的任务只能同时存在1个。
2.需要继承 AbstractJobBean， 实现executeInternal0方法，如果抛出异常则认为执行失败。
3.在spring中注入bean(@Component), jobName为对应的bean的名称，默认为类名(首字母小写)。
4.如果任务执行失败，可以在AbstractJobBean中发送邮件或者短信提醒。
5.QuartzJobFactory： 有状态任务，同一个任务下表示必须等到前一个线程处理完毕后才再启一个新的线程。
6.quartz_mysql.xml中配置 schedule，指定quartz.properties。  quartz.properties中指定对应的数据库地址。
```

#### [补偿执行](http://www.cnblogs.com/skyLogin/p/6927629.html)

```
servlet-api.3.0

下载地址：http://www.java2s.com/Code/Jar/s/Downloadservletapi30jar.htm
mvn install:install-file -DgroupId=javax.servlet -DartifactId=servlet-api -Dversion=3.0 -Dpackaging=jar -Dfile=servlet-api-3.0.jar

依赖tomcat7中的servlet-api，此工程为provide级别。

```
