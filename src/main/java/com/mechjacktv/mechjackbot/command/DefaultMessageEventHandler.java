package com.mechjacktv.mechjackbot.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandRegistry;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.MessageEventHandler;

final class DefaultMessageEventHandler implements MessageEventHandler {

  private final Function<String, Logger> loggerFactory;
  private final Map<String, Logger> loggers;
  private final CommandRegistry commandRegistry;
  private final CommandUtils commandUtils;

  @Inject
  DefaultMessageEventHandler(final Set<Command> commands, final CommandRegistry commandRegistry,
      final CommandUtils commandUtils) {
    this(commands, commandRegistry, commandUtils, LoggerFactory::getLogger);
  }

  DefaultMessageEventHandler(final Set<Command> commands, final CommandRegistry commandRegistry,
      final CommandUtils commandUtils, final Function<String, Logger> loggerFactory) {
    this.commandRegistry = commandRegistry;
    this.commandUtils = commandUtils;
    for (final Command command : commands) {
      this.commandRegistry.addCommand(command);
    }
    this.loggerFactory = loggerFactory;
    this.loggers = new HashMap<>();
  }

  @Override
  public void handleMessageEvent(final MessageEvent messageEvent) {
    for (final Command command : this.commandRegistry.getCommands()) {
      if (this.isTriggered(command, messageEvent)) {
        this.getLogger(command.getName().value).info(
            String.format("Executed: trigger=%s, user=%s, message=%s",
                command.getTrigger(),
                messageEvent.getChatUser().getTwitchLogin(),
                messageEvent.getMessage()));
        try {
          command.handleMessageEvent(messageEvent);
        } catch (final Throwable t) {
          this.getLogger(command.getName().value).error(
              String.format("Failed: trigger=%s, user=%s, message=%s",
                  command.getTrigger(),
                  messageEvent.getChatUser().getTwitchLogin(),
                  messageEvent.getMessage()),
              t);
        }
      }
    }
  }

  private boolean isTriggered(final Command command, final MessageEvent messageEvent) {
    return command.isTriggered(messageEvent)
        && this.commandUtils.hasAccessLevel(command, messageEvent)
        && this.commandUtils.isCooledDown(command, messageEvent);
  }

  private Logger getLogger(final String name) {
    if (this.loggers.containsKey(name)) {
      return this.loggers.get(name);
    }

    final Logger logger = this.loggerFactory.apply(name);

    this.loggers.put(name, logger);
    return logger;
  }

}
