package com.xposed.qbitkeeper.service.impls;

import com.xposed.qbitkeeper.entity.User;
import com.xposed.qbitkeeper.repo.UserRepository;
import com.xposed.qbitkeeper.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public User addUser(String userName, String email, String password) {
        User user = new User();
        User prevUserName = userRepository.findByUserName(userName);
        if(prevUserName != null){
            throw new IllegalStateException("Username already exists");
        }

        User prevUserEmail = userRepository.findByEmail(email);
        if(prevUserEmail != null){
            throw new IllegalStateException("Email ID already exists");
        }
        user.setUserName(userName);
        user.setEmail(email);
        this.passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = this.passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    @Override
    public User loginUser(String userName, String password) {
        User user = userRepository.findByUserName(userName);
        if(user == null){
            throw new IllegalStateException("We couldn't find an account with that username!");
        }
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if(matches){
            return user;
        }else{
            throw new IllegalStateException("Invalid password");
        }
    }

    @Override
    public User findUserById(long id){
        User user = userRepository.findById(id).orElseThrow(()->new RuntimeException("Account does not exist"));
        return user;
    }

    @Override
    public void deleteAllUser() {
        userRepository.deleteAll();
    }

    @Override
    public List<User> getAllUser() {
        List<User> users = userRepository.findAll();
        return new ArrayList<>(users);
    }

}
