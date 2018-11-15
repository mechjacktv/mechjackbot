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
  private static final String COMMAND_USAGE = "<commandTrigger>";

  private final AppConfiguration appConfiguration;
  private final CommandUtils commandUtils;
  private final CommandRegistry commandRegistry;

  @Inject
  public HelpCommand(final AppConfiguration appConfiguration, final CommandUtils commandUtils,
      final CommandRegistry commandRegistry) {
    super(new Configuration(appConfiguration, commandUtils,
        CommandDescription.of("Returns the description for a command."),
        CommandTriggerKey.of(COMMAND_TRIGGER_KEY), CommandTrigger.of(COMMAND_TRIGGER_DEFAULT))
        .setCommandUsage(CommandUsage.of(COMMAND_USAGE)));
    this.appConfiguration = appConfiguration;
    this.commandUtils = commandUtils;
    this.commandRegistry = commandRegistry;
  }

  @Override
  @CoolDown
  public void handleMessageEvent(final MessageEvent messageEvent) {
    final Message message = this.commandUtils.messageWithoutTrigger(this, messageEvent);

    if (!Strings.isNullOrEmpty(message.value)) {
      final CommandTrigger commandTrigger = CommandTrigger.of(message.value);
      final Optional<Command> command = this.commandRegistry.getCommand(commandTrigger);

      if (command.isPresent() && command.get().isTriggerable()) {
        this.sendCommandHelp(messageEvent, command.get());
      } else {
        this.sendCommandMissing(messageEvent, commandTrigger);
      }
    } else {
      this.commandUtils.sendUsage(this, messageEvent);
    }
  }

  private void sendCommandHelp(final MessageEvent messageEvent, final Command command) {
    final String messageFormat = this.appConfiguration.get(COMMAND_MESSAGE_FORMAT_KEY,
        COMMAND_MESSAGE_FORMAT_DEFAULT);

    messageEvent.sendResponse(Message.of(String.format(messageFormat,
        this.commandUtils.sanitizedChatUsername(this, messageEvent),
        command.getTrigger(), command.getDescription())));
  }

  private void sendCommandMissing(final MessageEvent messageEvent, final CommandTrigger commandTrigger) {
    final String messageFormat = this.appConfiguration.get(COMMAND_MISSING_MESSAGE_FORMAT_KEY,
        COMMAND_MISSING_MESSAGE_FORMAT_DEFAULT);

    messageEvent.sendResponse(Message.of(String.format(messageFormat,
        this.commandUtils.sanitizedChatUsername(this, messageEvent), commandTrigger)));
  }

}
