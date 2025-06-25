package com.maru.tools.email;

import com.maru.tools.email.domain.Email;
import com.maru.tools.email.domain.EmailRequest;
import com.maru.tools.email.repository.EmailRepository;
import com.maru.tools.exception.BasicExceptionEnum;
import com.maru.tools.exception.CustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MailSender {
    private final JavaMailSender javaMailSender;
    private final EmailRepository emailRepository;
    private final org.springframework.mail.MailSender mailSender;


    public Boolean sendMail(EmailRequest emailRequest, List<MultipartFile> attachments){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 수신, 참조, 첨부파일 세팅.
            setRecipientsAndAttachments(emailRequest, attachments, helper);

            //message.addRecipients(Message.RecipientType.TO, String.valueOf(emailRequest.getTo()));
            helper.setFrom(new InternetAddress("jinaru0131","Admin"));//보내는 사람
            helper.setSubject(emailRequest.getTitle());
            helper.setText(emailRequest.getContent(), true);

            javaMailSender.send(message);
            return true;
        } catch (MessagingException | UnsupportedEncodingException e){
            throw new CustomException(BasicExceptionEnum.INTERNAL_SERVER_ERROR,e.getMessage());
        }

    }

    public Boolean sendWeeklyReport(ByteArrayResource excelSheet, String guildId) throws MessagingException, UnsupportedEncodingException {

        Optional<Email> byGuildId = emailRepository.findByGuildId(guildId);

        if(byGuildId.isPresent()){
            System.out.println(2);
            Email email = byGuildId.get();
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.addAttachment("주간보고.xlsx", excelSheet);

            helper.setFrom(new InternetAddress("jinaru0131","Admin"));//보내는 사람
            helper.setTo(email.getEmail());
            helper.setSubject(email.getTitle());
            helper.setText(email.getContent(), true);

            javaMailSender.send(message);
            return true;


        } else{
            return false;
        }
    }

    // 수신자, 참조자, 첨부파일 세팅함수.
    private static void setRecipientsAndAttachments(EmailRequest emailRequest, List<MultipartFile> attachments, MimeMessageHelper helper) throws MessagingException {
        if (emailRequest.getTo() != null && !emailRequest.getTo().isEmpty()) {
            InternetAddress[] toAddresses = new InternetAddress[emailRequest.getTo().size()];
            for (int i = 0; i < emailRequest.getTo().size(); i++) {
                toAddresses[i] = new InternetAddress(emailRequest.getTo().get(i));
            }
            helper.setTo(toAddresses);
        }

        if (emailRequest.getCarbonCopy() != null && !emailRequest.getCarbonCopy().isEmpty()) {
            InternetAddress[] ccAddresses = new InternetAddress[emailRequest.getCarbonCopy().size()];
            for (int i = 0; i < emailRequest.getCarbonCopy().size(); i++) {
                ccAddresses[i] = new InternetAddress(emailRequest.getCarbonCopy().get(i));
            }
            helper.setCc(ccAddresses);
        }

        // 첨부파일 추가
        if (attachments != null && !attachments.isEmpty()) {
            for (MultipartFile file : attachments) {
                if (file != null && !file.isEmpty()) {
                    helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
                }
            }
        }
    }
}
