package tv.mechjack.mechjackbot.core;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.mechjackbot.api.ChatCommand;
import tv.mechjack.mechjackbot.api.ChatCommandRegistry;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.platform.util.ExecutionUtils;

public final class DefaultChatCommandRegistry implements ChatCommandRegistry {

  private static final Logger log = LoggerFactory.getLogger(DefaultChatCommandRegistry.class);

  private final Map<Class<? extends ChatCommand>, ChatCommand> commandByClass;
  private final Map<ChatCommandTrigger, ChatCommand> commandsByTrigger;
  private final ExecutionUtils executionUtils;

  @Inject
  DefaultChatCommandRegistry(final ExecutionUtils executionUtils) {
    this.commandByClass = new ConcurrentHashMap<>();
    this.commandsByTrigger = new ConcurrentHashMap<>();
    this.executionUtils = executionUtils;
  }

  @Override
  public final Collection<ChatCommand> getCommands() {
    return Collections.unmodifiableCollection(this.commandsByTrigger.values());
  }

  @Override
  public final Optional<ChatCommand> getCommand(final ChatCommandTrigger chatCommandTrigger) {
    return Optional.ofNullable(this.commandsByTrigger.get(chatCommandTrigger));
  }

  @Override
  public Optional<ChatCommand> getCommand(final Class<? extends ChatCommand> chatCommandClass) {
    return Optional.ofNullable(this.commandByClass.get(chatCommandClass));
  }

  @Override
  public final void addCommand(final ChatCommand chatCommand) {
    Objects.requireNonNull(chatCommand, this.executionUtils.nullMessageForName("chatCommand"));
    if (this.hasCommand(chatCommand.getTrigger())) {
      log.warn(String.format("ChatCommand, %s, with trigger, %s, was already registered. Replacing with %s",
          this.commandsByTrigger.get(chatCommand.getTrigger()).getName(), chatCommand.getTrigger(),
          chatCommand.getName()));
    }
    this.commandsByTrigger.put(chatCommand.getTrigger(), chatCommand);
    this.commandByClass.put(chatCommand.getClass(), chatCommand);
    log.info(String.format("Added chatCommand, %s, with trigger, %s", chatCommand.getName(), chatCommand.getTrigger()));
  }

  @Override
  public boolean hasCommand(final ChatCommandTrigger trigger) {
    Objects.requireNonNull(trigger, this.executionUtils.nullMessageForName("trigger"));
    return this.commandsByTrigger.containsKey(trigger);
  }

  @Override
  public boolean removeCommand(final ChatCommandTrigger trigger) {
    Objects.requireNonNull(trigger, this.executionUtils.nullMessageForName("trigger"));

    final ChatCommand chatCommand = this.commandsByTrigger.remove(trigger);

    if (Objects.nonNull(chatCommand)) {
      this.commandByClass.remove(chatCommand.getClass());
      log.info(String.format("Removed chatCommand, %s, with trigger, %s", chatCommand.getName(),
          chatCommand.getTrigger()));
      return true;
    }
    return false;
  }

}
