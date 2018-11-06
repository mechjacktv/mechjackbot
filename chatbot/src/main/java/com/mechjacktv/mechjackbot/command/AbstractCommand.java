package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandName;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.mechjackbot.MessageEvent;

public abstract class AbstractCommand implements Command {

  private final CommandTrigger commandTrigger;
  private final CommandUtils commandUtils;

  protected AbstractCommand(final CommandTrigger commandTrigger, final CommandUtils commandUtils) {
    this.commandTrigger = commandTrigger;
    this.commandUtils = commandUtils;
  }

  @Override
  public CommandName getName() {
    return CommandName.of(this.getClass().getCanonicalName());
  }

  @Override
  public final CommandTrigger getTrigger() {
    return this.commandTrigger;
  }

  @Override
  public boolean isTriggered(MessageEvent messageEvent) {
    return this.commandUtils.isCommandTrigger(this.getTrigger(), messageEvent);
  }

  @Override
  public boolean isTriggerable() {
    return true; // List a command by default
  }

}
