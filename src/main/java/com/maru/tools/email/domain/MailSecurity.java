package com.maru.tools.email.domain;

import lombok.Getter;

@Getter
public enum MailSecurity {
    SMTP, STARTTLS, SSL;
}
