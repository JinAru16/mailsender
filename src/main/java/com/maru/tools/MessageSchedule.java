package com.maru.tools;


import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageSchedule {
    private final JDA jda;

    @Scheduled(cron = "0 27 10 * * *") // 매주 목요일 10:00
    public void sendWeeklyMessage() {
        runTest();
    }

    public void runTest(){
        TextChannel channel = jda.getTextChannelById("1382576573810741321");

        if (channel != null) {
            channel.sendMessage("📝 주간 보고서 시간입니다! 메시지를 남겨주세요.").queue();
        }

        LocalDateTime now = LocalDateTime.now();
        now.minusDays(7);
       // channel.getHistoryAfter();
    }
}
