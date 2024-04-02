package com.xposed.qbitkeeper.service;

import com.xposed.qbitkeeper.entity.Password;

import java.util.Map;

public interface PasswordService {

    Password addPassword(String website, String password, long userId);

    Map<String, String> getPassword(String website, long userId);

}
