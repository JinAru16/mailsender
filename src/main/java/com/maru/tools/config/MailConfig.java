package com.maru.tools.config;

import com.maru.tools.email.MailSenderAssembler;
import com.maru.tools.email.domain.MailSecurity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class MailConfig {
    @Value("${smtp.username}")
    private String smtpUsername;

    @Value("${smtp.password}")
    private String smtpPassword;

    @Value("${smtp.port}")
    private String smtpPort;

    @Value("${smtp.host}")
    private String smtpHost;

    @Bean
    public JavaMailSender coreMailSender(){
        MailSenderAssembler mail = MailSenderAssembler.builder()
                .host(smtpHost)
                .username(smtpUsername)
                .password(smtpPassword)
                .port(smtpPort)
                .mailSecurity(MailSecurity.STARTTLS)
                .build();

        return mail.getMailSender();
    }
}
