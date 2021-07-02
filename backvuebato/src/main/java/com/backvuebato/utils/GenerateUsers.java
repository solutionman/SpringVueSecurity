package com.backvuebato.utils;

import com.backvuebato.entity.Persons;
import com.backvuebato.entity.Roles;
import com.backvuebato.entity.Users;
import com.backvuebato.repository.PersonRepository;
import com.backvuebato.repository.RolesRepository;
import com.backvuebato.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Date;
import java.util.Set;

public class GenerateUsers implements Runnable{
    RandomUtils randomUtils = new RandomUtils();
    UserRepository userRepository;
    RolesRepository rolesRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    PersonRepository personRepository;

    public GenerateUsers(UserRepository userRepository, RolesRepository rolesRepository, BCryptPasswordEncoder bCryptPasswordEncoder, PersonRepository personRepository){
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.personRepository = personRepository;
    }

    @Override
    public void run() {
        java.lang.System.out.println("Thread started");
        String name = Thread.currentThread().getName();
        long id = Thread.currentThread().getId();
        java.lang.System.out.println( "name: " + name + " id: " + id);

        // TODO synchronize threads

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

        Users user = new Users();
        user.setUsername(userName);
        user.setPassword(bCryptPasswordEncoder.encode(pass));
        user.setRoles(roles);
        userRepository.save(user);
        Persons person = new Persons();
        person.setUserid(user.getId());
        person.setFirstname(firstName);
        person.setFamilyname(familyName);
        person.setMiddlename(middleName);
        person.setBirthday(randomDate);
        person.setEmail(email);
        personRepository.save(person);


        java.lang.System.out.println("Thread ended");
    }
}
