package com.mechjacktv.mechjackbot.chatbot.command;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.chatbot.command.cooldown.GlobalCoolDown;
import com.mechjacktv.mechjackbot.chatbot.command.restrict.RestrictToPrivileged;

import javax.inject.Inject;

public class PingCommand implements Command {

    private final CommandUtils commandUtils;

    @Inject
    public PingCommand(final CommandUtils commandUtils) {
        this.commandUtils = commandUtils;
    }

    @Override
    public final String getCommandTrigger() {
        return "!ping";
    }

    @Override
    public final boolean isHandledMessage(MessageEvent messageEvent) {
        return this.commandUtils.isCommandTrigger(getCommandTrigger(), messageEvent);
    }

    @Override
    @RestrictToPrivileged
    @GlobalCoolDown
    public void handleMessage(MessageEvent messageEvent) {
        messageEvent.sendResponse(String.format("Don't worry, %s. I'm here.",
                this.commandUtils.getSanitizedViewerName(messageEvent)));
    }
}
