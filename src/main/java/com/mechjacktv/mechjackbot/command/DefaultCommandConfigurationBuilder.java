package com.mechjacktv.mechjackbot.command;

import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.CommandDescription;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.mechjackbot.CommandUsage;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.util.ExecutionUtils;

public final class DefaultCommandConfigurationBuilder implements CommandConfigurationBuilder {

  public static final String UNSET_DESCRIPTION = "No description set";
  public static final String UNSET_MESSAGE_FORMAT = "No message format set";
  public static final boolean UNSET_TRIGGERABLE = false;
  public static final String UNSET_USAGE = "";

  private final CommandUtils commandUtils;
  private final Configuration configuration;
  private final ExecutionUtils executionUtils;

  private String description;
  private String messageFormat;
  private String trigger;
  private Boolean triggerable;
  private String usage;

  @Inject
  public DefaultCommandConfigurationBuilder(final CommandUtils commandUtils, final Configuration configuration,
      final ExecutionUtils executionUtils) {
    this.commandUtils = commandUtils;
    this.configuration = configuration;
    this.executionUtils = executionUtils;
  }

  @Override
  public CommandConfigurationBuilder setDescription(final String description) {
    this.description = description;
    return this;
  }

  @Override
  public CommandConfigurationBuilder setMessageFormat(final String messageFormat) {
    this.messageFormat = messageFormat;
    return this;
  }

  @Override
  public CommandConfigurationBuilder setTrigger(final String trigger) {
    this.trigger = trigger;
    if (Objects.isNull(this.triggerable)) {
      this.triggerable = true;
    }
    return this;
  }

  @Override
  public CommandConfigurationBuilder setTriggerable(final boolean triggerable) {
    this.triggerable = triggerable;
    return this;
  }

  @Override
  public CommandConfigurationBuilder setUsage(final String usage) {
    this.usage = usage;
    return this;
  }

  @Override
  public CommandConfiguration build() {
    final String description = Objects.isNull(this.description) ? UNSET_DESCRIPTION : this.description;
    final String messageFormat = Objects.isNull(this.messageFormat) ? UNSET_MESSAGE_FORMAT : this.messageFormat;
    final String trigger = Objects.isNull(this.trigger) ? UUID.randomUUID().toString() : this.trigger;
    final boolean triggerable = Objects.isNull(this.triggerable) ? UNSET_TRIGGERABLE : this.triggerable;
    final String usage = Objects.isNull(this.usage) ? UNSET_USAGE : this.usage;

    return new DefaultCommandConfiguration(this.commandUtils, this.configuration, this.executionUtils,
        CommandDescription.of(description), CommandMessageFormat.of(messageFormat), CommandTrigger.of(trigger),
        triggerable, CommandUsage.of(usage));
  }
}
