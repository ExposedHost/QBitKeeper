package com.xposed.qbitkeeper.controller;

import com.xposed.qbitkeeper.entity.Password;
import com.xposed.qbitkeeper.entity.User;
import com.xposed.qbitkeeper.service.PasswordService;
import com.xposed.qbitkeeper.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class PasswordController {
    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService, UserService userService){
        this.passwordService = passwordService;
    }

    @PostMapping("/passwords")
    public ResponseEntity<Password> addPassword(@RequestParam(name = "user_id") long userId, @RequestBody Map<String, String> lookupObject) throws Exception {
        Password password = passwordService.addPassword(lookupObject.get("website"), lookupObject.get("password"), userId);
        return new ResponseEntity<>(password, HttpStatus.CREATED);
    }

    @GetMapping("/passwords")
    public ResponseEntity<String> getPassword(@RequestParam(name = "user_id") long userId, @RequestBody Map<String, String> lookupObject) throws Exception {
        Map<String, String> password = passwordService.getPassword(lookupObject.get("website"), userId);
        return ResponseEntity.ok(lookupObject.get("website")+" "+password.get(lookupObject.get("website")));
    }
}
