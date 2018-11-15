package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.*;

public abstract class AbstractCommand implements Command {

  private final AppConfiguration appConfiguration;
  private final CommandTriggerKey commandTriggerKey;
  private final CommandTrigger commandTriggerDefault;
  private final CommandUtils commandUtils;

  protected AbstractCommand(final AppConfiguration appConfiguration, final CommandTriggerKey commandTriggerKey,
      final CommandTrigger commandTriggerDefault, final CommandUtils commandUtils) {
    this.appConfiguration = appConfiguration;
    this.commandTriggerKey = commandTriggerKey;
    this.commandTriggerDefault = commandTriggerDefault;
    this.commandUtils = commandUtils;
  }

  @Override
  public CommandName getName() {
    return CommandName.of(this.sanitizeGuiceFromName(this.getClass().getCanonicalName()));
  }

  private String sanitizeGuiceFromName(final String rawCommandName) {
    if (!rawCommandName.contains("$$Enhancer")) {
      return rawCommandName;
    }
    return rawCommandName.substring(0, rawCommandName.indexOf("$$Enhancer"));
  }

  @Override
  public final CommandTrigger getTrigger() {
    return CommandTrigger.of(this.appConfiguration.get(this.commandTriggerKey.value, this.commandTriggerDefault.value));
  }

  @Override
  public boolean isTriggerable() {
    return true; // List a command by default
  }

  @Override
  public boolean isTriggered(MessageEvent messageEvent) {
    return this.commandUtils.isCommandTrigger(this.getTrigger(), messageEvent);
  }

}
