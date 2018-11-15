package com.mechjacktv.mechjackbot.command;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.util.scheduleservice.ScheduleService;

@SuppressWarnings("CanBeFinal")
public class QuitCommand extends AbstractCommand {

  private static final String COMMAND_TRIGGER_KEY = "command.quit.trigger";
  private static final String COMMAND_TRIGGER_DEFAULT = "!quit";

  private static final String COMMAND_MESSAGE_FORMAT_KEY = "command.quit.message_format";
  private static final String COMMAND_MESSAGE_FORMAT_DEFAULT = "Powering down";

  private final AppConfiguration appConfiguration;
  private final ScheduleService scheduleService;

  @Inject
  public QuitCommand(final AppConfiguration appConfiguration, final CommandUtils commandUtils,
      final ScheduleService scheduleService) {
    super(appConfiguration, CommandTriggerKey.of(COMMAND_TRIGGER_KEY), CommandTrigger.of(COMMAND_TRIGGER_DEFAULT),
        commandUtils);
    this.appConfiguration = appConfiguration;
    this.scheduleService = scheduleService;
  }

  @Override
  public final CommandDescription getDescription() {
    return CommandDescription.of("Shuts the chat bot down.");
  }

  @Override
  @RestrictToOwner
  public void handleMessageEvent(final MessageEvent messageEvent) {
    final String messageFormat = this.appConfiguration.get(COMMAND_MESSAGE_FORMAT_KEY,
        COMMAND_MESSAGE_FORMAT_DEFAULT);
    final ChatBot chatBot = messageEvent.getChatBot();

    messageEvent.sendResponse(Message.of(messageFormat));
    this.scheduleService.stop();
    chatBot.stop();
  }

}
