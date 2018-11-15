package com.mechjacktv.mechjackbot.command.shoutout;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.AbstractCommand;
import com.mechjacktv.mechjackbot.command.CommandUtils;

@SuppressWarnings("CanBeFinal")
public class ShoutOutListenerCommand extends AbstractCommand {

  private static final String COMMAND_TRIGGER_KEY = "command.shout_out.shout_out_listener.trigger";
  private static final String COMMAND_TRIGGER_DEFAULT = "!casterListener";

  private final ShoutOutService shoutOutService;
  private final CommandUtils commandUtils;

  @Inject
  public ShoutOutListenerCommand(final AppConfiguration appConfiguration, final ShoutOutService shoutOutService,
      final CommandUtils commandUtils) {
    super(appConfiguration, CommandTriggerKey.of(COMMAND_TRIGGER_KEY), CommandTrigger.of(COMMAND_TRIGGER_DEFAULT),
        commandUtils);
    this.shoutOutService = shoutOutService;
    this.commandUtils = commandUtils;
  }

  @Override
  public final CommandDescription getDescription() {
    return CommandDescription.of("Monitors chat looking for casters who are due for a shout out.");
  }

  @Override
  public void handleMessageEvent(MessageEvent messageEvent) {
    this.shoutOutService.sendCasterShoutOut(messageEvent, this.commandUtils.getSanitizedViewerName(messageEvent));
  }

  @Override
  public boolean isTriggerable() {
    return false;
  }

  @Override
  public final boolean isTriggered(MessageEvent messageEvent) {
    return this.shoutOutService.isCasterDue(this.commandUtils.getSanitizedViewerName(messageEvent));
  }

}
