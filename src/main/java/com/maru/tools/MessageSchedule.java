package com.maru.tools;


import com.maru.tools.email.MailSender;
import com.maru.tools.email.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageSchedule {
    private final JDA jda;
    private final EmailService emailService;
    private final MailSender mailSender;

    @Value("${report.path}")
    String path;

    //@Scheduled(cron = "0 0 9 * * THU") // 매주 목요일 10:00
    @Scheduled(cron = "10 * * * * * ")
    public void sendWeeklyMessage() {
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
            ByteArrayResource excelSheet = convertMessageToExcel(userMessageHistory);

            Boolean sendResult = mailSender.sendWeeklyReport(excelSheet, guildId);

            channel.sendMessage(sendResult.toString()).queue();
        });
    }

    private ByteArrayResource convertMessageToExcel(List<Message> userMessageHistory) {
        System.out.println("path : " + path);
        // 저장
        try (InputStream template = new FileInputStream(path)) {
            Workbook workbook = new XSSFWorkbook(template);
            Sheet sheet = workbook.getSheetAt(0);

            int startRowIdx = 11;
            int endRowIdx = 26;

            // 노무법인: 전주 / 금주 셀 13~26번째 row
            for(int rowIdx = startRowIdx; rowIdx < endRowIdx; rowIdx++){
                Row  row = sheet.getRow(rowIdx);
                Cell cellCurrentWeek = row.getCell(4); // E열 (금주)
                Cell cellLastWeek = row.getCell(3); // D열 (전주)

                // 기존 금주 → 전주로 복사
                cellLastWeek.setCellValue(cellCurrentWeek.getStringCellValue());
            }

            int rowIdx = startRowIdx;

            for (Message message : userMessageHistory) {
                Row row = sheet.getRow(rowIdx++);
                Cell cellCurrentWeek = row.getCell(4); // E열 (금주)
                cellCurrentWeek.setCellValue(message.getContentDisplay());
                if (rowIdx == endRowIdx){
                    break;
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            try (FileOutputStream fos = new FileOutputStream(path)) {
                fos.write(out.toByteArray());
            }
            return new ByteArrayResource(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("엑셀 저장 실패" + e.getMessage());
        }
    }
}
