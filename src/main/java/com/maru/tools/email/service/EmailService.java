package com.maru.tools.email.service;


import com.maru.tools.email.MailSender;
import com.maru.tools.email.domain.Email;
import com.maru.tools.email.domain.EmailDto;
import com.maru.tools.email.repository.EmailRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

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
    public Email setRecipien(SlashCommandInteractionEvent event){
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

    public Email setReportTitle(SlashCommandInteractionEvent event) {
        Optional<Email> existingEmailInfo = emailRepository.findByDiscordId(event.getMember().getId());
        if(existingEmailInfo.isPresent()){
            Email email = existingEmailInfo.get();
            String title = Objects.requireNonNull(event.getOption("title")).getAsString();

            email.setTitle(title);
            return emailRepository.save(email);
        } else{
            return emailRepository.save(new Email(event));
        }
    }

    public Email setReportContent(SlashCommandInteractionEvent event) {
        Optional<Email> existingEmailInfo = emailRepository.findByDiscordId(event.getMember().getId());
        if(existingEmailInfo.isPresent()){
            Email email = existingEmailInfo.get();
            String content = Objects.requireNonNull(event.getOption("content")).getAsString();

            email.setTitle(content);
            return emailRepository.save(email);
        } else{
            return emailRepository.save(new Email(event));
        }
    }
}
