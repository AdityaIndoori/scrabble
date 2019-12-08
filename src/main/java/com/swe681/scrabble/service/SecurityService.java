package com.swe681.scrabble.service;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
