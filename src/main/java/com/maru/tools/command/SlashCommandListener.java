package com.maru.tools.command;

import com.maru.tools.email.domain.Email;
import com.maru.tools.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.stereotype.Component;


import java.util.Objects;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

@Component
@RequiredArgsConstructor
public class SlashCommandListener extends ListenerAdapter {


    private final EmailService emailService;

    public void registerSlashCommands(JDA jda) {
        Guild guild = jda.getGuildById("1382576573357752394");
        assert guild != null;
        guild.updateCommands()
                .addCommands(
                        Commands.slash("say", "Makes the bot say something")
                                .addOption(STRING, "content", "What to say", true)
                )
                .addCommands(
                        Commands.slash("set_receiver", "Set recipient mail address ")
                                .addOption(STRING, "email_address", "Where to send", true)
                ).addCommands(
                        Commands.slash("set_title", "set mail Title")
                                .addOption(STRING, "title", "set default title", true)
                ).addCommands(
                        Commands.slash("set_content", "set mail content")
                                .addOption(STRING, "content", "set default content", true)
                )

                .queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "say":
                event.reply("Hello from Spring JDA Bot!").queue();
                break;
            case "set_receiver":
                String emailAddress = Objects.requireNonNull(event.getOption("email_address")).getAsString();
                Boolean isValid = emailService.validEmailAddress(emailAddress);
                if(isValid){
                    emailService.setRecipien(event);
                    event.reply(emailAddress + "를 수신자 메일로 설정하였습니다.").queue();
                } else{
                    event.reply("❌ 이메일 형식이 잘못되었습니다.").setEphemeral(true).queue();
                }
                break;
            case "set_title":
                Email email = emailService.setReportTitle(event);
                if(email != null){
                    event.reply(email.getTitle() + "(을)를 기본 제목으로 설정하였습니다.").queue();
                } else{
                    event.reply("❌ 제목 형식이 잘못되었습니다.").setEphemeral(true).queue();
                }
                break;
            case "set_content":
                Email contentSetting = emailService.setReportContent(event);
                if(contentSetting != null){
                    event.reply(contentSetting.getContent() + "(을)를 기본 내용으로 설정하였습니다.").queue();
                } else{
                    event.reply("❌ 내용의 형식이 잘못되었습니다.").setEphemeral(true).queue();
                }
                break;


        }

    }
}