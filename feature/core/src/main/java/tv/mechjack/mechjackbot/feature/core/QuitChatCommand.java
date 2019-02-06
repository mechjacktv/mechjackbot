package tv.mechjack.mechjackbot.feature.core;

import javax.inject.Inject;

import tv.mechjack.mechjackbot.api.BaseChatCommand;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.RequiresAccessLevel;
import tv.mechjack.mechjackbot.api.UserRole;
import tv.mechjack.platform.utils.scheduleservice.ScheduleService;

public final class QuitChatCommand extends BaseChatCommand {

  public static final String DEFAULT_DESCRIPTION = "Shuts the chat bot down.";
  public static final String DEFAULT_MESSAGE_FORMAT = "Powering down";
  public static final String DEFAULT_TRIGGER = "!quit";

  private final ScheduleService scheduleService;

  @Inject
  QuitChatCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
      final ScheduleService scheduleService) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT));
    this.scheduleService = scheduleService;
  }

  @Override
  @RequiresAccessLevel(UserRole.BROADCASTER)
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    this.sendResponse(chatMessageEvent);
    this.scheduleService.stop();
    chatMessageEvent.getChatBot().stop();
  }

}
