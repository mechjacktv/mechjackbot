package com.mechjacktv.mechjackbot.command.shoutout;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.CasterKey;
import com.mechjacktv.twitchclient.TwitchLogin;
import com.mechjacktv.util.TimeUtils;

@SuppressWarnings("CanBeFinal")
public class ShoutOutListenerCommand extends AbstractCommand {

  static final String COMMAND_TRIGGER_KEY = "com.mechjacktv.mechjackbot.command.shoutout.trigger";
  static final String COMMAND_TRIGGER_DEFAULT = "!shoutout";

  static final String COMMAND_FREQUENCY_KEY = "com.mechjacktv.mechjackbot.command.shoutout.frequency.hours";
  static final String COMMAND_FREQUENCY_DEFAULT = "1";

  static final String COMMAND_MESSAGE_FORMAT_KEY = "com.mechjacktv.mechjackbot.command.shoutout.message_format";
  static final String COMMAND_MESSAGE_FORMAT_DEFAULT = "Fellow caster in the stream! Everyone, please give a warm "
      + "welcome to @%1$s. It would be great if you checked them out and gave them a follow. https://twitch.tv/%1$s";

  private final com.mechjacktv.configuration.Configuration configuration;
  private final TimeUtils timeUtils;
  private final ShoutOutDataStore shoutOutDataStore;

  @Inject
  public ShoutOutListenerCommand(final com.mechjacktv.configuration.Configuration configuration,
      final CommandUtils commandUtils,
      final TimeUtils timeUtils, final ShoutOutDataStore shoutOutDataStore) {
    super(new Configuration(configuration, commandUtils,
        CommandDescription.of("Monitors chat looking for casters who are due for a shout out."),
        CommandTriggerKey.of(COMMAND_TRIGGER_KEY), CommandTrigger.of(COMMAND_TRIGGER_DEFAULT)));
    this.configuration = configuration;
    this.timeUtils = timeUtils;
    this.shoutOutDataStore = shoutOutDataStore;
  }

  @Override
  public final boolean isTriggered(final MessageEvent messageEvent) {
    return super.isTriggered(messageEvent) || this.isCasterDue(messageEvent);
  }

  private boolean isCasterDue(final MessageEvent messageEvent) {
    final Long frequency = this.timeUtils.hoursAsMs(
        Integer.parseInt(this.configuration.get(COMMAND_FREQUENCY_KEY, COMMAND_FREQUENCY_DEFAULT)));
    final TwitchLogin twitchLogin = messageEvent.getChatUser().getTwitchLogin();
    final CasterKey casterKey = this.shoutOutDataStore.createCasterKey(twitchLogin.value);

    return this.shoutOutDataStore.get(casterKey)
        .filter(caster -> this.timeUtils.currentTime() - caster.getLastShoutOut() > frequency).isPresent();
  }

  @Override
  public void handleMessageEvent(final MessageEvent messageEvent) {
    final TwitchLogin twitchLogin = messageEvent.getChatUser().getTwitchLogin();
    final String messageFormat = this.configuration.get(COMMAND_MESSAGE_FORMAT_KEY, COMMAND_MESSAGE_FORMAT_DEFAULT);

    messageEvent.sendResponse(Message.of(String.format(messageFormat, twitchLogin)));
    this.shoutOutDataStore.put(this.shoutOutDataStore.createCasterKey(twitchLogin.value),
        this.shoutOutDataStore.createCaster(twitchLogin.value, this.timeUtils.currentTime()));
  }

}
