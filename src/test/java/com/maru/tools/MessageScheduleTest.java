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
    @Test
    @DisplayName("")
    void callScheduleFixedClock(){
        //given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysBefore = now.minusDays(7);

        System.out.println(sevenDaysBefore);

        TextChannel channel = jda.getTextChannelById("1382576573810741321");

        channel.getHistory().retrievePast(10).queue();

        //when

        //then

    }
}