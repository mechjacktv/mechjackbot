package com.mechjacktv.mechjackbot;

import java.util.Collection;
import java.util.Optional;

public interface MessageEventHandler {

  Collection<Command> getCommands();

  Optional<Command> getCommand(CommandTrigger commandTrigger);

  void addCommand(Command command);

  void handleMessage(MessageEvent messageEvent);

}
