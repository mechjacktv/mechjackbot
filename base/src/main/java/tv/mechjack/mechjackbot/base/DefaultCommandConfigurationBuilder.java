package tv.mechjack.mechjackbot.base;

import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatCommandUsage;
import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.CommandConfiguration;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.CommandMessageFormat;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.utils.ExecutionUtils;

public final class DefaultCommandConfigurationBuilder implements CommandConfigurationBuilder {

  public static final String UNSET_DESCRIPTION = "No description set";
  public static final String UNSET_MESSAGE_FORMAT = "No message format set";
  public static final boolean UNSET_TRIGGERABLE = false;
  public static final String UNSET_USAGE = "";

  private final ChatCommandUtils chatCommandUtils;
  private final Configuration configuration;
  private final ExecutionUtils executionUtils;

  private String description;
  private String messageFormat;
  private String trigger;
  private Boolean triggerable;
  private String usage;

  @Inject
  DefaultCommandConfigurationBuilder(final ChatCommandUtils chatCommandUtils, final Configuration configuration,
      final ExecutionUtils executionUtils) {
    this.chatCommandUtils = chatCommandUtils;
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
    return new DefaultCommandConfiguration(this.chatCommandUtils, this.configuration, this.executionUtils,
        ChatCommandDescription.of(this.getDescription()), CommandMessageFormat.of(this.getMessageFormat()),
        ChatCommandTrigger.of(this.getTrigger()), this.isTriggerable(), ChatCommandUsage.of(this.getUsage()));
  }

  private String getDescription() {
    return Objects.isNull(this.description) ? UNSET_DESCRIPTION : this.description;
  }

  private String getMessageFormat() {
    return Objects.isNull(this.messageFormat) ? UNSET_MESSAGE_FORMAT : this.messageFormat;
  }

  private String getTrigger() {
    return Objects.isNull(this.trigger) ? UUID.randomUUID().toString() : this.trigger;
  }

  private boolean isTriggerable() {
    return Objects.isNull(this.triggerable) ? UNSET_TRIGGERABLE : this.triggerable;
  }

  private String getUsage() {
    return Objects.isNull(this.usage) ? UNSET_USAGE : this.usage;
  }

}
