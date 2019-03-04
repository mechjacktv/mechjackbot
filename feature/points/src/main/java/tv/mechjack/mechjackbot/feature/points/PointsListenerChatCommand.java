package tv.mechjack.mechjackbot.feature.points;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.mechjackbot.api.BaseChatCommand;
import tv.mechjack.mechjackbot.api.ChatBotConfiguration;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.RequiresAccessLevel;
import tv.mechjack.mechjackbot.api.UserRole;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.configuration.ConfigurationKey;
import tv.mechjack.platform.utils.TimeUtils;

public final class PointsListenerChatCommand extends BaseChatCommand {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(PointsListenerChatCommand.class);

  public static final String DEFAULT_DESCRIPTION = "Monitor chat for messages and award viewers points if enough time has "
      + "passed since last reward.";
  public static final String DEFAULT_FREQUENCY_MINUTES = "10";
  public static final String KEY_FREQUENCY_MINUTES = "frequency.minutes";

  private final ChatBotConfiguration chatBotConfiguration;
  private final Configuration configuration;
  private final ConfigurationKey keyFrequencyMinutes;
  private final TimeUtils timeUtils;
  private long pointsLastAwarded;

  @Inject
  PointsListenerChatCommand(
      final CommandConfigurationBuilder commandConfigurationBuilder,
      final ChatBotConfiguration chatBotConfiguration,
      final Configuration configuration, final TimeUtils timeUtils) {
    super(commandConfigurationBuilder.setDescription(DEFAULT_DESCRIPTION));
    this.chatBotConfiguration = chatBotConfiguration;
    this.configuration = configuration;
    this.keyFrequencyMinutes = ConfigurationKey.of(KEY_FREQUENCY_MINUTES,
        PointsListenerChatCommand.class);
    this.timeUtils = timeUtils;
  }

  @Override
  public final boolean isTriggered(final ChatMessageEvent chatMessageEvent) {
    // BLOCKER (2019-03-03 mechjack): should be minutes not seconds
    return this.timeUtils.currentTime() >= this.pointsLastAwarded
        + TimeUnit.SECONDS.toMillis(
            Integer.parseInt(this.configuration.get(this.keyFrequencyMinutes,
                DEFAULT_FREQUENCY_MINUTES)));
  }

  // https://tmi.twitch.tv/group/user/mechjack/chatters

  @Override
  @RequiresAccessLevel(UserRole.MODERATOR)
  public void handleMessageEvent(ChatMessageEvent chatMessageEvent) {
    try {
      // TODO (2019-03-03 mechjack): find official way to get this information
      final String uri = "https://tmi.twitch.tv/group/user/"
          + this.chatBotConfiguration.getChatChannelName() +
          "/chatters";
      final HttpClient httpClient = HttpClient.newHttpClient();
      final HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(uri)).build();
      final HttpResponse<String> httpResponse = httpClient.send(httpRequest,
          BodyHandlers.ofString());

      LOGGER.info(httpResponse.body());
    } catch (final IOException | InterruptedException e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

}
