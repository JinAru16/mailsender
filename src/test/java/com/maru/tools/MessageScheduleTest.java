package com.maru.tools;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageScheduleTest {

    @Autowired
    JDA jda;

    @Autowired
    MessageSchedule messageSchedule;
    @Test
    @DisplayName("")
    void callScheduleFixedClock(){
        //given
        messageSchedule.reportTask();
        //when

        //then

    }
}