package com.mechjacktv.mechjackbot.command.shoutout;

import javax.inject.Inject;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.mechjackbot.ChatMessageEvent;
import com.mechjacktv.mechjackbot.command.BaseChatCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.CasterKey;
import com.mechjacktv.twitchclient.TwitchLogin;
import com.mechjacktv.util.TimeUtils;

public final class ShoutOutListenerChatCommand extends BaseChatCommand {

  public static final String DEFAULT_DESCRIPTION = "Monitors chat looking for casters who are due for a shout out.";
  public static final String DEFAULT_FREQUENCY = "1";
  public static final String DEFAULT_MESSAGE_FORMAT = "Fellow streamer in chat! Everyone, please give a warm welcome "
    + "to @$(user). It would be great if you checked them out and gave them a follow. https://twitch.tv/$(user)";
  public static final String KEY_FREQUENCY = "frequency.hours";

  private final Configuration configuration;
  private final ShoutOutDataStore shoutOutDataStore;
  private final TimeUtils timeUtils;
  private ConfigurationKey frequencyKey;

  @Inject
  ShoutOutListenerChatCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
      final Configuration configuration, final ShoutOutDataStore shoutOutDataStore, final TimeUtils timeUtils) {
    super(commandConfigurationBuilder.setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT));
    this.configuration = configuration;
    this.shoutOutDataStore = shoutOutDataStore;
    this.timeUtils = timeUtils;
    this.frequencyKey = ConfigurationKey.of(KEY_FREQUENCY, this.getClass());
  }

  @Override
  public final boolean isTriggered(final ChatMessageEvent chatMessageEvent) {
    return this.isCasterDue(chatMessageEvent);
  }

  private boolean isCasterDue(final ChatMessageEvent chatMessageEvent) {
    final Long frequency = this.timeUtils.hoursAsMs(
        Integer.parseInt(this.configuration.get(this.frequencyKey.value, DEFAULT_FREQUENCY)));
    final TwitchLogin twitchLogin = chatMessageEvent.getChatUser().getTwitchLogin();
    final CasterKey casterKey = this.shoutOutDataStore.createCasterKey(twitchLogin.value);

    return this.shoutOutDataStore.get(casterKey)
        .filter(caster -> this.timeUtils.currentTime() - caster.getLastShoutOut() > frequency).isPresent();
  }

  @Override
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    final TwitchLogin twitchLogin = chatMessageEvent.getChatUser().getTwitchLogin();

    this.sendResponse(chatMessageEvent);
    this.shoutOutDataStore.put(this.shoutOutDataStore.createCasterKey(twitchLogin.value),
        this.shoutOutDataStore.createCaster(twitchLogin.value, this.timeUtils.currentTime()));
  }

}
