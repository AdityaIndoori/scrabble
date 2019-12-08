package com.swe681.scrabble.service;

import com.swe681.scrabble.model.Role;
import com.swe681.scrabble.model.User;
import com.swe681.scrabble.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * To implement login/authentication with Spring Security,
 * we need to implement org.springframework.security.core.userdetails.UserDetailsService interface
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    /**
     * The method is used to load the user's details by searching in the database.
     * @param username is the name typed in the text field
     * @return an object containing the user name, password and the set of roles they have an authority for
     * @throws UsernameNotFoundException is thrown when the username entered is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername: " + username);
        User user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException(username);
        log.info("loadUserByUsername2:" + user.getUsername() + " " + user.getPassword() + " " + user.getId());
        user.setRoles(new HashSet<>());
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        log.info("loadUserByUsername3:" + "Size = " + grantedAuthorities.size());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
