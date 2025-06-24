package com.maru.tools.email.service;


import com.maru.tools.email.MailSender;
import com.maru.tools.email.domain.Email;
import com.maru.tools.email.domain.EmailDto;
import com.maru.tools.email.repository.EmailRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final Validator validator;
    private final EmailRepository emailRepository;
    private final MailSender mailSender;

    public Boolean validEmailAddress(String emailAddress) {
        EmailDto email = new EmailDto(emailAddress);
        Set<ConstraintViolation<EmailDto>> violations = validator.validate(email);
        return violations.isEmpty();
    }

    @Description("기존에 저정한 이메일 주소가 있으면 신규주소로 update. 없다면 insert")
    public Email setRecipientMailService(SlashCommandInteractionEvent event){
        Optional<Email> existingEmailInfo = emailRepository.findByDiscordId(event.getMember().getId());

        if(existingEmailInfo.isPresent()){
            Email email = existingEmailInfo.get();
            String emailAddress = Objects.requireNonNull(event.getOption("email_address")).getAsString();

            email.setEmail(emailAddress);

            return emailRepository.save(email);
        } else{
            return emailRepository.save(new Email(event));
        }
    }


    public void sendMailWithContent(Workbook sheets) {
        List<MultipartFile> files = new ArrayList<>();
        files.add((MultipartFile) sheets);
        // WorkBook을 어떻게 MultiPartFiles로 바꿀것이냐. GPT에게 답변 받아둔거 참고
    }
}
