package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.RestrictToOwner;
import com.mechjacktv.scheduleservice.ScheduleService;

import javax.inject.Inject;

@SuppressWarnings("CanBeFinal")
public class QuitCommand extends AbstractCommand {

  private final ScheduleService scheduleService;


  @Inject
  public QuitCommand(final CommandUtils commandUtils, final ScheduleService scheduleService) {
    super("!quit", commandUtils);
    this.scheduleService = scheduleService;
  }

  @Override
  public final String getDescription() {
    return "Shuts the chat bot down.";
  }

  @Override
  @RestrictToOwner
  public void handleMessage(final MessageEvent messageEvent) {
    final ChatBot chatBot = messageEvent.getChatBot();

    messageEvent.sendResponse("That's all for me, folks");
    this.scheduleService.stop();
    chatBot.stop();
  }

}
