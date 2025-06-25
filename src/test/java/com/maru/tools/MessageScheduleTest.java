package com.maru.tools;

import net.dv8tion.jda.api.JDA;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MessageScheduleTest {

    @Autowired
    JDA jda;

    @Autowired
    WeeklyReportProcess messageSchedule;
    @Test
    @DisplayName("")
    void callScheduleFixedClock(){
        //given
        messageSchedule.reportTask();
        //when

        //then

    }
}