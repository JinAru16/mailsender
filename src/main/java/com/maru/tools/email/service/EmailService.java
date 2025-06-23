package com.maru.tools.email.service;


import com.maru.tools.email.domain.Email;
import com.maru.tools.email.domain.EmailDto;
import com.maru.tools.email.repository.EmailRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final Validator validator;
    private final EmailRepository emailRepository;

    public Boolean validEmailAddress(String emailAddress) {
        EmailDto email = new EmailDto(emailAddress);
        Set<ConstraintViolation<EmailDto>> violations = validator.validate(email);
        return violations.isEmpty();
    }

    public Email setRecipientMailService(SlashCommandInteractionEvent event){
        Optional<Email> byDiscordId = emailRepository.findByDiscordId(event.getMember().getId());

        if
        return emailRepository.save(new Email(event));
    }

}
