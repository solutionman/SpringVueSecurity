package com.backvuebato.utils;

import com.backvuebato.entity.Roles;
import com.backvuebato.repository.RolesRepository;
import com.backvuebato.repository.UserRepository;

import java.sql.Date;
import java.util.Set;

public class GenerateUsers implements Runnable{
    RandomUtils randomUtils = new RandomUtils();

    public void generateUsers(UserRepository userRepository, RolesRepository rolesRepository){

        run();

        String userName = randomUtils.generateAlphabeticRandom("username");
        while (userRepository.findByUsername(userName) != null) {
            userName = randomUtils.generateAlphabeticRandom("username");
        }
        String firstName = randomUtils.generateAlphabeticRandom("firstName");
        String familyName = randomUtils.generateAlphabeticRandom("familyName");
        String middleName = randomUtils.generateAlphabeticRandom("middleName");
        Set<Roles> roles = randomUtils.generateRoles(rolesRepository);
        String pass = randomUtils.generateAlphaNumericRandom();
        Date randomDate = randomUtils.randomSqlDate();
        String email = randomUtils.generateAlphabeticRandom("email");
    }

    @Override
    public void run() {
        java.lang.System.out.println("Thread started");
        String name = Thread.currentThread().getName();
        long id = Thread.currentThread().getId();
        java.lang.System.out.println( "name: " + name + " id: " + id);

        java.lang.System.out.println("Thread ended");
    }
}
