package com.backvuebato.controller;

import com.backvuebato.entity.Persons;
import com.backvuebato.entity.Roles;
import com.backvuebato.entity.Users;
import com.backvuebato.repository.PersonRepository;
import com.backvuebato.repository.RolesRepository;
import com.backvuebato.repository.UserRepository;
import com.fasterxml.jackson.databind.util.JSONPObject;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final RolesRepository rolesRepository;

    public UserController(UserRepository userRepository, PersonRepository personRepository, RolesRepository rolesRepository) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.rolesRepository = rolesRepository;
    }

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(value = "profile")
    public Map<String, Object> profile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        Users user = userRepository.findByUsername(name);
        Persons person = personRepository.findByUserid(user.getId());
        Map<String, Object> profile = new HashMap<>();
        profile.put("first_name", person.getFirstname());
        profile.put("second_name", person.getFamilyname());
        profile.put("middle_name", person.getMiddlename());
        profile.put("birthday", person.getBirthday());
        profile.put("email", person.getEmail());
        profile.put("username", user.getUsername());

        return profile;
    }

    @PostMapping(value = "edit_profile")
    public Map<String, Object> editProfile(@RequestBody Map<String, Object> formValues){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        Users user = userRepository.findByUsername(name);
        Persons person = personRepository.findByUserid( user.getId() );

        person.setFirstname( formValues.get("first_name").toString() );
        person.setFamilyname( formValues.get("second_name").toString() );
        person.setMiddlename( formValues.get("middle_name").toString() );
        person.setEmail( formValues.get("email").toString() );
        personRepository.save( person );

        Map<String, Object> profile = new HashMap<>();
        profile.put("first_name", person.getFirstname());
        profile.put("second_name", person.getFamilyname());
        profile.put("middle_name", person.getMiddlename());
        profile.put("birthday", person.getBirthday());
        profile.put("email", person.getEmail());

        return profile;
    }

    @PostMapping(value = "newuser")
    public Map<String, Object> newUser(@RequestBody Map<String, Object> formValues){
        if( null != userRepository.findByUsername( formValues.get("username").toString() ) ){
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", "user already exist");
            return errorResult;
        }
        Users user = new Users();
        user.setUsername( formValues.get("username").toString() );
        user.setPassword( bCryptPasswordEncoder.encode( formValues.get("password").toString() ) );
        List<String> roles = (List<String>) formValues.get("roles");
        Set<Roles> rolesToSet = new HashSet<>();
        for(String role : roles){
            Roles roleToAdd = rolesRepository.findByName(role);
            rolesToSet.add(roleToAdd);
        }
        user.setRoles(rolesToSet);
        userRepository.save( user );
        Persons person = new Persons();
        person.setUserid( userRepository.findByUsername(user.getUsername()).getId() );
        person.setFirstname( formValues.get("first_name").toString() );
        person.setFamilyname( formValues.get("second_name").toString() );
        person.setMiddlename( formValues.get("middle_name").toString() );
        // TODO setBirthday
        person.setEmail( formValues.get("email").toString() );
        personRepository.save(person);

        Map<String, Object> newUser = new HashMap<>();
        return newUser;
    }

    @PostMapping(value = "roles")
    public Map<String, Object> roles(){
        List<Roles> rolesList = rolesRepository.findAll();
        List<String> roles = new ArrayList<>();
        for(Roles role : rolesList){
            roles.add(role.getName());
        }
        Map<String, Object> rolesMap = new HashMap<>();
        rolesMap.put("roles", roles);
        return rolesMap;
    }
}
