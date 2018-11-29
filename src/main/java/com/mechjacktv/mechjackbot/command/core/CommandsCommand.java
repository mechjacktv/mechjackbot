package com.mechjacktv.mechjackbot.command.core;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandRegistry;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.command.BaseCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;

public final class CommandsCommand extends BaseCommand {

  public static final String MESSAGE_FORMAT_DEFAULT = "Channel Commands: %2$s";
  public static final String TRIGGER_DEFAULT = "!commands";

  private final CommandRegistry commandRegistry;

  @Inject
  protected CommandsCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
      final CommandRegistry commandRegistry) {
    super(commandConfigurationBuilder.setTrigger(TRIGGER_DEFAULT)
        .setDescription("Lists all the commands available to users.")
        .setMessageFormat(MESSAGE_FORMAT_DEFAULT));
    this.commandRegistry = commandRegistry;
  }

  @Override
  public void handleMessageEvent(final MessageEvent messageEvent) {
    final StringBuilder builder = new StringBuilder();

    for (final Command command : this.getSortedCommands()) {
      if (command.isTriggerable()) {
        builder.append(String.format(" %s", command.getTrigger()));
      }
    }
    this.sendResponse(messageEvent, builder.toString().trim());
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
