package com.backvuebato.backvuebato.utils;

import com.backvuebato.utils.DateParseUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;

@DisplayName("Testing date parsing utils")
class DateParseUtilsTest {
    DateParseUtils dateParseUtils = new DateParseUtils();

    @DisplayName("Testing method string to sql")
    @Test
    void stringToSqlDateTest() {
        String date = "2000-01-01";
        Date formatted = dateParseUtils.stringToSqlDate(date);
        Assertions.assertEquals(date, formatted.toString());

        String date2 = "01 января 2000";
        Date formatted2 = dateParseUtils.stringToSqlDate(date2);
        Assertions.assertEquals(date, formatted2.toString());
    }

    @DisplayName("Testing sql to String")
    @Test
    void sqlDateToStringTest(){
        Date date = Date.valueOf("1999-01-01");
        String formatted = dateParseUtils.sqlDateToString(date);
        Assertions.assertEquals("01 января 1999", formatted);
    }
}
