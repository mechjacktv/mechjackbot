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
    public final boolean handleMessage(MessageEvent messageEvent) {
        if(messageEvent.getMessage().startsWith("!ping") && commandUtils.privilegedUser(messageEvent)) {
            if(commandUtils.isCooleddown("!ping")) {
                messageEvent.respond("I'm alive! :P");
            }
            return true;
        }
        return false;
    }
}
