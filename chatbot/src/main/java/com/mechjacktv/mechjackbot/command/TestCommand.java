package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.GlobalCoolDown;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.RestrictToOwner;
import com.mechjacktv.twitchclient.TwitchClient;
import com.mechjacktv.twitchclient.TwitchClientMessage.UserFollow;
import com.mechjacktv.twitchclient.TwitchClientMessage.UserFollows;

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
  @RestrictToOwner
  @GlobalCoolDown
  public void handleMessage(final MessageEvent messageEvent) {
    final Optional<String> login = this.twitchClient.getUserId(this.channelName);

    if (login.isPresent()) {
      final String fromId = login.get();
      final UserFollows userFollows = this.twitchClient.getUserFollowsFromId(fromId);
      final StringBuilder builder = new StringBuilder("@%s is following: ");

      for (final UserFollow userFollow : userFollows.getUserFollowList()) {
        builder.append(userFollow.getToName());
        builder.append(" ");
      }
      messageEvent.sendResponse(String.format(builder.toString(), this.channelName));
    } else {
      messageEvent.sendResponse(String.format("@%s's id is missing", this.channelName));
    }
    messageEvent.getChatBot().stop();
  }

  @Override
  public boolean isTriggerable() {
    return false;
  }

}
