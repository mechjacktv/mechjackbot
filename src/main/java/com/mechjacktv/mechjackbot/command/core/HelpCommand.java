package com.mechjacktv.mechjackbot.command.core;

import java.util.Optional;

import javax.inject.Inject;

import com.google.common.base.Strings;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.BaseCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandMessageFormat;

public final class HelpCommand extends BaseCommand {

  public static final String DEFAULT_DESCRIPTION = "Returns the description for a command.";
  public static final String DEFAULT_MESSAGE_FORMAT = "@%s, %s -> %s";
  public static final String DEFAULT_MISSING_MESSAGE_FORMAT = "@%s, I don't see a command triggered by %s.";
  public static final String DEFAULT_TRIGGER = "!help";
  public static final String KEY_MISSING_MESSAGE_FORMAT = "missing_message_format";
  public static final String USAGE = "<commandTrigger>";

  private final CommandRegistry commandRegistry;
  private final CommandUtils commandUtils;
  private final Configuration configuration;
  private final CommandMessageFormat missingMessageFormatDefault;
  private final ConfigurationKey missingMessageFormatKey;

  @Inject
  protected HelpCommand(final CommandConfigurationBuilder commandConfigurationBuilder,
      final CommandRegistry commandRegistry, final CommandUtils commandUtils, final Configuration configuration) {
    super(commandConfigurationBuilder.setTrigger(DEFAULT_TRIGGER)
        .setDescription(DEFAULT_DESCRIPTION)
        .setMessageFormat(DEFAULT_MESSAGE_FORMAT)
        .setUsage(USAGE));
    this.commandRegistry = commandRegistry;
    this.commandUtils = commandUtils;
    this.configuration = configuration;
    this.missingMessageFormatDefault = CommandMessageFormat.of(DEFAULT_MISSING_MESSAGE_FORMAT);
    this.missingMessageFormatKey = ConfigurationKey.of(KEY_MISSING_MESSAGE_FORMAT, this.getClass());
  }

  @Override
  public void handleMessageEvent(final MessageEvent messageEvent) {
    final Message message = this.commandUtils.stripTriggerFromMessage(this, messageEvent);

    if (!Strings.isNullOrEmpty(message.value)) {
      final CommandTrigger commandTrigger = CommandTrigger.of(message.value);
      final Optional<Command> optionalCommand = this.commandRegistry.getCommand(commandTrigger);

      if (optionalCommand.isPresent() && optionalCommand.get().isTriggerable()) {
        final Command command = optionalCommand.get();

        this.sendResponse(messageEvent, command.getTrigger(), command.getDescription());
      } else {
        this.sendResponse(messageEvent, this.getMissingMessageFormat(), commandTrigger);
      }
    } else {
      this.sendUsage(messageEvent);
    }
  }

  private CommandMessageFormat getMissingMessageFormat() {
    return CommandMessageFormat.of(this.configuration.get(this.missingMessageFormatKey.value,
        this.missingMessageFormatDefault.value));
  }

}
