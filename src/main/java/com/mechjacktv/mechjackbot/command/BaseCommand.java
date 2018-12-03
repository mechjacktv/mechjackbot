package com.mechjacktv.mechjackbot.command;

import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.util.ExecutionUtils;

public abstract class BaseCommand implements Command {

  public static final String KEY_DESCRIPTION = "description";
  public static final String KEY_MESSAGE_FORMAT = "message_format";
  public static final String KEY_TRIGGER = "trigger";

  private final Configuration configuration;
  private final CommandUtils commandUtils;
  private final ExecutionUtils executionUtils;
  private final CommandDescription descriptionDefault;
  private final ConfigurationKey descriptionKey;
  private final CommandMessageFormat messageFormatDefault;
  private final ConfigurationKey messageFormatKey;
  private final CommandTrigger triggerDefault;
  private final ConfigurationKey triggerKey;
  private final CommandUsage usage;
  private final boolean triggerable;

  protected BaseCommand(CommandConfigurationBuilder commandConfigurationBuilder) {
    this(commandConfigurationBuilder.build());
  }

  protected BaseCommand(CommandConfiguration commandConfiguration) {
    this.commandUtils = commandConfiguration.getCommandUtils();
    this.configuration = commandConfiguration.getConfiguration();
    this.executionUtils = commandConfiguration.getExecutionUtils();
    this.descriptionDefault = commandConfiguration.getDescription();
    this.descriptionKey = ConfigurationKey.of(KEY_DESCRIPTION, this.getClass());
    this.messageFormatDefault = commandConfiguration.getMessageFormat();
    this.messageFormatKey = ConfigurationKey.of(KEY_MESSAGE_FORMAT, this.getClass());
    this.triggerDefault = commandConfiguration.getTrigger();
    this.triggerKey = ConfigurationKey.of(KEY_TRIGGER, this.getClass());
    this.triggerable = commandConfiguration.isTriggerable();
    this.usage = commandConfiguration.getUsage();
  }

  @Override
  public CommandName getName() {
    return CommandName.of(this.getClass().getCanonicalName());
  }

  @Override
  public CommandDescription getDescription() {
    return CommandDescription.of(this.configuration.get(this.descriptionKey.value, this.descriptionDefault.value));
  }

  @Override
  public CommandUsage getUsage() {
    return this.usage;
  }

  @Override
  public boolean isTriggerable() {
    return this.triggerable;
  }

  @Override
  public final CommandTrigger getTrigger() {
    return CommandTrigger.of(this.configuration.get(this.triggerKey.value, this.triggerDefault.value));
  }

  @Override
  public boolean isTriggered(final MessageEvent messageEvent) {
    return this.commandUtils.isTriggered(this, messageEvent);
  }

  protected final void sendResponse(final MessageEvent messageEvent, Object... args) {
    final CommandMessageFormat messageFormat = CommandMessageFormat.of(
        this.configuration.get(this.messageFormatKey.value, this.messageFormatDefault.value));

    this.sendResponse(messageEvent, messageFormat, args);
  }

  protected final void sendResponse(final MessageEvent messageEvent, final CommandMessageFormat messageFormat,
      final Object... args) {
    Objects.requireNonNull(messageEvent, this.executionUtils.nullMessageForName("messageEvent"));
    Objects.requireNonNull(messageFormat, this.executionUtils.nullMessageForName("messageFormat"));

    messageEvent.sendResponse(Message.of(String.format(messageFormat.value,
        ArrayUtils.addAll(new Object[] { messageEvent.getChatUser().getTwitchLogin() }, args))));
  }

  protected final void sendUsage(final MessageEvent messageEvent) {
    this.sendResponse(messageEvent, CommandMessageFormat.of(
        this.commandUtils.createUsageMessage(this, messageEvent).value));
  }

}
