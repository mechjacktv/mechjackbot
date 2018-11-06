package com.mechjacktv.mechjackbot.command;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;

@SuppressWarnings("CanBeFinal")
public class PingCommand extends AbstractCommand {

    private final CommandUtils commandUtils;

    @Inject
    public PingCommand(final CommandUtils commandUtils) {
        super(CommandTrigger.of("!ping"), commandUtils);
        this.commandUtils = commandUtils;
    }

    @Override
    public final CommandDescription getDescription() {
        return CommandDescription.of("A simple check to see if the chat bot is running.");
    }

    @Override
    @RestrictToPrivileged
    @GlobalCoolDown
    public void handleMessage(MessageEvent messageEvent) {
        messageEvent.sendResponse(Message.of(String.format("Don't worry, @%s. I'm here.",
                this.commandUtils.getSanitizedViewerName(messageEvent))));
    }

}
