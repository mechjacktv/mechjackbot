package com.mechjacktv.mechjackbot.chatbot.command;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;

import javax.inject.Inject;

public final class PingCommand implements Command {

    private final CommandUtils commandUtils;

    @Inject
    public PingCommand(final CommandUtils commandUtils) {
        this.commandUtils = commandUtils;
    }

    @Override
    public final boolean isHandledMessage(MessageEvent messageEvent) {
        return messageEvent.getMessage().startsWith("!ping") && commandUtils.privilegedUser(messageEvent);
    }

    @Override
    public final void handleMessage(MessageEvent messageEvent) {
        if(commandUtils.isCooleddown("!ping")) {
            messageEvent.respond("I'm alive! :P");
        }
    }
}
