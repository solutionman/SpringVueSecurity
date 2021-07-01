package com.backvuebato.controller;

import com.backvuebato.entity.Persons;
import com.backvuebato.entity.Roles;
import com.backvuebato.entity.Users;
import com.backvuebato.repository.PersonRepository;
import com.backvuebato.repository.RolesRepository;
import com.backvuebato.repository.UserRepository;
import com.backvuebato.utils.DateParseUtils;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.*;

import static java.lang.Integer.parseInt;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final RolesRepository rolesRepository;
    DateParseUtils dateParseUtils = new DateParseUtils();

    public UserController(UserRepository userRepository, PersonRepository personRepository, RolesRepository rolesRepository) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.rolesRepository = rolesRepository;
    }

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(value = "profile")
    public Map<String, Object> profile() {
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
    public Map<String, Object> editProfile(@RequestBody Map<String, Object> formValues) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        Users user = userRepository.findByUsername(name);
        Persons person = personRepository.findByUserid(user.getId());

        person.setFirstname(formValues.get("first_name").toString());
        person.setFamilyname(formValues.get("second_name").toString());
        person.setMiddlename(formValues.get("middle_name").toString());
        person.setEmail(formValues.get("email").toString());
        Date sqlBirthday = dateParseUtils.stringToSqlDate(formValues.get("birthday"));
        if (sqlBirthday != null) {
            person.setBirthday(sqlBirthday);
        }
        personRepository.save(person);

        Map<String, Object> profile = new HashMap<>();
        profile.put("first_name", person.getFirstname());
        profile.put("second_name", person.getFamilyname());
        profile.put("middle_name", person.getMiddlename());
        profile.put("birthday", person.getBirthday());
        profile.put("email", person.getEmail());

        return profile;
    }

    @PostMapping(value = "editUser")
    public Map<String, Object> editUser(@RequestBody Map<String, Object> userToEdit) {
        // TODO solve problem with losing parameter on reloading page (seems like I have to add userID to url)
        if (!userToEdit.isEmpty()) {
            Object pers = userToEdit.get("person");
            try {
                Map<String, Object> p = (Map<String, Object>) pers;
                int id = (int) p.get("id");
                Persons person = personRepository.findById(id);
                Map<String, Object> profile = new HashMap<>();
                profile.put("first_name", person.getFirstname());
                profile.put("second_name", person.getFamilyname());
                profile.put("middle_name", person.getMiddlename());
                profile.put("birthday", person.getBirthday());
                profile.put("email", person.getEmail());
                Users user = userRepository.findById(person.getUserid());
                profile.put("username", user.getUsername());
                String debug = "";
                List<String> userRoles = new ArrayList<>();
                for (Roles role : user.getRoles()) {
                    userRoles.add(role.getName());
                }
                List<Roles> rolesList = rolesRepository.findAll();
                List<String> roles = new ArrayList<>();
                for (Roles role : rolesList) {
                    roles.add(role.getName());
                }
                Map<String, Object> rolesMap = new HashMap<>();
                rolesMap.put("roles", roles);
                rolesMap.put("profile", profile);
                rolesMap.put("userRoles", userRoles);
                return rolesMap;

            } catch (Exception e) {
                java.lang.System.out.println("Error while getting person data " + e.getLocalizedMessage());
            }
        }


//        return profile;
        List<Roles> rolesList = rolesRepository.findAll();
        List<String> roles = new ArrayList<>();
        for (Roles role : rolesList) {
            roles.add(role.getName());
        }
        Map<String, Object> rolesMap = new HashMap<>();
        rolesMap.put("roles", roles);
        return rolesMap;
    }

    @PostMapping(value = "doEditUser")
    public Map<String, Object> doEditUser(@RequestBody Map<String, Object> formValues) {
        if (!formValues.isEmpty()) {
            Object pers = formValues.get("person");
            Map<String, Object> fromForm = (Map<String, Object>) pers;
            Map<String, Object> person = (Map<String, Object>) ((Map<String, Object>) fromForm).get("person");
            int id = (int) person.get("id");
            Persons persToEdit = personRepository.findById(id);
            persToEdit.setFirstname(formValues.get("first_name").toString());
            persToEdit.setFamilyname(formValues.get("second_name").toString());
            persToEdit.setMiddlename(formValues.get("middle_name").toString());
            persToEdit.setBirthday(dateParseUtils.stringToSqlDate(formValues.get("birthday").toString()));
            persToEdit.setEmail(formValues.get("email").toString());
            Users userToEdit = userRepository.findById(persToEdit.getUserid());
            userToEdit.setUsername(formValues.get("username").toString());
            List<String> roles = (List<String>) formValues.get("roles");
            Set<Roles> rolesToSet = new HashSet<>();
            for (String role : roles) {
                Roles roleToAdd = rolesRepository.findByName(role);
                rolesToSet.add(roleToAdd);
            }
            userToEdit.setRoles(rolesToSet);
            personRepository.save(persToEdit);
        }
        return formValues;
    }

    @PostMapping(value = "changePass")
    public Map<String, Object> changePass(@RequestBody Map<String, Object> userToEdit) {
        if (!userToEdit.isEmpty()) {
            Object pers = userToEdit.get("person");
            String newPass = userToEdit.get("pass").toString();
            try {
                Map<String, Object> p = (Map<String, Object>) pers;
                int id = (int) p.get("id");
                Persons person = personRepository.findByUserid(id);
                Map<String, Object> profile = new HashMap<>();
                profile.put("first_name", person.getFirstname());
                profile.put("second_name", person.getFamilyname());
                profile.put("middle_name", person.getMiddlename());
                profile.put("birthday", person.getBirthday());
                profile.put("email", person.getEmail());
                Users user = userRepository.findById(person.getUserid());
                user.setPassword(bCryptPasswordEncoder.encode(newPass));
                userRepository.save(user);
                profile.put("username", user.getUsername());
                List<String> userRoles = new ArrayList<>();
                for (Roles role : user.getRoles()) {
                    userRoles.add(role.getName());
                }
                List<Roles> rolesList = rolesRepository.findAll();
                List<String> roles = new ArrayList<>();
                for (Roles role : rolesList) {
                    roles.add(role.getName());
                }
                Map<String, Object> rolesMap = new HashMap<>();
                rolesMap.put("roles", roles);
                rolesMap.put("profile", profile);
                rolesMap.put("userRoles", userRoles);
                return rolesMap;

            } catch (Exception e) {
                java.lang.System.out.println("Error while getting person data " + e.getLocalizedMessage());
            }
        }
        Map<String, Object> editedUser = new HashMap<>();
        return editedUser;
    }

    @PostMapping(value = "newuser")
    public Map<String, Object> newUser(@RequestBody Map<String, Object> formValues) {
        if (null != userRepository.findByUsername(formValues.get("username").toString())) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", "user already exist");
            return errorResult;
        }
        Users user = new Users();
        user.setUsername(formValues.get("username").toString());
        user.setPassword(bCryptPasswordEncoder.encode(formValues.get("password").toString()));
        List<String> roles = (List<String>) formValues.get("roles");
        Set<Roles> rolesToSet = new HashSet<>();
        for (String role : roles) {
            Roles roleToAdd = rolesRepository.findByName(role);
            rolesToSet.add(roleToAdd);
        }
        user.setRoles(rolesToSet);
        userRepository.save(user);
        Persons person = new Persons();
        person.setUserid(userRepository.findByUsername(user.getUsername()).getId());
        person.setFirstname(null != formValues.get("first_name") ? formValues.get("first_name").toString() : "");
        person.setFamilyname(null != formValues.get("second_name") ? formValues.get("second_name").toString() : "");
        person.setMiddlename(null != formValues.get("middle_name") ? formValues.get("middle_name").toString() : "");
        Date formBirthDate = dateParseUtils.stringToSqlDate(formValues.get("birthday"));
        if (formBirthDate != null) {
            person.setBirthday(formBirthDate);
        }
        person.setEmail(null != formValues.get("email") ? formValues.get("email").toString() : "");
        personRepository.save(person);

        Map<String, Object> newUser = new HashMap<>();
        return newUser;
    }

    @PostMapping(value = "roles")
    public Map<String, Object> roles() {
        List<Roles> rolesList = rolesRepository.findAll();
        List<String> roles = new ArrayList<>();
        for (Roles role : rolesList) {
            roles.add(role.getName());
        }
        Map<String, Object> rolesMap = new HashMap<>();
        rolesMap.put("roles", roles);
        return rolesMap;
    }

    @PostMapping(value = "delete")
    public Map<String, Object> deletePersons(@RequestBody Map<String, Object> data) {

        try {
            List<Object> list = (List<Object>) data.get("selected");
            for (Object obj : list) {
                Map<String, Object> val = (Map<String, Object>) obj;
                Integer id = (Integer) val.get("id");
                personRepository.deleteById(id);
                Users user = userRepository.findByUsername(val.get("name").toString());
                userRepository.deleteById(user.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sortedTable(data);
    }

    @PostMapping("generate")
    public Map<String, Object> generateUsers(@RequestBody Map<String, Object> data) {
        int amount = Integer.parseInt(data.get("amount").toString());
        for (int i = 0; i < amount; i++) {
            String userName = generateAlphabeticRandom("username");
            while (userRepository.findByUsername(userName) != null) {
                userName = generateAlphabeticRandom("username");
            }
            String firstName = generateAlphabeticRandom("firstName");
            String familyName = generateAlphabeticRandom("familyName");
            String middleName = generateAlphabeticRandom("middleName");
            Set<Roles> roles = generateRoles();
            String pass = generateAlphaNumericRandom();

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
            // TODO set birthdate and email
            personRepository.save(person);
        }

        return sortedTable(data);
    }

    Map<String, Object> sortedTable(Map<String, Object> data){
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
//        Long personID = Long.parseLong( data.get("id").toString(), 10 );
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

    String generateAlphabeticRandom(String target) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String rand = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        if (target.equals("username")) {
            return rand;
        } else {
            return rand.substring(0, 1).toUpperCase() + rand.substring(1);
        }
    }

    String generateAlphaNumericRandom() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    Set<Roles> generateRoles() {
        List<Roles> roles = rolesRepository.findAll();
        int amountOfRoles = roles.size();
        Random random = new Random();
        int timesToRemove = random.nextInt(amountOfRoles - 1);
        while (timesToRemove > 0) {
            try {
                roles.remove(random.nextInt(amountOfRoles - 1));
            } catch (Exception e) {
                java.lang.System.out.println(e.getMessage());
            }
            timesToRemove--;
        }
        return new HashSet<>(roles);
    }
}
