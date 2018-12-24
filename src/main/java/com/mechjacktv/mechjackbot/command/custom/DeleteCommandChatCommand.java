package com.mechjacktv.mechjackbot.command.custom;

import javax.inject.Inject;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.mechjackbot.ChatCommandTrigger;
import com.mechjacktv.mechjackbot.ChatCommandUtils;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.mechjackbot.ChatMessageEvent;
import com.mechjacktv.mechjackbot.command.BaseChatCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandMessageFormat;

public class DeleteCommandChatCommand extends BaseChatCommand {

  public static final String DEFAULT_DESCRIPTION = "Remove a custom command.";
  public static final String DEFAULT_MESSAGE_FORMAT = "Command removed, %2$s";
  public static final String DEFAULT_NOT_CUSTOM_COMMAND_MESSAGE_FORMAT = "%s, %s is not a custom command";
  public static final String DEFAULT_TRIGGER = "!delcommand";
  public static final String KEY_NOT_CUSTOM_COMMAND_MESSAGE_FORMAT = "not_custom_command_message_format";
  public static final String USAGE = "<trigger>";

  private final Configuration configuration;
  private final ChatCommandUtils chatCommandUtils;
  private final CustomChatCommandService customChatCommandService;
  private final ConfigurationKey notCustomCommandMessageFormatKey;

  @Inject
  protected DeleteCommandChatCommand(final Configuration configuration,
      final CommandConfigurationBuilder commandConfigurationBuilder,
      final ChatCommandUtils chatCommandUtils, final CustomChatCommandService customChatCommandService) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT)
        .setUsage(USAGE));
    this.configuration = configuration;
    this.chatCommandUtils = chatCommandUtils;
    this.customChatCommandService = customChatCommandService;
    this.notCustomCommandMessageFormatKey = ConfigurationKey.of(KEY_NOT_CUSTOM_COMMAND_MESSAGE_FORMAT, this.getClass());
  }

  @Override
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    final ChatMessage cleanChatMessage = this.chatCommandUtils.stripTriggerFromMessage(this, chatMessageEvent);
    final ChatCommandTrigger trigger = ChatCommandTrigger.of(cleanChatMessage.value);

    if ("".equals(cleanChatMessage.value)) {
      this.sendUsage(chatMessageEvent);
    } else if (this.customChatCommandService.isExistingCustomChatCommand(trigger)) {
      this.customChatCommandService.removeCustomChatCommand(trigger);
      this.sendResponse(chatMessageEvent, trigger);
    } else {
      this.sendResponse(chatMessageEvent, this.getNotCustomCommandMessageFormat(), trigger);
    }
  }

  private CommandMessageFormat getNotCustomCommandMessageFormat() {
    return CommandMessageFormat.of(this.configuration.get(this.notCustomCommandMessageFormatKey,
        DEFAULT_NOT_CUSTOM_COMMAND_MESSAGE_FORMAT));
  }

}
