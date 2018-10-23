package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;

public abstract class AbstractCommand implements Command {

  private final String commandTrigger;
  private final CommandUtils commandUtils;

  protected AbstractCommand(final String commandTrigger, final CommandUtils commandUtils) {
    this.commandTrigger = commandTrigger;
    this.commandUtils = commandUtils;
  }

  @Override
  public String getName() {
    return this.getClass().getCanonicalName();
  }

  @Override
  public final String getTrigger() {
    return this.commandTrigger;
  }

  @Override
  public boolean isHandledMessage(MessageEvent messageEvent) {
    return this.commandUtils.isCommandTrigger(getTrigger(), messageEvent);
  }

  @Override
  public boolean isTriggerable() {
    return true; // List a command by default
  }

}
