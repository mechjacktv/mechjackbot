package com.mechjacktv.mechjackbot.command.core;

import java.util.Optional;

import javax.inject.Inject;

import com.google.common.base.Strings;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.BaseChatCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandMessageFormat;

public final class HelpChatCommand extends BaseChatCommand {

  public static final String DEFAULT_DESCRIPTION = "Returns the description for a command.";
  public static final String DEFAULT_MESSAGE_FORMAT = "@$(user), %s -> %s";
  public static final String DEFAULT_MISSING_MESSAGE_FORMAT = "@$(user), I don't see a command triggered by %s.";
  public static final String DEFAULT_TRIGGER = "!help";
  public static final String KEY_MISSING_MESSAGE_FORMAT = "missing_message_format";
  public static final String USAGE = "<commandTrigger>";

  private final ChatCommandRegistry chatCommandRegistry;
  private final ChatCommandUtils chatCommandUtils;
  private final Configuration configuration;
  private final CommandMessageFormat missingMessageFormatDefault;
  private final ConfigurationKey missingMessageFormatKey;

  @Inject
  HelpChatCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
      final ChatCommandRegistry chatCommandRegistry, final ChatCommandUtils chatCommandUtils,
      final Configuration configuration) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT)
        .setUsage(USAGE));
    this.chatCommandRegistry = chatCommandRegistry;
    this.chatCommandUtils = chatCommandUtils;
    this.configuration = configuration;
    this.missingMessageFormatDefault = CommandMessageFormat.of(DEFAULT_MISSING_MESSAGE_FORMAT);
    this.missingMessageFormatKey = ConfigurationKey.of(KEY_MISSING_MESSAGE_FORMAT, this.getClass());
  }

  @Override
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    final ChatMessage chatMessage = this.chatCommandUtils.stripTriggerFromMessage(this, chatMessageEvent);

    if (!Strings.isNullOrEmpty(chatMessage.value)) {
      final ChatCommandTrigger chatCommandTrigger = ChatCommandTrigger.of(chatMessage.value);
      final Optional<ChatCommand> optionalCommand = this.chatCommandRegistry.getCommand(chatCommandTrigger);

      if (optionalCommand.isPresent() && optionalCommand.get().isTriggerable()) {
        final ChatCommand chatCommand = optionalCommand.get();

        this.sendResponse(chatMessageEvent, chatCommand.getTrigger(), chatCommand.getDescription());
      } else {
        this.sendResponse(chatMessageEvent, this.getMissingMessageFormat(), chatCommandTrigger);
      }
    } else {
      this.sendUsage(chatMessageEvent);
    }
  }

  private CommandMessageFormat getMissingMessageFormat() {
    return CommandMessageFormat.of(this.configuration.get(this.missingMessageFormatKey.value,
        this.missingMessageFormatDefault.value));
  }

}
