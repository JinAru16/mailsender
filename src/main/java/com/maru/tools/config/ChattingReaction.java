package com.maru.tools.config;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.OffsetDateTime;

public class ChattingReaction extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        Message message = event.getMessage();
        TextChannel channel = event.getChannel().asTextChannel();

        String str = message.getContentDisplay();

        if(str.equals("ping")){
            event.getChannel().sendMessage("Pong!").queue();
        }

        System.out.println(event.getGuild().getId());

        if(str.equals("history")){
            channel.getHistory()
                    .retrievePast(100) // 최대 100개까지 과거 메시지 조회
                    .queue(messages -> {
                        for (Message msg : messages) {
                            System.out.println(msg.getTimeCreated());
                            System.out.println(msg.getAuthor().getName() + ": " + msg.getContentDisplay());
                        }
                    });
        }
    }
}
