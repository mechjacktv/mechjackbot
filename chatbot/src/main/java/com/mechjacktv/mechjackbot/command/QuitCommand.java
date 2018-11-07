package com.mechjacktv.mechjackbot.command;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.util.scheduleservice.ScheduleService;

@SuppressWarnings("CanBeFinal")
public class QuitCommand extends AbstractCommand {

  private final ScheduleService scheduleService;

  @Inject
  public QuitCommand(final CommandUtils commandUtils, final ScheduleService scheduleService) {
    super(CommandTrigger.of("!quit"), commandUtils);
    this.scheduleService = scheduleService;
  }

  @Override
  public final CommandDescription getDescription() {
    return CommandDescription.of("Shuts the chat bot down.");
  }

  @Override
  @RestrictToOwner
  public void handleMessage(final MessageEvent messageEvent) {
    final ChatBot chatBot = messageEvent.getChatBot();

    messageEvent.sendResponse(Message.of("MechJackBot powering down"));
    this.scheduleService.stop();
    chatBot.stop();
  }

}
