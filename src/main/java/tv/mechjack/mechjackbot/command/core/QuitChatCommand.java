package tv.mechjack.mechjackbot.command.core;

import javax.inject.Inject;

import tv.mechjack.mechjackbot.*;
import tv.mechjack.mechjackbot.ChatMessageEvent;
import tv.mechjack.mechjackbot.RequiresUserRole;
import tv.mechjack.mechjackbot.UserRole;
import tv.mechjack.mechjackbot.command.BaseChatCommand;
import tv.mechjack.mechjackbot.command.CommandConfigurationBuilder;
import tv.mechjack.util.scheduleservice.ScheduleService;

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
  @RequiresUserRole(UserRole.BROADCASTER)
  public void handleMessageEvent(final ChatMessageEvent chatMessageEvent) {
    this.sendResponse(chatMessageEvent);
    this.scheduleService.stop();
    chatMessageEvent.getChatBot().stop();
  }

}
