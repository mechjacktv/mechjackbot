package com.mechjacktv.mechjackbot.command.shoutout;

import com.mechjacktv.mechjackbot.GlobalCoolDown;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.RestrictToPrivileged;
import com.mechjacktv.mechjackbot.command.AbstractCommand;
import com.mechjacktv.mechjackbot.command.CommandUtils;

import javax.inject.Inject;

@SuppressWarnings("CanBeFinal")
public class AddCasterCommand extends AbstractCommand {

  private final CommandUtils commandUtils;
  private final ShoutOutService casterService;

  @Inject
  public AddCasterCommand(final ShoutOutService casterService, final CommandUtils commandUtils) {
    super("!addcaster", commandUtils);
    this.casterService = casterService;
    this.commandUtils = commandUtils;
  }

  @Override
  public final String getDescription() {
    return "Adds a caster to the caster list.";
  }

  @Override
  @RestrictToPrivileged
  @GlobalCoolDown
  public void handleMessage(MessageEvent messageEvent) {
    final String message = messageEvent.getMessage();
    final String[] messageParts = message.split("\\s+");

    if (messageParts.length == 2) {
      final String casterName = this.commandUtils.sanitizeViewerName(messageParts[1]);

      this.casterService.setCaster(casterName, 0);
      messageEvent.sendResponse(String.format("Added @%s to casters list", casterName));
    } else {
      this.commandUtils.sendUsage(messageEvent, String.format("%s <casterName>", this.getTrigger()));
    }
  }

}
