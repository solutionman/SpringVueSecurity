package com.backvuebato.utils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/*
* Convert String (YYYY-MM-DD or dd MMMM yyyy or d MMMM yyyy) to Sql Date (YYYY-MM-DD)
* 2000-01-01 -> 2000-01-01
* 01 января 2000 -> 2000-01-01
* 1 января 2000 -> 2000-01-01
*/
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

    /*
    * convert Sql Date (YYYY-MM-DD) to String (dd MMMM yyyy)
    * 2000-02-02 -> 1 января 2000
    */
    public String sqlDateToString(Date date) {
        try {
            LocalDate localDate = date.toLocalDate();
            String converted = localDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("ru")));
            if (converted.charAt(0) == '0') {
                return converted.substring(1);
            }
            return converted;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
