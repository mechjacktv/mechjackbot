package com.mechjacktv.mechjackbot.command;

import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.util.ExecutionUtils;

public abstract class BaseChatCommand implements ChatCommand {

  public static final String KEY_DESCRIPTION = "description";
  public static final String KEY_MESSAGE_FORMAT = "message_format";
  public static final String KEY_TRIGGER = "trigger";

  private final Configuration configuration;
  private final ChatCommandUtils chatCommandUtils;
  private final ExecutionUtils executionUtils;
  private final ChatCommandDescription descriptionDefault;
  private final ConfigurationKey descriptionKey;
  private final CommandMessageFormat messageFormatDefault;
  private final ConfigurationKey messageFormatKey;
  private final ChatCommandTrigger triggerDefault;
  private final ConfigurationKey triggerKey;
  private final ChatCommandUsage usage;
  private final boolean triggerable;

  protected BaseChatCommand(CommandConfigurationBuilder commandConfigurationBuilder) {
    this(commandConfigurationBuilder.build());
  }

  protected BaseChatCommand(CommandConfiguration commandConfiguration) {
    this.chatCommandUtils = commandConfiguration.getChatCommandUtils();
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
  public ChatCommandName getName() {
    return ChatCommandName.of(this.getClass().getCanonicalName());
  }

  @Override
  public ChatCommandDescription getDescription() {
    return ChatCommandDescription.of(this.configuration.get(this.descriptionKey.value, this.descriptionDefault.value));
  }

  @Override
  public ChatCommandUsage getUsage() {
    return this.usage;
  }

  @Override
  public boolean isTriggerable() {
    return this.triggerable;
  }

  @Override
  public final ChatCommandTrigger getTrigger() {
    return ChatCommandTrigger.of(this.configuration.get(this.triggerKey.value, this.triggerDefault.value));
  }

  @Override
  public boolean isTriggered(final ChatMessageEvent chatMessageEvent) {
    return this.chatCommandUtils.isTriggered(this, chatMessageEvent);
  }

  protected final void sendResponse(final ChatMessageEvent chatMessageEvent, Object... args) {
    final CommandMessageFormat messageFormat = CommandMessageFormat.of(
        this.configuration.get(this.messageFormatKey.value, this.messageFormatDefault.value));

    this.sendResponse(chatMessageEvent, messageFormat, args);
  }

  protected final void sendResponse(final ChatMessageEvent chatMessageEvent, final CommandMessageFormat messageFormat,
      final Object... args) {
    Objects.requireNonNull(chatMessageEvent, this.executionUtils.nullMessageForName("chatMessageEvent"));
    Objects.requireNonNull(messageFormat, this.executionUtils.nullMessageForName("messageFormat"));

    chatMessageEvent.sendResponse(ChatMessage.of(String.format(messageFormat.value,
        ArrayUtils.addAll(new Object[] { chatMessageEvent.getChatUser().getTwitchLogin() }, args))));
  }

  protected final void sendUsage(final ChatMessageEvent chatMessageEvent) {
    this.sendResponse(chatMessageEvent, CommandMessageFormat.of(
        this.chatCommandUtils.createUsageMessage(this, chatMessageEvent).value));
  }

}
