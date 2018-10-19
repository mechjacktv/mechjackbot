package com.mechjacktv.mechjackbot.chatbot.command;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;

public abstract class AbstractCommand implements Command {

    private final String commandTrigger;
    private final CommandUtils commandUtils;

    public AbstractCommand(final String commandTrigger, final CommandUtils commandUtils) {
        this.commandTrigger = commandTrigger;
        this.commandUtils = commandUtils;
    }

    @Override
    public String getCommandTrigger() {
        return this.commandTrigger;
    }

    @Override
    public boolean isHandledMessage(MessageEvent messageEvent) {
        return this.commandUtils.isCommandTrigger(getCommandTrigger(), messageEvent);
    }

    @Override
    public boolean isListed() {
        return true; // List a command by default
    }

}
