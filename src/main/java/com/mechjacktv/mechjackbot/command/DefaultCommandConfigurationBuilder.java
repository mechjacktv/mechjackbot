package com.mechjacktv.mechjackbot.command;

import java.util.Objects;

import javax.inject.Inject;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.CommandDescription;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.mechjackbot.CommandUsage;
import com.mechjacktv.mechjackbot.CommandUtils;

public final class DefaultCommandConfigurationBuilder implements CommandConfigurationBuilder {

  private final CommandUtils commandUtils;
  private final Configuration configuration;

  private String description;
  private String messageFormat;
  private String trigger;
  private Boolean triggerable;
  private String usage;

  @Inject
  public DefaultCommandConfigurationBuilder(final CommandUtils commandUtils, final Configuration configuration) {
    this.commandUtils = commandUtils;
    this.configuration = configuration;
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
    final String description = Objects.isNull(this.description) ? "No description set" : this.description;
    final String messageFormat = Objects.isNull(this.messageFormat) ? "%s No message format set" : this.messageFormat;
    final String trigger = Objects.isNull(this.trigger) ? "!unknown" : this.trigger;
    final boolean triggerable = Objects.isNull(this.triggerable) ? false : this.triggerable;
    final String usage = Objects.isNull(this.usage) ? "" : this.usage;

    return new DefaultCommandConfiguration(this.commandUtils, this.configuration, CommandDescription.of(description),
        CommandMessageFormat.of(messageFormat), CommandTrigger.of(trigger), triggerable, CommandUsage.of(usage));
  }
}
