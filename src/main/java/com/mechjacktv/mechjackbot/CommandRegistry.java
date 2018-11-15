package com.mechjacktv.mechjackbot;

import java.util.Collection;
import java.util.Optional;

public interface CommandRegistry {

  Collection<Command> getCommands();

  Optional<Command> getCommand(CommandTrigger commandTrigger);

  void addCommand(Command command);

}
