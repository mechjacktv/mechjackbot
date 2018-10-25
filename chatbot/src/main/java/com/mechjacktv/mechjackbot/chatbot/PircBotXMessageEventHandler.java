package com.mechjacktv.mechjackbot.chatbot;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.MessageEventHandler;

import java.util.*;

public final class PircBotXMessageEventHandler implements MessageEventHandler {

  private final Map<CommandTrigger, Command> commands;

  public PircBotXMessageEventHandler() {
    this.commands = new HashMap<>();
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
    this.commands.put(command.getTrigger(), command);
  }

  @Override
  public final void handleMessage(final MessageEvent messageEvent) {
    for (final Command command : this.getCommands()) {
      if (command.isTriggered(messageEvent)) {
        command.handleMessage(messageEvent);
      }
    }
  }

}
