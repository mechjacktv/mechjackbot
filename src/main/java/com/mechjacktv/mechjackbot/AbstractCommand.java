package com.mechjacktv.mechjackbot;

public abstract class AbstractCommand implements Command {

  private final com.mechjacktv.configuration.Configuration configuration;
  private final CommandUtils commandUtils;
  private final CommandDescription commandDescription;
  private final CommandUsage commandUsage;
  private final CommandTriggerKey commandTriggerKey;
  private final CommandTrigger commandTriggerDefault;
  private final boolean triggerable;

  protected AbstractCommand(final Configuration configuration) {
    this.configuration = configuration.configuration;
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
    return CommandTrigger.of(this.configuration.get(this.commandTriggerKey.value,
        this.commandTriggerDefault.value));
  }

  @Override
  public boolean isTriggered(MessageEvent messageEvent) {
    return this.commandUtils.isTriggered(this, messageEvent);
  }

  protected static final class Configuration {

    private final com.mechjacktv.configuration.Configuration configuration;
    private final CommandUtils commandUtils;
    private final CommandDescription commandDescription;
    private final CommandTriggerKey commandTriggerKey;
    private final CommandTrigger commandTriggerDefault;
    private CommandUsage commandUsage;
    private boolean triggerable;

    public Configuration(final com.mechjacktv.configuration.Configuration configuration,
        final CommandUtils commandUtils,
        final CommandDescription commandDescription, final CommandTriggerKey commandTriggerKey,
        final CommandTrigger commandTriggerDefault) {
      this.configuration = configuration;
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

}
