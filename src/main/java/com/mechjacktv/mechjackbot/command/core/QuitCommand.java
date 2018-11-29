package com.mechjacktv.mechjackbot.command.core;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.BaseCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public final class QuitCommand extends BaseCommand {

  public static final String MESSAGE_FORMAT_DEFAULT = "Powering down";
  public static final String TRIGGER_DEFAULT = "!quit";

  private final ScheduleService scheduleService;

  @Inject
  protected QuitCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
      final ScheduleService scheduleService) {
    super(commandConfigurationBuilder.setTrigger(TRIGGER_DEFAULT)
        .setDescription("Shuts the chat bot down.")
        .setMessageFormat(MESSAGE_FORMAT_DEFAULT));
    this.scheduleService = scheduleService;
  }

  @Override
  @RestrictToAccessLevel(AccessLevel.OWNER)
  public void handleMessageEvent(final MessageEvent messageEvent) {
    this.sendResponse(messageEvent);
    this.scheduleService.stop();
    messageEvent.getChatBot().stop();
  }

}
