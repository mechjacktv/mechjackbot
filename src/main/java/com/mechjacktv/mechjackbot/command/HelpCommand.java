package com.mechjacktv.mechjackbot.command;

import java.util.Optional;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;

@SuppressWarnings("CanBeFinal")
public class HelpCommand extends AbstractCommand {

  private static final String COMMAND_TRIGGER_KEY = "command.help.trigger";
  private static final String COMMAND_TRIGGER_DEFAULT = "!help";

  private static final String COMMAND_MISSING_MESSAGE_FORMAT_KEY = "command.help.message_format.missing";
  private static final String COMMAND_MISSING_MESSAGE_FORMAT_DEFAULT = "@%s, I don't see a command triggered by %s.";
  private static final String COMMAND_PRESENT_MESSAGE_FORMAT_KEY = "command.help.message_format.present";
  private static final String COMMAND_PRESENT_MESSAGE_FORMAT_DEFAULT = "@%s, %s -> %s";
  private static final String COMMAND_USAGE_MESSAGE_FORMAT_KEY = "command.help.message_format.usage";
  private static final String COMMAND_USAGE_MESSAGE_FORMAT_DEFAULT = "%s <commandTrigger>";

  private final AppConfiguration appConfiguration;
  private final CommandUtils commandUtils;
  private final MessageEventHandler messageEventHandler;

  @Inject
  public HelpCommand(final AppConfiguration appConfiguration, final CommandUtils commandUtils,
      final MessageEventHandler messageEventHandler) {
    super(appConfiguration, CommandTriggerKey.of(COMMAND_TRIGGER_KEY), CommandTrigger.of(COMMAND_TRIGGER_DEFAULT),
        commandUtils);
    this.appConfiguration = appConfiguration;
    this.commandUtils = commandUtils;
    this.messageEventHandler = messageEventHandler;
  }

  @Override
  public final CommandDescription getDescription() {
    return CommandDescription.of("Returns the description for a command.");
  }

  @Override
  @GlobalCoolDown
  public void handleMessageEvent(final MessageEvent messageEvent) {
    final Message message = messageEvent.getMessage();
    final String[] messageParts = message.value.split("\\s+");

    if (messageParts.length == 2) {
      final CommandTrigger commandTrigger = CommandTrigger.of(messageParts[1]);
      final Optional<Command> command = this.messageEventHandler.getCommand(commandTrigger);

      if (command.isPresent() && command.get().isTriggerable()) {
        final String messageFormat = this.appConfiguration.get(COMMAND_PRESENT_MESSAGE_FORMAT_KEY,
            COMMAND_PRESENT_MESSAGE_FORMAT_DEFAULT);

        messageEvent.sendResponse(Message.of(String.format(messageFormat,
            this.commandUtils.getSanitizedViewerName(messageEvent),
            command.get().getTrigger(), command.get().getDescription())));
      } else {
        final String messageFormat = this.appConfiguration.get(COMMAND_MISSING_MESSAGE_FORMAT_KEY,
            COMMAND_MISSING_MESSAGE_FORMAT_DEFAULT);

        messageEvent.sendResponse(Message.of(String.format(messageFormat,
            this.commandUtils.getSanitizedViewerName(messageEvent), commandTrigger)));
      }
    } else {
      final String messageFormat = this.appConfiguration.get(COMMAND_USAGE_MESSAGE_FORMAT_KEY,
          COMMAND_USAGE_MESSAGE_FORMAT_DEFAULT);

      this.commandUtils.sendUsage(messageEvent,
          CommandUsage.of(String.format(messageFormat, this.getTrigger())));
    }
  }

}
