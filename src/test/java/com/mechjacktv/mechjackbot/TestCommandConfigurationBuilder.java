package com.mechjacktv.mechjackbot;

import java.util.Objects;

import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandMessageFormat;

public class TestCommandConfigurationBuilder {

  private final CommandConfigurationBuilder commandConfigurationBuilder;
  private CommandDescription defaultDescription;
  private CommandMessageFormat defaultMessageFormat;
  private CommandTrigger defaultTrigger;
  private CommandUsage usage;

  public TestCommandConfigurationBuilder(final CommandConfigurationBuilder commandConfigurationBuilder) {
    this.commandConfigurationBuilder = commandConfigurationBuilder;
    this.defaultDescription = null;
    this.defaultMessageFormat = null;
    this.defaultTrigger = null;
    this.usage = null;
  }

  public final TestCommandConfigurationBuilder setDefaultDescription(final String defaultDescription) {
    this.defaultDescription = CommandDescription.of(defaultDescription);
    return this;
  }

  public final TestCommandConfigurationBuilder setDefaultMessageFormat(final String defaultMessageFormat) {
    this.defaultMessageFormat = CommandMessageFormat.of(defaultMessageFormat);
    return this;
  }

  public final TestCommandConfigurationBuilder setDefaultTrigger(final String defaultTrigger) {
    this.defaultTrigger = CommandTrigger.of(defaultTrigger);
    return this;
  }

  public final TestCommandConfigurationBuilder setUsage(final String usage) {
    this.usage = CommandUsage.of(usage);
    return this;
  }

  public final TestCommandConfiguration build() {
    final CommandDescription defaultDescription = Objects.isNull(this.defaultDescription)
        ? CommandDescription.of(TestCommand.DEFAULT_DESCRIPTION)
        : this.defaultDescription;
    final CommandMessageFormat defaultMessageFormat = Objects.isNull(this.defaultMessageFormat)
        ? CommandMessageFormat.of(TestCommand.DEFAULT_MESSAGE_FORMAT)
        : this.defaultMessageFormat;
    final CommandTrigger defaultTrigger = Objects.isNull(this.defaultTrigger)
        ? CommandTrigger.of(TestCommand.DEFAULT_TRIGGER)
        : this.defaultTrigger;
    final CommandUsage usage = Objects.isNull(this.usage) ? CommandUsage.of(TestCommand.USAGE) : this.usage;

    return new TestCommandConfiguration(this.commandConfigurationBuilder, defaultDescription, defaultMessageFormat,
        defaultTrigger, usage);
  }

}
