package com.backvuebato.utils;

import java.sql.Date;

public class DateParseUtils {
    public Date stringToSqlDate(String date){
        try {
            Date result = java.sql.Date.valueOf(date);
            return result;
        } catch (Exception e){
            return null;
        }
    }
}
