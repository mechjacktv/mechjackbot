package com.mechjacktv.mechjackbot;

import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandMessageFormat;

public final class TestCommandConfiguration {

  private final CommandConfigurationBuilder commandConfigurationBuilder;
  private final CommandDescription defaultDescription;
  private final CommandMessageFormat defaultMessageFormat;
  private final CommandTrigger defaultTrigger;
  private final CommandUsage usage;

  public TestCommandConfiguration(final CommandConfigurationBuilder commandConfigurationBuilder,
      final CommandDescription defaultDescription, final CommandMessageFormat defaultMessageFormat,
      final CommandTrigger defaultTrigger, final CommandUsage usage) {
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

  public final CommandDescription getDefaultDescription() {
    return this.defaultDescription;
  }

  public final CommandMessageFormat getDefaultMessageFormat() {
    return this.defaultMessageFormat;
  }

  public final CommandTrigger getDefaultTrigger() {
    return this.defaultTrigger;
  }

  public final CommandUsage getUsage() {
    return this.usage;
  }

}
