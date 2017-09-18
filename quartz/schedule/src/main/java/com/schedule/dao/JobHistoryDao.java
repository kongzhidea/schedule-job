package com.schedule.dao;

import com.schedule.model.JobHistory;
import com.schedule.param.JobHistoryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface JobHistoryDao {

    public void insert(JobHistory jobHistory);

    public JobHistory getById(long id);

    public void updateById(JobHistory jobHistory);

    public void deleteById(long id);

    List<JobHistory> search(@Param("param") JobHistoryParam param, @Param("start") int start, @Param("limit") int limit);

    int count(@Param("param") JobHistoryParam param);
}
