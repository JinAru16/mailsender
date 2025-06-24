package com.maru.tools.email.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class EmailRequest {
    @NotBlank
    private List<String> to;
    private List<String>  carbonCopy;
    @NotBlank
    private String title;
    private String content;

    @Builder
    public EmailRequest(List<String> to, List<String> carbonCopy, String title, String content) {
        this.to = to;
        this.carbonCopy = carbonCopy;
        this.title = title;
        this.content = content;
    }
}
