package com.backvuebato.service;

import com.backvuebato.entity.Roles;
import com.backvuebato.entity.Users;
import com.backvuebato.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = this.userRepository.findByUsername(username);
        if(null == user){
            System.out.println("User not found " + username);
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }
        Set<Roles> roleSet = user.getRoles();
        List<String> roleNames = new ArrayList<>();
        for(Roles role : roleSet){
            roleNames.add(role.getName());
        }
        List<GrantedAuthority> grantList = new ArrayList<>();
        for (String rol : roleNames){
            GrantedAuthority authority = new SimpleGrantedAuthority(rol);
            grantList.add(authority);
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantList);
    }
}
