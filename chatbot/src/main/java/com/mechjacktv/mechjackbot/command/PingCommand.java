package com.mechjacktv.mechjackbot.command;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;

@SuppressWarnings("CanBeFinal")
public class PingCommand extends AbstractCommand {

  private static final String COMMAND_TRIGGER_KEY = "command.oing.trigger";
  private static final String COMMAND_TRIGGER_DEFAULT = "!ping";

  private static final String COMMAND_MESSAGE_FORMAT_KEY = "command.ping.message_format";
  private static final String COMMAND_MESSAGE_FORMAT_DEFAULT = "Don't worry, @%s. I'm here.";

  private final AppConfiguration appConfiguration;
  private final CommandUtils commandUtils;

  @Inject
  public PingCommand(final AppConfiguration appConfiguration, final CommandUtils commandUtils) {
    super(appConfiguration, CommandTriggerKey.of(COMMAND_TRIGGER_KEY), CommandTrigger.of(COMMAND_TRIGGER_DEFAULT),
        commandUtils);
    this.appConfiguration = appConfiguration;
    this.commandUtils = commandUtils;
  }

  @Override
  public final CommandDescription getDescription() {
    return CommandDescription.of("A simple check to see if the chat bot is running.");
  }

  @Override
  @RestrictToPrivileged
  @GlobalCoolDown
  public void handleMessage(MessageEvent messageEvent) {
    final String messageFormat = this.appConfiguration.get(COMMAND_MESSAGE_FORMAT_KEY,
        COMMAND_MESSAGE_FORMAT_DEFAULT);

    messageEvent.sendResponse(Message.of(String.format(messageFormat,
        this.commandUtils.getSanitizedViewerName(messageEvent))));
  }

}
