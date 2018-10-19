package com.mechjacktv.mechjackbot.chatbot.command;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;

import javax.inject.Inject;

public class TestCommand  extends AbstractCommand {

    private final CommandUtils commandUtils;

    @Inject
    public TestCommand(final CommandUtils commandUtils) {
        super("!test", commandUtils);
        this.commandUtils = commandUtils;
    }

    @Override
    public String getDescription() {
        return "A command I change freely to test development.";
    }

    @Override
    @RestrictToOwner
    @GlobalCoolDown
    public void handleMessage(final MessageEvent messageEvent) {
        messageEvent.sendResponse(String.format("@%s, your test completed successfully.",
                this.commandUtils.getSanitizedViewerName(messageEvent)));
    }

}
