package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.scheduleservice.ScheduleService;

import javax.inject.Inject;

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

    messageEvent.sendResponse(Message.of("That's all for me, folks"));
    this.scheduleService.stop();
    chatBot.stop();
  }

}
