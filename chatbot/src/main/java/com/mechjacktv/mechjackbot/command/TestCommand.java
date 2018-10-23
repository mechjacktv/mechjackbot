package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.GlobalCoolDown;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.RestrictToOwner;
import com.mechjacktv.twitchclient.GetUsersEndpoint;
import com.mechjacktv.twitchclient.TwitchClient;
import com.mechjacktv.twitchclient.TwitchClientMessage;

import javax.inject.Inject;
import java.util.Optional;

@SuppressWarnings("CanBeFinal")
public class TestCommand extends AbstractCommand {

    private final String channelName;
    private final TwitchClient twitchClient;

    @Inject
    public TestCommand(final ChatBotConfiguration chatBotConfiguration, final CommandUtils commandUtils, final TwitchClient twitchClient) {
        super("!test", commandUtils);
        this.channelName = chatBotConfiguration.getTwitchChannel();
        this.twitchClient = twitchClient;
    }

    @Override
    public final String getDescription() {
        return "A command I change freely to test development.";
    }

    @Override
    public boolean isTriggerable() {
        return false;
    }

    @Override
    @RestrictToOwner
    @GlobalCoolDown
    public void handleMessage(final MessageEvent messageEvent) {
        final Optional<String> login = this.twitchClient.getUserId(this.channelName);

        messageEvent.sendResponse(String.format("@%s's id is %s", this.channelName, login.orElse("missing")));
        messageEvent.getChatBot().stop();
    }

}
