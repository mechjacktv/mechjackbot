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
    public final boolean handleMessage(final MessageEvent messageEvent) {
        if(messageEvent.getMessage().startsWith(this.commandTrigger) && this.commandUtils.channelOwner(messageEvent)) {
            final ChatBot chatBot = messageEvent.getChatBot();

            messageEvent.respond("<3 Calling it quits <3");
            chatBot.stop();
            return true;
        }
        return false;
    }
}
