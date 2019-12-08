package com.swe681.scrabble.service;

import com.swe681.scrabble.model.User;
import com.swe681.scrabble.repository.RoleRepository;
import com.swe681.scrabble.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder; //Instance of the Password Salted Hashing

    @Autowired
    private RoleRepository roleRepository; //Instance of the role table in database

    @Autowired
    private UserRepository userRepository; //Instance of the user table in database

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(roleRepository.findAll()));
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
