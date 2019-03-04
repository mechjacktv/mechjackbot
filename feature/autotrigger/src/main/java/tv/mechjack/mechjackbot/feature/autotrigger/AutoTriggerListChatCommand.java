package tv.mechjack.mechjackbot.feature.autotrigger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import tv.mechjack.mechjackbot.api.BaseChatCommand;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.RequiresAccessLevel;
import tv.mechjack.mechjackbot.api.UserRole;

public class AutoTriggerListChatCommand extends BaseChatCommand {

  public static final String DEFAULT_MESSAGE_FORMAT = "Auto-Trigger Lists: %s";
  public static final String DEFAULT_DESCRIPTION = "Lists existing auto-trigger lists.";
  public static final String DEFAULT_TRIGGER = "!autolist";

  private final AutoTriggerService autoTriggerService;

  @Inject
  protected AutoTriggerListChatCommand(
      final CommandConfigurationBuilder commandConfigurationBuilder,
      final AutoTriggerService autoTriggerService) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT));
    this.autoTriggerService = autoTriggerService;
  }

  @Override
  @RequiresAccessLevel(UserRole.MODERATOR)
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    final List<String> listNames = new ArrayList<>();

    for (final ListName listName : this.autoTriggerService.getAutoTriggerListNames()) {
      listNames.add(listName.value);
    }
    this.sendResponse(chatMessageEvent, String.join(", ", listNames));
  }

}
