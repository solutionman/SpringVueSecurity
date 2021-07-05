package com.backvuebato.controller;

import com.backvuebato.entity.Persons;
import com.backvuebato.entity.Users;
import com.backvuebato.repository.PersonRepository;
import com.backvuebato.repository.UserRepository;
import com.backvuebato.utils.DateParseUtils;
import com.backvuebato.utils.TableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PersonController {

    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    DateParseUtils dateParseUtils = new DateParseUtils();
    TableUtils tableUtils;

    public PersonController(PersonRepository personRepository, UserRepository userRepository) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
        tableUtils = new TableUtils(personRepository, userRepository);
    }

    @PostMapping(value = "persons")
    public Map<String, Object> getPersons(@RequestBody Map<String, Object> data) {
        return tableUtils.sortedTable(data);
    }

}
