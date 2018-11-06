package com.mechjacktv.mechjackbot.command;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.twitchclient.TwitchClient;

@SuppressWarnings("CanBeFinal")
public class TestCommand extends AbstractCommand {

    @Inject
    public TestCommand(final ChatBotConfiguration chatBotConfiguration, final CommandUtils commandUtils, final TwitchClient twitchClient) {
        super(CommandTrigger.of("!test"), commandUtils);
    }

    @Override
    public final CommandDescription getDescription() {
        return CommandDescription.of("A command I change freely to test development.");
    }

    @Override
    @RestrictToOwner
    @GlobalCoolDown
    public void handleMessage(final MessageEvent messageEvent) {
        messageEvent.sendResponse(Message.of("Test run"));
    }

    @Override
    public boolean isTriggerable() {
        return false;
    }

}
