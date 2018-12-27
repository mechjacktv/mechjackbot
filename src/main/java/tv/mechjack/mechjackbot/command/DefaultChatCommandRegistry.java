package tv.mechjack.mechjackbot.command;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.mechjackbot.ChatCommand;
import tv.mechjack.mechjackbot.ChatCommandRegistry;
import tv.mechjack.mechjackbot.ChatCommandTrigger;
import tv.mechjack.util.ExecutionUtils;

public final class DefaultChatCommandRegistry implements ChatCommandRegistry {

  private static final Logger log = LoggerFactory.getLogger(DefaultChatCommandRegistry.class);

  private final Map<ChatCommandTrigger, ChatCommand> commands;
  private final ExecutionUtils executionUtils;

  @Inject
  DefaultChatCommandRegistry(final ExecutionUtils executionUtils) {
    this.commands = new ConcurrentHashMap<>();
    this.executionUtils = executionUtils;
  }

  @Override
  public final Collection<ChatCommand> getCommands() {
    return Collections.unmodifiableCollection(this.commands.values());
  }

  @Override
  public final Optional<ChatCommand> getCommand(final ChatCommandTrigger chatCommandTrigger) {
    return Optional.ofNullable(this.commands.get(chatCommandTrigger));
  }

  @Override
  public final void addCommand(final ChatCommand chatCommand) {
    Objects.requireNonNull(chatCommand, this.executionUtils.nullMessageForName("chatCommand"));
    if (this.hasCommand(chatCommand.getTrigger())) {
      log.warn(String.format("ChatCommand, %s, with trigger, %s, was already registered. Replacing with %s",
          this.commands.get(chatCommand.getTrigger()).getName(), chatCommand.getTrigger(), chatCommand.getName()));
    }
    this.commands.put(chatCommand.getTrigger(), chatCommand);
    log.info(String.format("Added chatCommand, %s, with trigger, %s", chatCommand.getName(), chatCommand.getTrigger()));
  }

  @Override
  public boolean hasCommand(final ChatCommandTrigger trigger) {
    Objects.requireNonNull(trigger, this.executionUtils.nullMessageForName("trigger"));
    return this.commands.containsKey(trigger);
  }

  @Override
  public boolean removeCommand(final ChatCommandTrigger trigger) {
    Objects.requireNonNull(trigger, this.executionUtils.nullMessageForName("trigger"));
    final ChatCommand chatCommand = this.commands.remove(trigger);
    if (Objects.nonNull(chatCommand)) {
      log.info(String.format("Removed chatCommand, %s, with trigger, %s", chatCommand.getName(),
          chatCommand.getTrigger()));
      return true;
    }
    return false;
  }

}
