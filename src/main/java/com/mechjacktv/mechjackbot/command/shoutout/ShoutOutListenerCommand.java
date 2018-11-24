package com.mechjacktv.mechjackbot.command.shoutout;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.proto.mechjackbot.command.shoutout.ShoutOutServiceMessage.CasterKey;
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

  private final AppConfiguration appConfiguration;
  private final CommandUtils commandUtils;
  private final TimeUtils timeUtils;
  private final ShoutOutDataStore shoutOutDataStore;

  @Inject
  public ShoutOutListenerCommand(final AppConfiguration appConfiguration, final CommandUtils commandUtils,
      final TimeUtils timeUtils, final ShoutOutDataStore shoutOutDataStore) {
    super(new Configuration(appConfiguration, commandUtils,
        CommandDescription.of("Monitors chat looking for casters who are due for a shout out."),
        CommandTriggerKey.of(COMMAND_TRIGGER_KEY), CommandTrigger.of(COMMAND_TRIGGER_DEFAULT)));
    this.appConfiguration = appConfiguration;
    this.commandUtils = commandUtils;
    this.timeUtils = timeUtils;
    this.shoutOutDataStore = shoutOutDataStore;
  }

  @Override
  public final boolean isTriggered(final MessageEvent messageEvent) {
    return super.isTriggered(messageEvent) || this.isCasterDue(messageEvent);
  }

  private boolean isCasterDue(final MessageEvent messageEvent) {
    final Long frequency = this.timeUtils.hoursAsMs(
        Integer.parseInt(this.appConfiguration.get(COMMAND_FREQUENCY_KEY, COMMAND_FREQUENCY_DEFAULT)));
    final ChatUsername chatUsername = this.sanitizeChatUsername(messageEvent);
    final CasterKey casterKey = this.shoutOutDataStore.createCasterKey(chatUsername.value);

    return this.shoutOutDataStore.get(casterKey)
        .filter(caster -> this.timeUtils.currentTime() - caster.getLastShoutOut() > frequency).isPresent();
  }

  @Override
  public void handleMessageEvent(final MessageEvent messageEvent) {
    final ChatUsername chatUsername = this.sanitizeChatUsername(messageEvent);
    final String messageFormat = this.appConfiguration.get(COMMAND_MESSAGE_FORMAT_KEY, COMMAND_MESSAGE_FORMAT_DEFAULT);

    messageEvent.sendResponse(Message.of(String.format(messageFormat, chatUsername)));
    this.shoutOutDataStore.put(this.shoutOutDataStore.createCasterKey(chatUsername.value),
        this.shoutOutDataStore.createCaster(chatUsername.value, this.timeUtils.currentTime()));
  }

  private ChatUsername sanitizeChatUsername(final MessageEvent messageEvent) {
    return this.commandUtils.sanitizeChatUsername(messageEvent.getChatUser().getUsername());
  }

}
