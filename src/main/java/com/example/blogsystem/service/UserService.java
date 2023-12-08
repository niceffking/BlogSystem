package com.example.blogsystem.service;

import com.example.blogsystem.dao.UserMapper;
import com.example.blogsystem.model.Articleinfo;
import com.example.blogsystem.model.Userinfo;
import com.example.blogsystem.model.vo.UserinfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public int reg(Userinfo userinfo) {
        return userMapper.reg(userinfo);
    }

    public Userinfo getUserByName(String username) {
        return userMapper.getUserByName(username);
    }

    public UserinfoVO getUserById(int id) {
        return userMapper.getUserById(id);
    }
}
