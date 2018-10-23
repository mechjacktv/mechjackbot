package com.mechjacktv.mechjackbot.command.shoutout;

import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.command.AbstractCommand;
import com.mechjacktv.mechjackbot.command.CommandUtils;

import javax.inject.Inject;

@SuppressWarnings("CanBeFinal")
public class ShoutOutListenerCommand extends AbstractCommand {

  private final ShoutOutService shoutOutService;
  private final CommandUtils commandUtils;

  @Inject
  public ShoutOutListenerCommand(final ShoutOutService shoutOutService, final CommandUtils commandUtils) {
    super("!casterListener", commandUtils);
    this.shoutOutService = shoutOutService;
    this.commandUtils = commandUtils;
  }

  @Override
  public final String getDescription() {
    return "Monitors chat looking for casters who are due for a shout out.";
  }

  @Override
  public void handleMessage(MessageEvent messageEvent) {
    this.shoutOutService.sendCasterShoutOut(messageEvent, this.commandUtils.getSanitizedViewerName(messageEvent));
  }

  @Override
  public final boolean isHandledMessage(MessageEvent messageEvent) {
    return this.shoutOutService.isCasterDue(this.commandUtils.getSanitizedViewerName(messageEvent));
  }

  @Override
  public boolean isTriggerable() { return false; }

}
