package com.mechjacktv.mechjackbot.command.shoutout;

import javax.inject.Inject;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.SettingKey;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.command.BaseCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.CasterKey;
import com.mechjacktv.twitchclient.TwitchLogin;
import com.mechjacktv.util.TimeUtils;

@SuppressWarnings("CanBeFinal")
public class ShoutOutListenerCommand extends BaseCommand {

  public static final String FREQUENCY_DEFAULT = "1";
  public static final String FREQUENCY_KEY = "frequency.hours";
  public static final String MESSAGE_FORMAT_DEFAULT = "Fellow streamer in the stream! Everyone, please give a warm "
      + "welcome to @%1$s. It would be great if you checked them out and gave them a follow. https://twitch.tv/%1$s";
  public static final String TRIGGER_DEFAULT = "!shoutout";

  private final Configuration configuration;
  private final ShoutOutDataStore shoutOutDataStore;
  private final TimeUtils timeUtils;
  private SettingKey frequencyKey;

  @Inject
  protected ShoutOutListenerCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
      final Configuration configuration, final ShoutOutDataStore shoutOutDataStore, final TimeUtils timeUtils) {
    super(commandConfigurationBuilder.setTrigger(TRIGGER_DEFAULT)
        .setDescription("Monitors chat looking for casters who are due for a shout out.")
        .setMessageFormat(MESSAGE_FORMAT_DEFAULT)
        .setTriggerable(false));
    this.configuration = configuration;
    this.shoutOutDataStore = shoutOutDataStore;
    this.timeUtils = timeUtils;
    this.frequencyKey = SettingKey.of(this.getClass(), FREQUENCY_KEY);
  }

  @Override
  public final boolean isTriggered(final MessageEvent messageEvent) {
    return this.isCasterDue(messageEvent);
  }

  private boolean isCasterDue(final MessageEvent messageEvent) {
    final Long frequency = this.timeUtils.hoursAsMs(
        Integer.parseInt(this.configuration.get(this.frequencyKey.value, FREQUENCY_DEFAULT)));
    final TwitchLogin twitchLogin = messageEvent.getChatUser().getTwitchLogin();
    final CasterKey casterKey = this.shoutOutDataStore.createCasterKey(twitchLogin.value);

    return this.shoutOutDataStore.get(casterKey)
        .filter(caster -> this.timeUtils.currentTime() - caster.getLastShoutOut() > frequency).isPresent();
  }

  @Override
  public void handleMessageEvent(final MessageEvent messageEvent) {
    final TwitchLogin twitchLogin = messageEvent.getChatUser().getTwitchLogin();

    this.sendResponse(messageEvent);
    this.shoutOutDataStore.put(this.shoutOutDataStore.createCasterKey(twitchLogin.value),
        this.shoutOutDataStore.createCaster(twitchLogin.value, this.timeUtils.currentTime()));
  }

}
