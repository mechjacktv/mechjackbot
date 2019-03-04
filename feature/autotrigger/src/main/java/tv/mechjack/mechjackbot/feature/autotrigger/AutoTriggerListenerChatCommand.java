package tv.mechjack.mechjackbot.feature.autotrigger;

import javax.inject.Inject;

import tv.mechjack.mechjackbot.api.BaseChatCommand;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.NoCoolDown;
import tv.mechjack.mechjackbot.api.RequiresAccessLevel;
import tv.mechjack.mechjackbot.api.UserRole;

public class AutoTriggerListenerChatCommand extends BaseChatCommand {

  public static final String DEFAULT_DESCRIPTION = "Monitors chat for activity and sends messages for auto-triggered "
      + "commands when the conditions are met.";

  private final AutoTriggerService autoTriggerService;

  @Inject
  AutoTriggerListenerChatCommand(
      final CommandConfigurationBuilder commandConfigurationBuilder,
      final AutoTriggerService autoTriggerService) {
    super(commandConfigurationBuilder.setDescription(DEFAULT_DESCRIPTION));
    this.autoTriggerService = autoTriggerService;
  }

  @Override
  public final boolean isTriggered(final ChatMessageEvent chatMessageEvent) {
    // we need to do book keeping on every message so always say yes
    return true;
  }

  @Override
  @NoCoolDown
  @RequiresAccessLevel(UserRole.VIEWER)
  public void handleMessageEvent(ChatMessageEvent chatMessageEvent) {
    this.autoTriggerService.handleMessageEvent(chatMessageEvent);
  }

}
