package com.maru.tools.config;

import com.maru.tools.command.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

@Configuration
public class JdaConfig {

    @Value("${discord.bot.token}")
    private String botToken;

    @Bean
    public JDA jda(SlashCommandListener slashCommandListener) throws Exception {
        EnumSet<GatewayIntent> intents = EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_VOICE_STATES);


        JDA jda = JDABuilder.createDefault(botToken)
                .enableIntents(intents)
                .addEventListeners(slashCommandListener) // 이벤트 리스너 주입
                .addEventListeners(new ChattingReaction())
                .build()
                .awaitReady();// 초기화 완료까지 블로킹 (중요)

        slashCommandListener.registerSlashCommands(jda);

        return jda;
    }

}

