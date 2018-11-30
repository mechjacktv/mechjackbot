package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.CommandDescription;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.mechjackbot.CommandUsage;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.util.ExecutionUtils;

public final class DefaultCommandConfiguration implements CommandConfiguration {

  private final CommandUtils commandUtils;
  private final Configuration configuration;
  private final ExecutionUtils executionUtils;

  private final CommandDescription description;
  private final CommandMessageFormat messageFormat;
  private final CommandTrigger trigger;
  private final boolean triggerable;
  private final CommandUsage usage;

  DefaultCommandConfiguration(final CommandUtils commandUtils, final Configuration configuration,
      final ExecutionUtils executionUtils, final CommandDescription description,
      final CommandMessageFormat messageFormat, final CommandTrigger trigger, final boolean triggerable,
      final CommandUsage usage) {
    this.commandUtils = commandUtils;
    this.configuration = configuration;
    this.executionUtils = executionUtils;
    this.description = description;
    this.messageFormat = messageFormat;
    this.trigger = trigger;
    this.triggerable = triggerable;
    this.usage = usage;
  }

  @Override
  public final CommandUtils getCommandUtils() {
    return this.commandUtils;
  }

  @Override
  public final Configuration getConfiguration() {
    return this.configuration;
  }

  @Override
  public final ExecutionUtils getExecutionUtils() {
    return this.executionUtils;
  }

  @Override
  public final CommandDescription getDescription() {
    return this.description;
  }

  @Override
  public final CommandMessageFormat getMessageFormat() {
    return this.messageFormat;
  }

  @Override
  public final CommandTrigger getTrigger() {
    return this.trigger;
  }

  @Override
  public final boolean isTriggerable() {
    return this.triggerable;
  }

  @Override
  public final CommandUsage getUsage() {
    return this.usage;
  }

}
