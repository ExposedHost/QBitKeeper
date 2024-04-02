package com.xposed.qbitkeeper.service;

import com.xposed.qbitkeeper.entity.User;

import java.util.List;

public interface UserService {
    User addUser(String userName, String email, String password);
    User loginUser(String userName, String password);
    void deleteAllUser();
    List<User> getAllUser();

    User findUserById(long id);
}
