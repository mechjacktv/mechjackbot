package com.mechjacktv.mechjackbot.chatbot;

import java.util.*;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.MessageEventHandler;
import com.mechjacktv.util.ExecutionUtils;

public final class PircBotXMessageEventHandler implements MessageEventHandler {

  private final Map<CommandTrigger, Command> commands;
  private final ExecutionUtils executionUtils;

  @Inject
  public PircBotXMessageEventHandler(final ExecutionUtils executionUtils) {
    this.commands = new HashMap<>();
    this.executionUtils = executionUtils;
  }

  @Override
  public final Collection<Command> getCommands() {
    return Collections.unmodifiableCollection(this.commands.values());
  }

  @Override
  public final Optional<Command> getCommand(final CommandTrigger commandTrigger) {
    return Optional.ofNullable(this.commands.get(commandTrigger));
  }

  @Override
  public final void addCommand(Command command) {
    Objects.requireNonNull(command, this.executionUtils.nullMessageForName("command"));
    this.commands.put(command.getTrigger(), command);
  }

  @Override
  public final void handleMessageEvent(final MessageEvent messageEvent) {
    Objects.requireNonNull(messageEvent, this.executionUtils.nullMessageForName("messageEvent"));
    for (final Command command : this.getCommands()) {
      if (command.isTriggered(messageEvent)) {
        command.handleMessageEvent(messageEvent);
      }
    }
  }

}
