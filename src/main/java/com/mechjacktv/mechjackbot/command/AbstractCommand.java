package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.*;

public abstract class AbstractCommand implements Command {

  private final AppConfiguration appConfiguration;
  private final CommandDescription commandDescription;
  private final CommandTriggerKey commandTriggerKey;
  private final CommandTrigger commandTriggerDefault;
  private final CommandUtils commandUtils;
  private final boolean viewerTriggerable;

  protected AbstractCommand(final AppConfiguration appConfiguration, final CommandDescription commandDescription,
      final CommandTriggerKey commandTriggerKey, final CommandTrigger commandTriggerDefault,
      final CommandUtils commandUtils) {
    this(appConfiguration, commandDescription, commandTriggerKey, commandTriggerDefault, commandUtils, true);
  }

  protected AbstractCommand(final AppConfiguration appConfiguration, final CommandDescription commandDescription,
      final CommandTriggerKey commandTriggerKey, final CommandTrigger commandTriggerDefault,
      final CommandUtils commandUtils, final boolean viewerTriggerable) {
    this.appConfiguration = appConfiguration;
    this.commandDescription = commandDescription;
    this.commandTriggerKey = commandTriggerKey;
    this.commandTriggerDefault = commandTriggerDefault;
    this.commandUtils = commandUtils;
    this.viewerTriggerable = viewerTriggerable;
  }

  @Override
  public CommandName getName() {
    return CommandName.of(this.getClass().getCanonicalName());
  }

  @Override
  public CommandDescription getDescription() {
    return this.commandDescription;
  }

  @Override
  public boolean isViewerTriggerable() {
    return this.viewerTriggerable;
  }

  @Override
  public final CommandTrigger getTrigger() {
    return CommandTrigger.of(this.appConfiguration.get(this.commandTriggerKey.value, this.commandTriggerDefault.value));
  }

  @Override
  public boolean isTriggered(MessageEvent messageEvent) {
    return this.commandUtils.isCommandTrigger(this.getTrigger(), messageEvent);
  }

}
