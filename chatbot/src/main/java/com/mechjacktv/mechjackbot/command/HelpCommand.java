package com.mechjacktv.mechjackbot.command;

import java.util.Optional;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;

@SuppressWarnings("CanBeFinal")
public class HelpCommand extends AbstractCommand {

  private final CommandUtils commandUtils;
  private final MessageEventHandler messageEventHandler;

  @Inject
  public HelpCommand(final CommandUtils commandUtils, final MessageEventHandler messageEventHandler) {
    super(CommandTrigger.of("!help"), commandUtils);
    this.commandUtils = commandUtils;
    this.messageEventHandler = messageEventHandler;
  }

  @Override
  public final CommandDescription getDescription() {
    return CommandDescription.of("Returns the description for a command.");
  }

  @Override
  @GlobalCoolDown
  public void handleMessage(final MessageEvent messageEvent) {
    final Message message = messageEvent.getMessage();
    final String[] messageParts = message.value.split("\\s+");

    if (messageParts.length == 2) {
      final CommandTrigger commandTrigger = CommandTrigger.of(messageParts[1]);
      final Optional<Command> command = this.messageEventHandler.getCommand(commandTrigger);

      if (command.isPresent() && command.get().isTriggerable()) {
        messageEvent.sendResponse(Message.of(String.format("@%s, %s -> %s",
                this.commandUtils.getSanitizedViewerName(messageEvent),
                command.get().getTrigger(), command.get().getDescription())));
      } else {
        messageEvent.sendResponse(Message.of(String.format("@%s, I don't see a command triggered by %s.",
                this.commandUtils.getSanitizedViewerName(messageEvent), commandTrigger)));
      }
    } else {
      this.commandUtils.sendUsage(messageEvent,
              CommandUsage.of(String.format("%s <commandTrigger>", this.getTrigger())));
    }
  }

}
