package com.mechjacktv.mechjackbot.chatbot.command;

import com.mechjacktv.mechjackbot.*;

import javax.inject.Inject;

public class QuitCommand implements Command {

    private static final String COMMAND_TRIGGER = "command.quit.trigger";
    private static final String COMMAND_TRIGGER_DEFAULT = "quit";

    private final String commandTrigger;
    private final CommandUtils commandUtils;

    @Inject
    public QuitCommand(final AppConfiguration appConfiguration, final CommandUtils commandUtils) {
        this.commandTrigger = "!" + appConfiguration.getProperty(COMMAND_TRIGGER, COMMAND_TRIGGER_DEFAULT);
        this.commandUtils = commandUtils;
    }

    @Override
    public final String getCommandTrigger() {
        return this.commandTrigger;
    }

    @Override
    public final boolean isHandledMessage(MessageEvent messageEvent) {
        return this.commandUtils.isCommandTrigger(getCommandTrigger(), messageEvent);
    }

    @Override
    @RestrictToOwner
    public void handleMessage(final MessageEvent messageEvent) {
        final ChatBot chatBot = messageEvent.getChatBot();

        messageEvent.sendResponse("That's all for me, folks");
        chatBot.stop();
    }
}
