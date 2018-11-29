package com.mechjacktv.mechjackbot.command;

import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.SettingKey;
import com.mechjacktv.mechjackbot.*;

public abstract class BaseCommand implements Command {

  public static final String MESSAGE_FORMAT_KEY = "message_format";
  public static final String TRIGGER_KEY = "trigger";

  private final Configuration configuration;
  private final CommandUtils commandUtils;
  private final CommandDescription description;
  private final CommandMessageFormat messageFormatDefault;
  private final SettingKey messageFormatKey;
  private final CommandTrigger triggerDefault;
  private final SettingKey triggerKey;
  private final CommandUsage usage;
  private final boolean triggerable;

  protected BaseCommand(CommandConfigurationBuilder commandConfigurationBuilder) {
    this(commandConfigurationBuilder.build());
  }

  protected BaseCommand(CommandConfiguration commandConfiguration) {
    this.commandUtils = commandConfiguration.getCommandUtils();
    this.configuration = commandConfiguration.getConfiguration();
    this.description = commandConfiguration.getDescription();
    this.messageFormatDefault = commandConfiguration.getMessageFormat();
    this.messageFormatKey = SettingKey.of(this.getClass(), MESSAGE_FORMAT_KEY);
    this.triggerDefault = commandConfiguration.getTrigger();
    this.triggerKey = SettingKey.of(this.getClass(), TRIGGER_KEY);
    this.triggerable = commandConfiguration.isTriggerable();
    this.usage = commandConfiguration.getUsage();
  }

  @Override
  public CommandName getName() {
    return CommandName.of(this.getClass().getCanonicalName());
  }

  @Override
  public CommandDescription getDescription() {
    return this.description;
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

  protected final void sendResponse(final MessageEvent messageEvent, CommandMessageFormat messageFormat,
      Object... args) {
    Objects.requireNonNull(messageEvent, "messageEvent");
    Objects.requireNonNull(messageFormat, "messageFormat");
    if (Objects.nonNull(args)) {
      messageEvent.sendResponse(Message.of(String.format(messageFormat.value,
          ArrayUtils.addAll(new Object[] { messageEvent.getChatUser().getTwitchLogin() }, args))));
    } else {
      messageEvent.sendResponse(Message.of(String.format(messageFormat.value,
          messageEvent.getChatUser().getTwitchLogin())));
    }
  }

  protected final void sendUsage(final MessageEvent messageEvent) {
    this.commandUtils.sendUsage(this, messageEvent);
  }

  // MECHJACK
  protected final SettingKey getTriggerKey() {
    return this.triggerKey;
  }

  protected final CommandTrigger getTriggerDefault() {
    return this.triggerDefault;
  }

}
