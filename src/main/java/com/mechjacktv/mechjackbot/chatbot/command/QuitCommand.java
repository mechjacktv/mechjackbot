package com.mechjacktv.mechjackbot.chatbot.command;

import com.mechjacktv.mechjackbot.*;

import javax.inject.Inject;

public class QuitCommand implements Command {

    private final CommandUtils commandUtils;

    @Inject
    public QuitCommand(final CommandUtils commandUtils) {
        this.commandUtils = commandUtils;
    }

    @Override
    public boolean handleMessage(final MessageEvent messageEvent) {
        if(messageEvent.getMessage().startsWith("!quit") && commandUtils.channelOwner(messageEvent)) {
            final ChatBot chatBot = messageEvent.getChatBot();

            messageEvent.respond("<3 Calling it quits <3");
            chatBot.stop();
            return true;
        }
        return false;
    }
}
