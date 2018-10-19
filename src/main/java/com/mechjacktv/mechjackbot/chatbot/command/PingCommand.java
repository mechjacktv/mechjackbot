package com.mechjacktv.mechjackbot.chatbot.command;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;

import javax.inject.Inject;

public class PingCommand extends AbstractCommand {

    private final CommandUtils commandUtils;

    @Inject
    public PingCommand(final CommandUtils commandUtils) {
        super("!ping", commandUtils);
        this.commandUtils = commandUtils;
    }

    @Override
    public String getDecription() {
        return "A simple check to see if the chatbot is running.";
    }

    @Override
    @RestrictToPrivileged
    @GlobalCoolDown
    public void handleMessage(MessageEvent messageEvent) {
        messageEvent.sendResponse(String.format("Don't worry, @%s. I'm here.",
                this.commandUtils.getSanitizedViewerName(messageEvent)));
    }

}
