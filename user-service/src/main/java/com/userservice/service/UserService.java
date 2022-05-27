package com.userservice.service;

import com.userservice.dao.UserDao;
import com.userservice.entity.User;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final String USER_CREATE = "user_create";

    @Autowired
    private UserDao userDao;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public User findById(int id) {

        return userDao.findById(id);
    }

    @Transactional
    public void update(User user) {

        userDao.update(user);
    }

    @Transactional
    public int save(User user) {

        int userId = userDao.save(user);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("userEmail", user.getEmail());
        jsonObject.put("userContact", user.getContact());

        kafkaTemplate.send(USER_CREATE, jsonObject.toJSONString());

        return userId;
    }

}
