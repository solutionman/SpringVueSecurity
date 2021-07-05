package com.backvuebato.controller;

import com.backvuebato.repository.PersonRepository;
import com.backvuebato.repository.UserRepository;
import com.backvuebato.utils.TableUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PersonController {
    TableUtils tableUtils;

    public PersonController(PersonRepository personRepository, UserRepository userRepository) {
        tableUtils = new TableUtils(personRepository, userRepository);
    }

    @PostMapping(value = "persons")
    public Map<String, Object> getPersons(@RequestBody Map<String, Object> data) {
        return tableUtils.sortedTable(data);
    }

}
