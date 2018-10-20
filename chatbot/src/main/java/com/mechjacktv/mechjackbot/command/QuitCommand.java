package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.*;

import javax.inject.Inject;

@SuppressWarnings("CanBeFinal")
public class QuitCommand extends AbstractCommand {

    private static final String COMMAND_TRIGGER = "command.quit.trigger";
    private static final String COMMAND_TRIGGER_DEFAULT = "!quit";

    @Inject
    public QuitCommand(final AppConfiguration appConfiguration, final CommandUtils commandUtils) {
        super(appConfiguration.getProperty(COMMAND_TRIGGER, COMMAND_TRIGGER_DEFAULT), commandUtils);
    }

    @Override
    public final String getDescription() {
        return "Shuts the chat bot down.";
    }

    @Override
    @RestrictToOwner
    public void handleMessage(final MessageEvent messageEvent) {
        final ChatBot chatBot = messageEvent.getChatBot();

        messageEvent.sendResponse("That's all for me, folks");
        chatBot.stop();
    }

}
