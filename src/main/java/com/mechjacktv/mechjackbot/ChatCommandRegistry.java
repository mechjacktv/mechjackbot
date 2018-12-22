package com.mechjacktv.mechjackbot;

import java.util.Collection;
import java.util.Optional;

public interface ChatCommandRegistry {

  Collection<ChatCommand> getCommands();

  Optional<ChatCommand> getCommand(ChatCommandTrigger chatCommandTrigger);

  void addCommand(ChatCommand chatCommand);

}
