package com.schedule.service;

import com.schedule.model.User;
import com.schedule.param.UserParam;
import com.schedule.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getUser(int id) {
        return userDao.getUser(id);
    }

    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    public List<User> search(UserParam param, int start, int limit) {
        return userDao.search(param, start, limit);
    }

    public int count(UserParam param) {
        return userDao.count(param);
    }

    public void addUser(User user) {
        userDao.addUser(user);
    }
}
