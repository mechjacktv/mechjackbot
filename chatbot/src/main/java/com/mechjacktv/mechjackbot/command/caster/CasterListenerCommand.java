package com.mechjacktv.mechjackbot.command.caster;

import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.command.AbstractCommand;
import com.mechjacktv.mechjackbot.command.CommandUtils;

import javax.inject.Inject;

@SuppressWarnings("CanBeFinal")
public class CasterListenerCommand extends AbstractCommand {

  private final CasterService casterService;
  private final CommandUtils commandUtils;

  @Inject
  public CasterListenerCommand(final CasterService casterService, final CommandUtils commandUtils) {
    super("!casterListener", commandUtils);
    this.casterService = casterService;
    this.commandUtils = commandUtils;
  }

  @Override
  public final String getDescription() {
    return "Monitors chat looking for casters who are due for a shout out.";
  }

  @Override
  public void handleMessage(MessageEvent messageEvent) {
    final String casterName = this.commandUtils.getSanitizedViewerName(messageEvent);

    this.casterService.sendCasterShoutOut(messageEvent, casterName);
  }

  @Override
  public final boolean isHandledMessage(MessageEvent messageEvent) {
    final String viewerName = this.commandUtils.getSanitizedViewerName(messageEvent);

    return this.casterService.isCasterDue(viewerName);
  }

  @Override
  public boolean isTriggerable() {
    return false; // this is a passive command and can't be called
  }

}
