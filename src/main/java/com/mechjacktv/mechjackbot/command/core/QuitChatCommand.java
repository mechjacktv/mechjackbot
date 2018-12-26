package com.mechjacktv.mechjackbot.command.core;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.BaseChatCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.util.scheduleservice.ScheduleService;

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
