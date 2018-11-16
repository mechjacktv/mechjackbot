package com.mechjacktv.mechjackbot.command.shoutout;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.util.TimeUtils;

@SuppressWarnings("CanBeFinal")
public class ShoutOutListenerCommand extends AbstractCommand {

  static final String COMMAND_TRIGGER_KEY = "com.mechjacktv.mechjackbot.command.shoutout.trigger";
  static final String COMMAND_TRIGGER_DEFAULT = "!shoutout";

  static final String COMMAND_FREQUENCY_KEY = "com.mechjacktv.mechjackbot.command.shoutout.frequency.hours";
  static final String COMMAND_FREQUENCY_DEFAULT = "1";

  private final AppConfiguration appConfiguration;
  private final CommandUtils commandUtils;
  private final TimeUtils timeUtils;
  private final ShoutOutDataStore shoutOutDataStore;

  @Inject
  public ShoutOutListenerCommand(final AppConfiguration appConfiguration, final CommandUtils commandUtils,
      final TimeUtils timeUtils, final ShoutOutDataStore shoutOutDataStore) {
    super(new Configuration(appConfiguration, commandUtils,
        CommandDescription.of("Monitors chat looking for casters who are due for a shout out."),
        CommandTriggerKey.of(COMMAND_TRIGGER_KEY), CommandTrigger.of(COMMAND_TRIGGER_DEFAULT)).setTriggerable(false));
    this.appConfiguration = appConfiguration;
    this.commandUtils = commandUtils;
    this.timeUtils = timeUtils;
    this.shoutOutDataStore = shoutOutDataStore;
  }

  @Override
  public final boolean isTriggered(final MessageEvent messageEvent) {
    final Long frequency = this.timeUtils.hoursAsMs(
        Integer.parseInt(this.appConfiguration.get(COMMAND_FREQUENCY_KEY, COMMAND_FREQUENCY_DEFAULT)));
    final ChatUsername chatUsername = this.sanitizeChatUsername(messageEvent);

    return this.shoutOutDataStore.get(this.shoutOutDataStore.createCasterKey(chatUsername.value))
        .filter(caster -> this.timeUtils.currentTime() - caster.getLastShoutOut() > frequency).isPresent();
  }

  @Override
  public void handleMessageEvent(final MessageEvent messageEvent) {
    final ChatUsername chatUsername = this.sanitizeChatUsername(messageEvent);

    messageEvent.sendResponse(Message.of(String.format("Fellow caster in the stream! " +
        "Everyone, please give a warm welcome to @%1$s. " +
        "It would be great if you checked them out " +
        "and gave them a follow. https://twitch.tv/%1$s",
        chatUsername)));
    this.shoutOutDataStore.put(this.shoutOutDataStore.createCasterKey(chatUsername.value),
        this.shoutOutDataStore.createCaster(chatUsername.value, System.currentTimeMillis()));
  }

  private ChatUsername sanitizeChatUsername(final MessageEvent messageEvent) {
    return this.commandUtils.sanitizeChatUsername(messageEvent.getChatUser().getUsername());
  }

}
