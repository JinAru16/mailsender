package com.maru.tools.config;

import com.maru.tools.jda.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdaConfig {

    @Value("${discord.bot.token}")
    private String botToken;

    @Bean
    public JDA jda(SlashCommandListener slashCommandListener) throws Exception {
        return JDABuilder.createDefault(botToken)
                .addEventListeners(slashCommandListener) // 이벤트 리스너 주입
                .build()
                .awaitReady(); // 초기화 완료까지 블로킹 (중요)
    }
}

