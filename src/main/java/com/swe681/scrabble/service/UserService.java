package com.swe681.scrabble.service;

import com.swe681.scrabble.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
