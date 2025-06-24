package com.maru.tools;


import com.maru.tools.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageSchedule {
    private final JDA jda;
    private final EmailService emailService;

    //@Scheduled(cron = "0 0 10 * * THU") // 매주 목요일 10:00
    @Scheduled(cron = "10 * * * * * ")
    public void sendWeeklyMessage() {
        reportTask();
    }

    public void reportTask(){
        // 등록된 채널 아이디 확인
        TextChannel channel = jda.getTextChannelById("1382576573810741321");

        // 최근7일동안 입력한 메시지 내역 확인.
//        if (channel != null) {
//            channel.sendMessage("📝 주간 보고서 시간입니다! 메시지를 남겨주세요.").queue();
//            return;
//        }

        OffsetDateTime oneWeekAgo = OffsetDateTime.now().minusDays(7);

        channel.getHistory().retrievePast(100).queue(messages -> {
            List<Message> userMessageHistory = messages.stream()
                    .filter(m -> !(m.getAuthor().isBot()))
                    .filter(m -> m.getTimeCreated().isAfter(oneWeekAgo))
                    .sorted(Comparator.comparing(Message::getTimeCreated))
                    .toList();

            // 메시지 -> 메일화
             emailService.convertMessageToEmail(userMessageHistory);
        });
    }

}
