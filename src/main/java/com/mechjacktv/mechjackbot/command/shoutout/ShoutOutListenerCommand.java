package com.mechjacktv.mechjackbot.command.shoutout;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.AbstractCommand;

@SuppressWarnings("CanBeFinal")
public class ShoutOutListenerCommand extends AbstractCommand {

  private static final String COMMAND_TRIGGER_KEY = "command.shout_out.shout_out_listener.trigger";
  private static final String COMMAND_TRIGGER_DEFAULT = "!casterListener";

  private final CommandUtils commandUtils;
  private final ShoutOutService shoutOutService;

  @Inject
  public ShoutOutListenerCommand(final AppConfiguration appConfiguration, final CommandUtils commandUtils,
      final ShoutOutService shoutOutService) {
    super(new Configuration(appConfiguration, commandUtils,
        CommandDescription.of("Monitors chat looking for casters who are due for a shout out."),
        CommandTriggerKey.of(COMMAND_TRIGGER_KEY), CommandTrigger.of(COMMAND_TRIGGER_DEFAULT)).setTriggerable(false));
    this.commandUtils = commandUtils;
    this.shoutOutService = shoutOutService;
  }

  @Override
  public final boolean isTriggered(final MessageEvent messageEvent) {
    return this.shoutOutService.isCasterDue(this.commandUtils.sanitizedChatUsername(this, messageEvent));
  }

  @Override
  public void handleMessageEvent(final MessageEvent messageEvent) {
    this.shoutOutService.sendCasterShoutOut(messageEvent, this.commandUtils.sanitizedChatUsername(this, messageEvent));
  }

}
