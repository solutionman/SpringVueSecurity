package com.backvuebato.utils;

import com.backvuebato.entity.Persons;
import com.backvuebato.entity.Users;
import com.backvuebato.repository.PersonRepository;
import com.backvuebato.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class TableUtils {
    private PersonRepository personRepository;
    private UserRepository userRepository;
    private DateParseUtils dateParseUtils = new DateParseUtils();

    public TableUtils(PersonRepository personRepository, UserRepository userRepository){
        this.personRepository = personRepository;
        this.userRepository = userRepository;
    }

    public Map<String, Object> sortedTable(Map<String, Object> data) {
        String sortDesc = data.get("sortDesc").toString();
        int itemsPerPage = parseInt(data.get("itemsPerPage").toString());
        String orderBy = data.get("sortBy").toString();
        String forPageable;
        switch (orderBy) {
            case "[familyname]":
                forPageable = "familyname";
                break;
            case "[firstname]":
                forPageable = "firstname";
                break;
            case "[middlename]":
                forPageable = "middlename";
                break;
            case "[birthday]":
                forPageable = "birthday";
                break;
            default:
                forPageable = "id";
        }
        int currPage = parseInt(data.get("page").toString()) - 1;
        Pageable pageable = PageRequest.of(currPage, itemsPerPage, Sort.by(sortDesc.equals("[true]") ? Sort.Direction.DESC : Sort.Direction.ASC, forPageable));
        Page<Persons> page;
        String search = null == data.get("search") ? "" : data.get("search").toString();
        if (search.isEmpty()) {
            page = personRepository.findAll(pageable);
        } else {
            page = personRepository.findByFamilynameContainingIgnoreCaseOrFirstnameContainingIgnoreCaseOrMiddlenameContainingIgnoreCase(
                    search, search, search, pageable);
        }

        List<Persons> personsList = page.getContent();

        List<Map<String, Object>> personsToDisplay = new ArrayList<>();
        for (Persons person : personsList) {
            Map<String, Object> currentPerson = new HashMap<>();

            currentPerson.put("id", person.getId());
            Users user = userRepository.findById(person.getUserid());
            currentPerson.put("name", user.getUsername());
            currentPerson.put("familyname", person.getFamilyname());
            currentPerson.put("firstname", person.getFirstname());
            currentPerson.put("middlename", person.getMiddlename());
            currentPerson.put("birthday", dateParseUtils.sqlDateToString(person.getBirthday()));
            currentPerson.put("additionalData", "some data to show about this person, for example email: "
                    + (person.getEmail() == null ? "" : person.getEmail()));
            personsToDisplay.add(currentPerson);
        }
        long totalPersons = page.getTotalElements();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("persons", personsToDisplay);
        resultMap.put("totalPersons", totalPersons);

        return resultMap;
    }
}
