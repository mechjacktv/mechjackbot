package com.mechjacktv.mechjackbot.command;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandRegistry;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.util.ExecutionUtils;

public final class DefaultCommandRegistry implements CommandRegistry {

  private static final Logger log = LoggerFactory.getLogger(DefaultCommandRegistry.class);

  private final Map<CommandTrigger, Command> commands;
  private final ExecutionUtils executionUtils;

  @Inject
  public DefaultCommandRegistry(final ExecutionUtils executionUtils) {
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
  public final void addCommand(final Command command) {
    Objects.requireNonNull(command, this.executionUtils.nullMessageForName("command"));
    if (this.commands.containsKey(command.getTrigger())) {
      log.warn(String.format("Command, %s, with trigger, %s, was already registered. Replacing with %s",
          this.commands.get(command.getTrigger()).getName(), command.getTrigger(), command.getName()));
    }
    this.commands.put(command.getTrigger(), command);
    log.info(String.format("Added command, %s, with trigger, %s", command.getName(), command.getTrigger()));
  }

}
