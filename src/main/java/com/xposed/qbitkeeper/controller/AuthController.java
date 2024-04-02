package com.xposed.qbitkeeper.controller;

import com.xposed.qbitkeeper.entity.User;
import com.xposed.qbitkeeper.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AuthController {
    private UserService userService;
    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> addUser(@RequestBody Map<String, String> userBody) throws Exception {
        return new ResponseEntity<>(userService.addUser(userBody.get("user_name"), userBody.get("email") ,userBody.get("password")), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody Map<String, String> userBody) throws Exception {
        User loggedUser = userService.loginUser(userBody.get("user_name"), userBody.get("password"));
        return ResponseEntity.ok(loggedUser);
    }

    @DeleteMapping("/users")
    public ResponseEntity<String> deleteAllUsers() throws Exception{
        userService.deleteAllUser();
        return ResponseEntity.ok("Deleted all users");
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAllUsers(){
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }

}
