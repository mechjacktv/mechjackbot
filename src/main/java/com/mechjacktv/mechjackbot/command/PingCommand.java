package com.mechjacktv.mechjackbot.command;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;

@SuppressWarnings("CanBeFinal")
public class PingCommand extends AbstractCommand {

  static final String COMMAND_TRIGGER_KEY = "command.oing.trigger";
  static final String COMMAND_TRIGGER_DEFAULT = "!ping";

  static final String COMMAND_MESSAGE_FORMAT_KEY = "command.ping.message_format";
  static final String COMMAND_MESSAGE_FORMAT_DEFAULT = "Don't worry, @%s. I'm here.";

  private final com.mechjacktv.configuration.Configuration configuration;
  private final CommandUtils commandUtils;

  @Inject
  public PingCommand(final com.mechjacktv.configuration.Configuration configuration, final CommandUtils commandUtils) {
    super(new Configuration(configuration, commandUtils,
        CommandDescription.of("A simple check to see if the chat bot is running."),
        CommandTriggerKey.of(COMMAND_TRIGGER_KEY), CommandTrigger.of(COMMAND_TRIGGER_DEFAULT)));
    this.configuration = configuration;
    this.commandUtils = commandUtils;
  }

  @Override
  @RestrictToAccessLevel(AccessLevel.MODERATOR)
  public void handleMessageEvent(MessageEvent messageEvent) {
    final String messageFormat = this.configuration.get(COMMAND_MESSAGE_FORMAT_KEY,
        COMMAND_MESSAGE_FORMAT_DEFAULT);

    messageEvent.sendResponse(Message.of(String.format(messageFormat,
        this.commandUtils.sanitizeChatUsername(messageEvent.getChatUser().getUsername()))));
  }

}
