package com.schedule.service;

import com.schedule.dao.JobScheduleDao;
import com.schedule.model.JobSchedule;
import com.schedule.param.JobScheduleParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther zhihui.kzh
 * @create 14/8/1721:17
 */
@Service
public class JobScheduleService {
    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    JobScheduleDao jobScheduleDao;

    public void addJob(JobSchedule jobSchedule) {
        jobScheduleDao.insert(jobSchedule);
    }

    public JobSchedule getJobById(long id) {
        return jobScheduleDao.getById(id);
    }

    public JobSchedule getJobByNameGroup(String name, String group) {
        return jobScheduleDao.getByNameGroup(name, group);
    }

    public void updateJobById(JobSchedule jobSchedule) {
        jobScheduleDao.updateById(jobSchedule);
    }

    public void deleteJobById(long id) {
        jobScheduleDao.deleteById(id);
    }

    public List<JobSchedule> search(JobScheduleParam param, int start, int limit) {
        return jobScheduleDao.search(param, start, limit);
    }

    public int count(JobScheduleParam param) {
        return jobScheduleDao.count(param);
    }
}
