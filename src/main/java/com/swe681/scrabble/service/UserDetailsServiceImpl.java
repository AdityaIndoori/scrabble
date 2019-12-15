package com.swe681.scrabble.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.swe681.scrabble.model.Role;
import com.swe681.scrabble.model.User;
import com.swe681.scrabble.repository.UserRepository;

/**
 * To implement login/authentication with Spring Security,
 * we need to implement org.springframework.security.core.userdetails.UserDetailsService interface
 */
@Service
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
        User user = userRepository.findByUsername(username);
        if (user == null) {
        	throw new UsernameNotFoundException(username);
        }
        user.setRoles(new HashSet<>());
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
