package com.mechjacktv.mechjackbot.command.shoutout;

import com.mechjacktv.mechjackbot.GlobalCoolDown;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.RestrictToPrivileged;
import com.mechjacktv.mechjackbot.command.AbstractCommand;
import com.mechjacktv.mechjackbot.command.CommandUtils;

import javax.inject.Inject;

@SuppressWarnings("CanBeFinal")
public class DelCasterCommand extends AbstractCommand {

  private final CommandUtils commandUtils;
  private final ShoutOutService shoutOutService;

  @Inject
  public DelCasterCommand(final ShoutOutService shoutOutService, final CommandUtils commandUtils) {
    super("!delcaster", commandUtils);
    this.shoutOutService = shoutOutService;
    this.commandUtils = commandUtils;
  }

  @Override
  public final String getDescription() {
    return "Removes a caster from the caster list.";
  }

  @Override
  @RestrictToPrivileged
  @GlobalCoolDown
  public void handleMessage(MessageEvent messageEvent) {
    final String message = messageEvent.getMessage();
    final String[] messageParts = message.split("\\s+");

    if (messageParts.length == 2) {
      final String casterName = this.commandUtils.sanitizeViewerName(messageParts[1]);

      this.shoutOutService.removeCaster(casterName);
      messageEvent.sendResponse(String.format("Removed @%s from casters list", casterName));
    } else {
      this.commandUtils.sendUsage(messageEvent, String.format("%s <casterName>", this.getTrigger()));
    }
  }

}
