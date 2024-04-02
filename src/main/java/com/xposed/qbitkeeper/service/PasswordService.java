package com.xposed.qbitkeeper.service;

import com.xposed.qbitkeeper.entity.Password;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Map;

public interface PasswordService {

    Password addPassword(String website, String password, long userId) throws Exception;

    Map<String, String> getPassword(String website, long userId) throws Exception;

}
