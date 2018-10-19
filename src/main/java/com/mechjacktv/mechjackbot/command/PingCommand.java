package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.GlobalCoolDown;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.RestrictToPrivileged;

import javax.inject.Inject;

public class PingCommand extends AbstractCommand {

    private final CommandUtils commandUtils;

    @Inject
    public PingCommand(final CommandUtils commandUtils) {
        super("!ping", commandUtils);
        this.commandUtils = commandUtils;
    }

    @Override
    public final String getDescription() {
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
