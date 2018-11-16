package com.mechjacktv.mechjackbot;

public abstract class AbstractCommand implements Command {

  protected static final class Configuration {

    private final AppConfiguration appConfiguration;
    private final CommandUtils commandUtils;
    private final CommandDescription commandDescription;
    private final CommandTriggerKey commandTriggerKey;
    private final CommandTrigger commandTriggerDefault;
    private CommandUsage commandUsage;
    private boolean triggerable;

    public Configuration(final AppConfiguration appConfiguration, final CommandUtils commandUtils,
        final CommandDescription commandDescription, final CommandTriggerKey commandTriggerKey,
        final CommandTrigger commandTriggerDefault) {
      this.appConfiguration = appConfiguration;
      this.commandDescription = commandDescription;
      this.commandTriggerKey = commandTriggerKey;
      this.commandTriggerDefault = commandTriggerDefault;
      this.commandUtils = commandUtils;
      this.commandUsage = CommandUsage.of("");
      this.triggerable = true;
    }

    public Configuration setCommandUsage(final CommandUsage commandUsage) {
      this.commandUsage = commandUsage;
      return this;
    }

    public Configuration setTriggerable(final boolean triggerable) {
      this.triggerable = triggerable;
      return this;
    }

  }

  private final AppConfiguration appConfiguration;
  private final CommandUtils commandUtils;
  private final CommandDescription commandDescription;
  private final CommandUsage commandUsage;
  private final CommandTriggerKey commandTriggerKey;
  private final CommandTrigger commandTriggerDefault;
  private final boolean triggerable;

  protected AbstractCommand(final Configuration configuration) {
    this.appConfiguration = configuration.appConfiguration;
    this.commandUtils = configuration.commandUtils;
    this.commandDescription = configuration.commandDescription;
    this.commandUsage = configuration.commandUsage;
    this.commandTriggerKey = configuration.commandTriggerKey;
    this.commandTriggerDefault = configuration.commandTriggerDefault;
    this.triggerable = configuration.triggerable;
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
  public CommandUsage getUsage() {
    return this.commandUsage;
  }

  @Override
  public boolean isTriggerable() {
    return this.triggerable;
  }

  @Override
  public final CommandTrigger getTrigger() {
    return CommandTrigger.of(this.appConfiguration.get(this.commandTriggerKey.value, this.commandTriggerDefault.value));
  }

  @Override
  public boolean isTriggered(MessageEvent messageEvent) {
    return this.commandUtils.isTriggered(this, messageEvent);
  }

}
