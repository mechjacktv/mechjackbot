package com.mechjacktv.mechjackbot.command;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.util.scheduleservice.ScheduleService;

@SuppressWarnings("CanBeFinal")
public class QuitCommand extends AbstractCommand {

  static final String COMMAND_TRIGGER_KEY = "command.quit.trigger";
  static final String COMMAND_TRIGGER_DEFAULT = "!quit";

  static final String COMMAND_MESSAGE_KEY = "command.quit.message";
  static final String COMMAND_MESSAGE_DEFAULT = "Powering down";

  private final com.mechjacktv.configuration.Configuration configuration;
  private final ScheduleService scheduleService;

  @Inject
  public QuitCommand(final com.mechjacktv.configuration.Configuration configuration, final CommandUtils commandUtils,
      final ScheduleService scheduleService) {
    super(new Configuration(configuration, commandUtils, CommandDescription.of("Shuts the chat bot down."),
        CommandTriggerKey.of(COMMAND_TRIGGER_KEY), CommandTrigger.of(COMMAND_TRIGGER_DEFAULT)));
    this.configuration = configuration;
    this.scheduleService = scheduleService;
  }

  @Override
  @RestrictToAccessLevel(AccessLevel.OWNER)
  public void handleMessageEvent(final MessageEvent messageEvent) {
    final ChatBot chatBot = messageEvent.getChatBot();

    messageEvent.sendResponse(Message.of(this.configuration.get(COMMAND_MESSAGE_KEY, COMMAND_MESSAGE_DEFAULT)));
    this.scheduleService.stop();
    chatBot.stop();
  }

}
