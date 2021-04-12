package com.backvuebato.utils;

import java.sql.Date;

public class DateParseUtils {
    public Date stringToSqlDate(Object date){
        try {
            String dateToString = date.toString();
            Date result = java.sql.Date.valueOf(dateToString);
            return result;
        } catch (Exception e){
            return null;
        }
    }
}
