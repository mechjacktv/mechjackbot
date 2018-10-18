package com.mechjacktv.mechjackbot.chatbot.command;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.chatbot.command.cooldown.GlobalCoolDown;

import javax.inject.Inject;

public class TestCommand implements Command {

    private final CommandUtils commandUtils;

    @Inject
    public TestCommand(final CommandUtils commandUtils) {
        this.commandUtils = commandUtils;
    }

    @Override
    public boolean isHandledMessage(MessageEvent messageEvent) {
        return commandUtils.isCommandTrigger("!test") && this.commandUtils.channelOwner(messageEvent);
    }

    @Override
    @GlobalCoolDown
    public void handleMessage(final MessageEvent messageEvent) {
        messageEvent.respond("No cool down active");
    }
}
