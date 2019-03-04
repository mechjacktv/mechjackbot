package tv.mechjack.mechjackbot.feature.autotrigger;

import javax.inject.Inject;

import tv.mechjack.mechjackbot.api.BaseChatCommand;
import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.CommandMessageFormat;
import tv.mechjack.mechjackbot.api.RequiresAccessLevel;
import tv.mechjack.mechjackbot.api.UserRole;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.configuration.ConfigurationKey;

public class AutoTriggerDeleteChatCommand extends BaseChatCommand {

  public static final String DEFAULT_MESSAGE_FORMAT = "$(user), auto trigger list removed, %s";
  public static final String DEFAULT_DESCRIPTION = "Remove an auto trigger list.";
  public static final String DEFAULT_NOT_AUTO_TRIGGER_LIST_MESSAGE_FORMAT = "$(user), %s is not an auto trigger list";
  public static final String DEFAULT_TRIGGER = "!autodelete";
  public static final ConfigurationKey KEY_NOT_AUTO_TRIGGER_LIST_MESSAGE_FORMAT = ConfigurationKey.of(
      "not_auto_trigger_list_message_format",
      AutoTriggerDeleteChatCommand.class);
  public static final String USAGE = "<name>";

  private final AutoTriggerService autoTriggerService;
  private final ChatCommandUtils chatCommandUtils;
  private final Configuration configuration;

  @Inject
  protected AutoTriggerDeleteChatCommand(
      final CommandConfigurationBuilder commandConfigurationBuilder,
      final AutoTriggerService autoTriggerService,
      final ChatCommandUtils chatCommandUtils,
      final Configuration configuration) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT)
        .setUsage(USAGE));
    this.autoTriggerService = autoTriggerService;
    this.chatCommandUtils = chatCommandUtils;
    this.configuration = configuration;
  }

  @Override
  @RequiresAccessLevel(UserRole.MODERATOR)
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    final ChatMessage cleanChatMessage = this.chatCommandUtils
        .stripTriggerFromMessage(this, chatMessageEvent);
    final ListName listName = ListName.of(cleanChatMessage.value);

    if ("".equals(cleanChatMessage.value)) {
      this.sendUsage(chatMessageEvent);
    } else if (this.autoTriggerService.isExistingAutoTriggerList(listName)) {
      this.autoTriggerService.removeAutoTriggerList(listName);
      this.sendResponse(chatMessageEvent, listName);
    } else {
      this.sendResponse(chatMessageEvent,
          this.getNotAutoTriggerListMessageFormat(), listName);
    }
  }

  private CommandMessageFormat getNotAutoTriggerListMessageFormat() {
    return CommandMessageFormat.of(
        this.configuration.get(KEY_NOT_AUTO_TRIGGER_LIST_MESSAGE_FORMAT,
            DEFAULT_NOT_AUTO_TRIGGER_LIST_MESSAGE_FORMAT));
  }

}
