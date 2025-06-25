package com.maru.tools;


import com.maru.tools.email.MailSender;
import com.maru.tools.email.service.DiscordMessageConverter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeeklyReportProcess {
    private final JDA jda;
    private final DiscordMessageConverter converter;
    private final MailSender mailSender;



    //@Scheduled(cron = "0 0 9 * * THU") // 매주 목요일 10:00
    @Scheduled(cron = "10 * * * * * ")
    public void sendWeeklyReport() {
        System.out.println("스케쥴 실행");
        reportTask();
    }

    public void reportTask(){
        // 등록된 채널 아이디 확인
        String guildId = "1382576573810741321";
        TextChannel channel = jda.getTextChannelById(guildId);

        // 최근7일동안 입력한 메시지 내역 확인.
        OffsetDateTime oneWeekAgo = OffsetDateTime.now().minusDays(7);

        channel.getHistory().retrievePast(100).queue(messages -> {
            List<Message> userMessageHistory = messages.stream()
                    .filter(m -> !(m.getAuthor().isBot()))
                    .filter(m -> m.getTimeCreated().isAfter(oneWeekAgo))
                    .sorted(Comparator.comparing(Message::getTimeCreated))
                    .toList();

            // 메시지 -> 메일화
            ByteArrayResource excelSheet = converter.convertMessageToExcel(userMessageHistory);

            Boolean sendResult = mailSender.sendWeeklyReport(excelSheet, guildId);

            channel.sendMessage(sendResult.toString()).queue();
        });
    }

}
