package tv.mechjack.mechjackbot.feature.custom;

import javax.inject.Inject;

import tv.mechjack.mechjackbot.api.BaseChatCommand;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.CommandMessageFormat;
import tv.mechjack.mechjackbot.api.RequiresAccessLevel;
import tv.mechjack.mechjackbot.api.UserRole;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.configuration.ConfigurationKey;

public class DeleteCommandChatCommand extends BaseChatCommand {

  public static final String DEFAULT_DESCRIPTION = "Remove a custom command.";
  public static final String DEFAULT_MESSAGE_FORMAT = "$(user), command removed, %s";
  public static final String DEFAULT_NOT_CUSTOM_COMMAND_MESSAGE_FORMAT = "$(user), %s is not a custom command";
  public static final String DEFAULT_TRIGGER = "!delcommand";
  public static final ConfigurationKey KEY_NOT_CUSTOM_COMMAND_MESSAGE_FORMAT = ConfigurationKey
      .of("not_custom_command_message_format", DeleteCommandChatCommand.class);
  public static final String USAGE = "<trigger>";

  private final Configuration configuration;
  private final ChatCommandUtils chatCommandUtils;
  private final CustomChatCommandService customChatCommandService;

  @Inject
  protected DeleteCommandChatCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
      final Configuration configuration, final ChatCommandUtils chatCommandUtils,
      final CustomChatCommandService customChatCommandService) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT)
        .setUsage(USAGE));
    this.configuration = configuration;
    this.chatCommandUtils = chatCommandUtils;
    this.customChatCommandService = customChatCommandService;
  }

  @Override
  @RequiresAccessLevel(UserRole.MODERATOR)
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
    return CommandMessageFormat.of(this.configuration.get(KEY_NOT_CUSTOM_COMMAND_MESSAGE_FORMAT,
        DEFAULT_NOT_CUSTOM_COMMAND_MESSAGE_FORMAT));
  }

}
