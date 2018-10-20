package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.GlobalCoolDown;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.RestrictToOwner;
import com.mechjacktv.twitchclient.TwitchClient;

import javax.inject.Inject;

@SuppressWarnings("CanBeFinal")
public class TestCommand  extends AbstractCommand {

    private final TwitchClient twitchClient;

    @Inject
    public TestCommand(final CommandUtils commandUtils, final TwitchClient twitchClient) {
        super("!test", commandUtils);
        this.twitchClient = twitchClient;
    }

    @Override
    public final String getDescription() {
        return "A command I change freely to test development.";
    }

    @Override
    public boolean isListed() {
        return false;
    }

    @Override
    @RestrictToOwner
    @GlobalCoolDown
    public void handleMessage(final MessageEvent messageEvent) {
            messageEvent.sendResponse(String.format("MechJack's userId is %s",
                    twitchClient.getUserId("mechjack")));
    }

}
