package com.mechjacktv.mechjackbot.chatbot.command;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.Cooldown;
import com.mechjacktv.mechjackbot.MessageEvent;

import javax.inject.Inject;

public class PingCommand implements Command {

    private final CommandUtils commandUtils;

    @Inject
    public PingCommand(final CommandUtils commandUtils) {
        this.commandUtils = commandUtils;
    }

    @Override
    @Cooldown
    public boolean handleMessage(MessageEvent messageEvent) {
        if(messageEvent.getMessage().startsWith("!ping") && commandUtils.privilegedUser(messageEvent)) {
            messageEvent.respond("I'm alive! :P");
            return true;
        }
        return false;
    }
}
