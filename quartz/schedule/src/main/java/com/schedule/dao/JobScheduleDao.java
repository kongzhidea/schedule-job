package com.schedule.dao;

import com.schedule.model.JobSchedule;
import com.schedule.param.JobScheduleParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface JobScheduleDao {

    public void insert(JobSchedule jobSchedule);

    public JobSchedule getById(long id);

    public void updateById(JobSchedule jobSchedule);

    public void deleteById(long id);

    List<JobSchedule> search(@Param("param") JobScheduleParam param, @Param("start") int start, @Param("limit") int limit);

    int count(@Param("param") JobScheduleParam param);

    JobSchedule getByNameGroup(String name, String group);
}
