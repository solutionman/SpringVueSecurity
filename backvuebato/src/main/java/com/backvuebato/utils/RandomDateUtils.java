package com.backvuebato.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDateUtils {
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
}
