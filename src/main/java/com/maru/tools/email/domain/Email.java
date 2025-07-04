package com.maru.tools.email.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Nullable
    private String email;

    private String discordId;

    private String guildId;

    @Setter
    @Nullable
    private String title;

    @Setter
    @Nullable
    private String content;


    public Email(SlashCommandInteractionEvent event){
        this.email = event.getOption("email_address").getAsString();
        this.discordId = Objects.requireNonNull(event.getMember()).getId();
        this.guildId = Objects.requireNonNull(event.getGuild()).getId();
        this.title = event.getOption("title").getAsString();
        this.content = event.getOption("content").getAsString();
    }
}
