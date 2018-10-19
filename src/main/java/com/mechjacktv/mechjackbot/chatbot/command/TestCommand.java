package com.mechjacktv.mechjackbot.chatbot.command;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.chatbot.command.cooldown.GlobalCoolDown;
import com.mechjacktv.mechjackbot.chatbot.command.restrict.RestrictToOwner;

import javax.inject.Inject;

public class TestCommand implements Command {

    private final CommandUtils commandUtils;

    @Inject
    public TestCommand(final CommandUtils commandUtils) {
        this.commandUtils = commandUtils;
    }

    @Override
    public final String getCommandTrigger() {
        return "!test";
    }

    @Override
    public final boolean isHandledMessage(MessageEvent messageEvent) {
        return this.commandUtils.isCommandTrigger(getCommandTrigger(), messageEvent);
    }

    @Override
    @RestrictToOwner
    @GlobalCoolDown
    public void handleMessage(final MessageEvent messageEvent) {
        messageEvent.sendResponse(String.format("%s, your test command completed successfully.",
                this.commandUtils.getSanitizedViewerName(messageEvent)));
    }
}
