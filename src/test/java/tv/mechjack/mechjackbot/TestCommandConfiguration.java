package tv.mechjack.mechjackbot;

import tv.mechjack.mechjackbot.command.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.command.CommandMessageFormat;

public final class TestCommandConfiguration {

  private final CommandConfigurationBuilder commandConfigurationBuilder;
  private final ChatCommandDescription defaultDescription;
  private final CommandMessageFormat defaultMessageFormat;
  private final ChatCommandTrigger defaultTrigger;
  private final ChatCommandUsage usage;

  public TestCommandConfiguration(final CommandConfigurationBuilder commandConfigurationBuilder,
      final ChatCommandDescription defaultDescription, final CommandMessageFormat defaultMessageFormat,
      final ChatCommandTrigger defaultTrigger, final ChatCommandUsage usage) {
    this.commandConfigurationBuilder = commandConfigurationBuilder;
    this.commandConfigurationBuilder.setDescription(defaultDescription.value)
        .setMessageFormat(defaultMessageFormat.value)
        .setTrigger(defaultTrigger.value)
        .setUsage(usage.value);
    this.defaultDescription = defaultDescription;
    this.defaultMessageFormat = defaultMessageFormat;
    this.defaultTrigger = defaultTrigger;
    this.usage = usage;
  }

  public final CommandConfigurationBuilder getCommandConfigurationBuilder() {
    return this.commandConfigurationBuilder;
  }

  public final ChatCommandDescription getDefaultDescription() {
    return this.defaultDescription;
  }

  public final CommandMessageFormat getDefaultMessageFormat() {
    return this.defaultMessageFormat;
  }

  public final ChatCommandTrigger getDefaultTrigger() {
    return this.defaultTrigger;
  }

  public final ChatCommandUsage getUsage() {
    return this.usage;
  }

}
