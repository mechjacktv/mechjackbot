package com.mechjacktv.mechjackbot.command.core;

import java.util.stream.Collectors;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandRegistry;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.command.BaseCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;

public final class CommandsCommand extends BaseCommand {

  public static final String DEFAULT_DESCRIPTION = "Lists all the commands available to users.";
  public static final String DEFAULT_MESSAGE_FORMAT = "Commands: %2$s";
  public static final String DEFAULT_TRIGGER = "!commands";

  private final CommandRegistry commandRegistry;

  @Inject
  protected CommandsCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
      final CommandRegistry commandRegistry) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT));
    this.commandRegistry = commandRegistry;
  }

  @Override
  public void handleMessageEvent(final MessageEvent messageEvent) {
    this.sendResponse(messageEvent, this.commandRegistry.getCommands().stream().filter(Command::isTriggerable)
        .map(command -> command.getTrigger().value).sorted().collect(Collectors.joining(" ")));
  }

}
