package com.backvuebato.utils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateParseUtils {
    public Date stringToSqlDate(Object date) {
        try {
            String dateToString = date.toString();
            // YYYY-MM-DD
            if (dateToString.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return java.sql.Date.valueOf(dateToString);
            }
            if (dateToString.charAt(1) == ' ') {
                dateToString = "0" + dateToString;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("ru"));
            LocalDate localDate = LocalDate.parse(dateToString, formatter);
            return java.sql.Date.valueOf(localDate);
        } catch (Exception e) {
            return null;
        }
    }

    public String sqlDateToString(Date date) {
        try {
            LocalDate localDate = date.toLocalDate();
            String converted = localDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("ru")));
            if(converted.charAt(0) == '0'){
                return converted.substring(1);
            }
            return converted;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
