CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `password` varchar(64) CHARACTER SET utf8 DEFAULT '',
  `realname` varchar(64) CHARACTER SET utf8 DEFAULT '',
  `uptime` datetime DEFAULT NULL,
  `mobile` varchar(32) CHARACTER SET utf8 DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  `mail` varchar(32) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

INSERT INTO `user` (`id`, `username`, `password`, `realname`, `uptime`, `mobile`, `status`, `mail`) VALUES (1, 'abc', '202cb962ac59075b964b07152d234b70', 'kk', '2016-07-14 19:28:04', '12345678901', 0, 'abc@qq.com');

-- user表 密码为，123


CREATE TABLE `job_schedule` (
  `id` bigint(21) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(80) NOT NULL COMMENT '任务名称',
  `job_group` varchar(80) DEFAULT NULL COMMENT '任务所属的组',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '任务调度状态，1正常，2暂停',
  `description` varchar(150) DEFAULT NULL COMMENT '任务描述',
  `schedule_time` varchar(64) NOT NULL COMMENT '任务调度表达式',
  `update_user_id` int(11) DEFAULT NULL COMMENT '维护人',
  `update_user_name` varchar(64) DEFAULT NULL COMMENT '维护人',
  `addtime` datetime DEFAULT NULL COMMENT '创建时间',
  `uptime` datetime DEFAULT NULL COMMENT '更新时间',
  `arguments` varchar(1024) DEFAULT NULL COMMENT '运行参数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_job_name_group` (`job_name`,`job_group`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='Job调度任务表';


CREATE TABLE `job_history` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '任务执行结果自增ID',
  `job_name` varchar(80) NOT NULL COMMENT '任务名称',
  `job_group` varchar(80) DEFAULT NULL COMMENT '任务所属的组',
  `job_status` int(11) DEFAULT '0' COMMENT '任务执行结果，1正在执行，2执行成功，3执行失败',
  `host_name` varchar(150) DEFAULT NULL COMMENT '主机名称',
  `ip` varchar(64) DEFAULT NULL COMMENT '主机ip',
  `start_time` datetime DEFAULT NULL COMMENT '任务执行开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '任务执行结束时间',
  `update_user_id` int(11) DEFAULT NULL COMMENT '任务维护人',
  `update_user_name` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='Job执行结果记录表';
