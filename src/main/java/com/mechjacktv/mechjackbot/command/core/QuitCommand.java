package com.mechjacktv.mechjackbot.command.core;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.BaseCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public final class QuitCommand extends BaseCommand {

  public static final String DEFAULT_DESCRIPTION = "Shuts the chat bot down.";
  public static final String DEFAULT_MESSAGE_FORMAT = "Powering down";
  public static final String DEFAULT_TRIGGER = "!quit";

  private final ScheduleService scheduleService;

  @Inject
  protected QuitCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
      final ScheduleService scheduleService) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT));
    this.scheduleService = scheduleService;
  }

  @Override
  @RequiresAccessLevel(AccessLevel.OWNER)
  public void handleMessageEvent(final MessageEvent messageEvent) {
    this.sendResponse(messageEvent);
    this.scheduleService.stop();
    messageEvent.getChatBot().stop();
  }

}
