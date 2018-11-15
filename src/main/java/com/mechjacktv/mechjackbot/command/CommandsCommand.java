package com.mechjacktv.mechjackbot.command;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;

@SuppressWarnings("CanBeFinal")
public class CommandsCommand extends AbstractCommand {

  static final String COMMAND_TRIGGER_KEY = "command.commands.trigger";
  static final String COMMAND_TRIGGER_DEFAULT = "!commands";

  static final String COMMAND_MESSAGE_FORMAT_KEY = "command.commands.message_format";
  static final String COMMAND_MESSAGE_FORMAT_DEFAULT = "Channel Commands: %s";

  private final AppConfiguration appConfiguration;
  private final CommandRegistry commandRegistry;

  @Inject
  public CommandsCommand(final AppConfiguration appConfiguration, final CommandUtils commandUtils,
      final CommandRegistry commandRegistry) {
    super(new Configuration(appConfiguration, commandUtils,
        CommandDescription.of("Lists all the commands available to users."),
        CommandTriggerKey.of(COMMAND_TRIGGER_KEY), CommandTrigger.of(COMMAND_TRIGGER_DEFAULT)));
    this.appConfiguration = appConfiguration;
    this.commandRegistry = commandRegistry;
  }

  @Override
  public void handleMessageEvent(final MessageEvent messageEvent) {
    final String messageFormat = this.appConfiguration.get(COMMAND_MESSAGE_FORMAT_KEY, COMMAND_MESSAGE_FORMAT_DEFAULT);
    final StringBuilder builder = new StringBuilder();

    for (final Command command : this.getSortedCommands()) {
      if (command.isTriggerable()) {
        builder.append(String.format(" %s", command.getTrigger()));
      }
    }
    messageEvent.sendResponse(Message.of(String.format(messageFormat, builder.toString().trim())));
  }

  private Set<Command> getSortedCommands() {
    final SortedSet<Command> sortedCommands = new TreeSet<>(this::compareCommands);

    sortedCommands.addAll(this.commandRegistry.getCommands());
    return sortedCommands;
  }

  private int compareCommands(final Command command1, final Command command2) {
    return command1.getTrigger().value.compareTo(command2.getTrigger().value);
  }

}
