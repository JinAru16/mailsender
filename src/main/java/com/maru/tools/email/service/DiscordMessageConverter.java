package com.maru.tools.email.service;

import net.dv8tion.jda.api.entities.Message;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

@Service
public class DiscordMessageConverter {
    @Value("${report.path}")
    String path;

    public ByteArrayResource convertMessageToExcel(List<Message> userMessageHistory) {
        // 저장
        try (InputStream template = new FileInputStream(path)) {
            Workbook workbook = new XSSFWorkbook(template);
            Sheet sheet = workbook.getSheetAt(0);

            int startRowIdx = 11;
            int endRowIdx = 26;

            // 노무법인: 전주 / 금주 셀 13~26번째 row
            for(int rowIdx = startRowIdx; rowIdx < endRowIdx; rowIdx++){
                Row row = sheet.getRow(rowIdx);
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
