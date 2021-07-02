package com.backvuebato.utils;

import com.backvuebato.entity.Roles;
import com.backvuebato.repository.RolesRepository;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {
    public Date randomSqlDate(){
        String date1 = "01/01/1960";
        String date2 = "01/01/2020";
        try{
            java.util.Date d1 = new SimpleDateFormat("dd/MM/yyyy").parse(date1);
            java.util.Date d2 = new SimpleDateFormat("dd/MM/yyyy").parse(date2);
            return new Date(ThreadLocalRandom.current().nextLong(d1.getTime(), d2.getTime()));
        } catch (Exception e){
            return null;
        }
    }
    public String generateAlphabeticRandom(String target) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String rand = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        switch (target) {
            case "username":
                return rand;
            case "email":
                return rand + "@mail.com";
            default:
                return rand.substring(0, 1).toUpperCase() + rand.substring(1);
        }
    }

    public String generateAlphaNumericRandom() {
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

    public Set<Roles> generateRoles(RolesRepository rolesRepository) {
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
