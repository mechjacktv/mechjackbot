package tv.mechjack.mechjackbot.api;

import java.util.Collection;
import java.util.Optional;

public interface ChatCommandRegistry {

  Collection<ChatCommand> getCommands();

  Optional<ChatCommand> getCommand(ChatCommandTrigger chatCommandTrigger);

  Optional<ChatCommand> getCommand(Class<? extends ChatCommand> chatCommandClass);

  void addCommand(ChatCommand chatCommand);

  boolean hasCommand(ChatCommandTrigger trigger);

  boolean removeCommand(ChatCommandTrigger trigger);

}
