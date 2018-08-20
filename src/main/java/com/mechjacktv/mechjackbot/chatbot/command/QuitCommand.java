package com.mechjacktv.mechjackbot.chatbot.command;

import com.mechjacktv.mechjackbot.*;

import javax.inject.Inject;

public class QuitCommand implements Command {

    private final String botOwner;

    @Inject
    public QuitCommand(final BotConfiguration botConfiguration) {
        this.botOwner = botConfiguration.getChannel().toLowerCase();
    }

    @Override
    public boolean handleMessage(final MessageEvent messageEvent) {
        if(messageEvent.getMessage().startsWith("!quit")) {
            final ChatUser chatUser = messageEvent.getChatUser();
            final String chatUsername = chatUser.getUsername().toLowerCase();

            if(botOwner.equals(chatUsername)) {
                final ChatBot chatBot = messageEvent.getChatBot();

                messageEvent.respond("<3 Calling it quits <3");
                chatBot.stop();
            }
            return true;
        }
        return false;
    }
}
