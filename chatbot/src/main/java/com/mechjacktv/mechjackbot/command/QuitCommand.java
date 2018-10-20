package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.RestrictToOwner;

import javax.inject.Inject;

@SuppressWarnings("CanBeFinal")
public class QuitCommand extends AbstractCommand {

    @Inject
    public QuitCommand(final CommandUtils commandUtils) {
        super("!quit", commandUtils);
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
