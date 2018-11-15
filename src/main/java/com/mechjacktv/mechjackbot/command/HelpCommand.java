package com.mechjacktv.mechjackbot.command;

import java.util.Optional;

import javax.inject.Inject;

import com.google.common.base.Strings;

import com.mechjacktv.mechjackbot.*;

@SuppressWarnings("CanBeFinal")
public class HelpCommand extends AbstractCommand {

  static final String COMMAND_TRIGGER_KEY = "command.help.trigger";
  static final String COMMAND_TRIGGER_DEFAULT = "!help";

  static final String COMMAND_MESSAGE_FORMAT_KEY = "command.help.message_format";
  static final String COMMAND_MESSAGE_FORMAT_DEFAULT = "@%s, %s -> %s";
  static final String COMMAND_MISSING_MESSAGE_FORMAT_KEY = "command.help.missing_message_format";
  static final String COMMAND_MISSING_MESSAGE_FORMAT_DEFAULT = "@%s, I don't see a command triggered by %s.";
  static final String COMMAND_USAGE_MESSAGE_FORMAT = "%s <commandTrigger>";

  private final AppConfiguration appConfiguration;
  private final CommandUtils commandUtils;
  private final CommandRegistry commandRegistry;

  @Inject
  public HelpCommand(final AppConfiguration appConfiguration, final CommandUtils commandUtils,
      final CommandRegistry commandRegistry) {
    super(appConfiguration, CommandDescription.of("Returns the description for a command."),
        CommandTriggerKey.of(COMMAND_TRIGGER_KEY), CommandTrigger.of(COMMAND_TRIGGER_DEFAULT),
        commandUtils);
    this.appConfiguration = appConfiguration;
    this.commandUtils = commandUtils;
    this.commandRegistry = commandRegistry;
  }

  @Override
  @GlobalCoolDown
  public void handleMessageEvent(final MessageEvent messageEvent) {
    final String messageArgument = this.commandUtils.stripTriggerOffMessage(this.getTrigger(),
        messageEvent.getMessage());

    if (!Strings.isNullOrEmpty(messageArgument)) {
      final CommandTrigger commandTrigger = CommandTrigger.of(messageArgument);
      final Optional<Command> command = this.commandRegistry.getCommand(commandTrigger);

      if (command.isPresent() && command.get().isViewerTriggerable()) {
        this.sendCommandHelp(messageEvent, command.get());
      } else {
        this.sendCommandMissing(messageEvent, commandTrigger);
      }
    } else {
      this.commandUtils.sendUsage(messageEvent,
          CommandUsage.of(String.format(COMMAND_USAGE_MESSAGE_FORMAT, this.getTrigger())));
    }
  }

  private void sendCommandHelp(final MessageEvent messageEvent, final Command command) {
    final String messageFormat = this.appConfiguration.get(COMMAND_MESSAGE_FORMAT_KEY,
        COMMAND_MESSAGE_FORMAT_DEFAULT);

    messageEvent.sendResponse(Message.of(String.format(messageFormat,
        this.commandUtils.getSanitizedViewerName(messageEvent),
        command.getTrigger(), command.getDescription())));
  }

  private void sendCommandMissing(final MessageEvent messageEvent, final CommandTrigger commandTrigger) {
    final String messageFormat = this.appConfiguration.get(COMMAND_MISSING_MESSAGE_FORMAT_KEY,
        COMMAND_MISSING_MESSAGE_FORMAT_DEFAULT);

    messageEvent.sendResponse(Message.of(String.format(messageFormat,
        this.commandUtils.getSanitizedViewerName(messageEvent), commandTrigger)));
  }

}
