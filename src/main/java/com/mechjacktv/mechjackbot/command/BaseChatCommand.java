package com.mechjacktv.mechjackbot.command;

import java.util.Collection;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;

import picocli.CommandLine;
import picocli.CommandLine.IParseResultHandler2;
import picocli.CommandLine.Model.ArgSpec;
import picocli.CommandLine.Model.CommandSpec;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.mechjackbot.ChatCommandDescription;
import com.mechjacktv.mechjackbot.ChatCommandName;
import com.mechjacktv.mechjackbot.ChatCommandTrigger;
import com.mechjacktv.mechjackbot.ChatCommandUsage;
import com.mechjacktv.mechjackbot.ChatCommandUtils;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.mechjackbot.ChatMessageEvent;
import com.mechjacktv.util.ExecutionUtils;

public abstract class BaseChatCommand implements PicoCliCommandParser, RespondingChatCommand {

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

  @Override
  public final void sendResponse(final ChatMessageEvent chatMessageEvent, final Object... args) {
    final CommandMessageFormat messageFormat = CommandMessageFormat.of(
        this.configuration.get(this.messageFormatKey.value, this.messageFormatDefault.value));

    this.sendResponse(chatMessageEvent, messageFormat, args);
  }

  @Override
  public final void sendResponse(final ChatMessageEvent chatMessageEvent, final CommandMessageFormat messageFormat,
      final Object... args) {
    Objects.requireNonNull(chatMessageEvent, this.executionUtils.nullMessageForName("chatMessageEvent"));
    Objects.requireNonNull(messageFormat, this.executionUtils.nullMessageForName("messageFormat"));

    chatMessageEvent.sendResponse(ChatMessage.of(String.format(messageFormat.value,
        ArrayUtils.addAll(new Object[] { chatMessageEvent.getChatUser().getTwitchLogin() }, args))));
  }

  @Override
  public final void sendUsage(final ChatMessageEvent chatMessageEvent) {
    this.sendResponse(chatMessageEvent, CommandMessageFormat.of(
        this.chatCommandUtils.createUsageMessage(this, chatMessageEvent).value));
  }

  @Override
  public boolean parseArguments(final Collection<ArgSpec> argSpecs, final ChatMessageEvent messageEvent,
      final IParseResultHandler2<Boolean> handler) {
    final CommandSpec commandSpec = CommandSpec.create();

    for (final ArgSpec argSpec : argSpecs) {
      commandSpec.add(argSpec);
    }
    return this.parseArguments(commandSpec, messageEvent, handler);
  }

  @Override
  public boolean parseArguments(final CommandSpec commandSpec, final ChatMessageEvent messageEvent,
      final IParseResultHandler2<Boolean> handler) {
    final ChatMessage cleanMessage = this.chatCommandUtils.stripTriggerFromMessage(this, messageEvent);
    final CommandLine commandLine = new CommandLine(commandSpec);

    if ("".equals(cleanMessage.value)) {
      this.sendUsage(messageEvent);
      return false;
    }
    return commandLine.parseWithHandlers(handler, new ShowUsagePicoCliExceptionHandler(this, messageEvent),
        cleanMessage.value.split("\\s+"));
  }

}
