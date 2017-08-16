package com.schedule.service;

import com.schedule.dao.JobHistoryDao;
import com.schedule.model.JobHistory;
import com.schedule.param.JobHistoryParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther zhihui.kzh
 * @create 15/8/1721:18
 */
@Service
public class JobHistoryService {
    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    JobHistoryDao jobHistoryDao;

    public void addJobHistory(JobHistory jobHistory) {
        jobHistoryDao.insert(jobHistory);
    }

    public void updateJobHistoryById(JobHistory jobHistory) {
        jobHistoryDao.updateById(jobHistory);
    }

    public List<JobHistory> search(JobHistoryParam param, int start, int limit) {
        return jobHistoryDao.search(param, start, limit);
    }

    public int count(JobHistoryParam param) {
        return jobHistoryDao.count(param);
    }

}
