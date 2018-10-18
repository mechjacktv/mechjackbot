package com.mechjacktv.mechjackbot.chatbot.command;

import com.mechjacktv.mechjackbot.*;

import javax.inject.Inject;

public final class QuitCommand implements Command {

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
    public boolean isHandledMessage(MessageEvent messageEvent) {
        return messageEvent.getMessage().startsWith(this.commandTrigger) && this.commandUtils.isChannelOwner(messageEvent);
    }

    @Override
    public final void handleMessage(final MessageEvent messageEvent) {
        final ChatBot chatBot = messageEvent.getChatBot();

        messageEvent.respond("MrDestructoid MrDestructoid ##### Calling it quits. #####  MrDestructoid MrDestructoid");
        chatBot.stop();
    }
}
