package com.maru.tools.email;

import com.maru.tools.email.domain.MailSecurity;
import lombok.Builder;
import lombok.Getter;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Getter
public class MailSenderAssembler {

    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    @Builder
    public MailSenderAssembler(String host, String port, String username, String password, MailSecurity mailSecurity) {
        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(port));
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setJavaMailProperties(getMailProperties(mailSecurity));
        mailSender.setDefaultEncoding("UTF-8");
    }

    private Properties getMailProperties(MailSecurity mailSecurity) {
        Properties pt = new Properties();
        switch (mailSecurity) {
            case SMTP:
                pt.put("mail.transport.protocol", "smtp");
                pt.put("mail.smtp.auth", true);
                pt.put("mail.smtp.starttls.enable", false);
                pt.put("mail.debug", false);
                break;
            case STARTTLS:
                pt.put("mail.smtp.auth", false);
                pt.put("mail.smtp.starttls.enable", true);
                pt.put("mail.smtp.starttls.required", true);
                pt.put("mail.smtp.socketFactory.fallback",true);
                pt.put("mail.smtp.ssl.enable", "true"); // SSL 설정의 간편 버전
                break;
            case SSL:
                pt.put("mail.smtp.auth", true);
                pt.put("mail.smtp.starttls.enable", true);
                pt.put("mail.smtp.starttls.required", false);
                pt.put("mail.smtp.socketFactory.fallback",true);
                pt.put("mail.smtp.ssl.enable", "true"); // SSL 설정의 간편 버전
        }
        return pt;
    }



}