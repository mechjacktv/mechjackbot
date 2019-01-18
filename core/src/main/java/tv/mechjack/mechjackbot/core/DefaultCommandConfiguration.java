package tv.mechjack.mechjackbot.core;

import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatCommandUsage;
import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.CommandConfiguration;
import tv.mechjack.mechjackbot.api.CommandMessageFormat;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.utils.ExecutionUtils;

public final class DefaultCommandConfiguration implements CommandConfiguration {

  private final ChatCommandUtils chatCommandUtils;
  private final Configuration configuration;
  private final ExecutionUtils executionUtils;

  private final ChatCommandDescription description;
  private final CommandMessageFormat messageFormat;
  private final ChatCommandTrigger trigger;
  private final boolean triggerable;
  private final ChatCommandUsage usage;

  DefaultCommandConfiguration(final ChatCommandUtils chatCommandUtils, final Configuration configuration,
      final ExecutionUtils executionUtils, final ChatCommandDescription description,
      final CommandMessageFormat messageFormat, final ChatCommandTrigger trigger, final boolean triggerable,
      final ChatCommandUsage usage) {
    this.chatCommandUtils = chatCommandUtils;
    this.configuration = configuration;
    this.executionUtils = executionUtils;
    this.description = description;
    this.messageFormat = messageFormat;
    this.trigger = trigger;
    this.triggerable = triggerable;
    this.usage = usage;
  }

  @Override
  public final ChatCommandUtils getChatCommandUtils() {
    return this.chatCommandUtils;
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
  public final ChatCommandDescription getDescription() {
    return this.description;
  }

  @Override
  public final CommandMessageFormat getMessageFormat() {
    return this.messageFormat;
  }

  @Override
  public final ChatCommandTrigger getTrigger() {
    return this.trigger;
  }

  @Override
  public final boolean isTriggerable() {
    return this.triggerable;
  }

  @Override
  public final ChatCommandUsage getUsage() {
    return this.usage;
  }

}
