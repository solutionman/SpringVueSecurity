package com.backvuebato.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateParseUtils {
    public Date stringToSqlDate(Object date){
        try {
            String dateToString = date.toString();

            // YYYY-MM-DD
            if(dateToString.matches("\\d{4}-\\d{2}-\\d{2}")){
                return java.sql.Date.valueOf(dateToString);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("ru"));
            LocalDate localDate = LocalDate.parse(dateToString, formatter);
            Date result = java.sql.Date.valueOf(localDate);

            return result;
        } catch (Exception e){
            return null;
        }
    }
}
