package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.GlobalCoolDown;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.MessageEventHandler;

import javax.inject.Inject;
import java.util.Optional;

@SuppressWarnings("CanBeFinal")
public class HelpCommand extends AbstractCommand {

    private final CommandUtils commandUtils;
    private final MessageEventHandler messageEventHandler;

    @Inject
    public HelpCommand(final CommandUtils commandUtils, final MessageEventHandler messageEventHandler) {
        super("!help", commandUtils);
        this.commandUtils = commandUtils;
        this.messageEventHandler = messageEventHandler;
    }

    @Override
    public final String getDescription() {
        return "Returns the description for a command.";
    }

    @Override
    @GlobalCoolDown
    public void handleMessage(final MessageEvent messageEvent) {
        final String message = messageEvent.getMessage();
        final String[] messageParts = message.split("\\s+");

        if (messageParts.length == 2) {
            final String commandTrigger = messageParts[1];
            final Optional<Command> command = this.messageEventHandler.getCommand(commandTrigger);

            if(command.isPresent() && command.get().isTriggerable()) {
                messageEvent.sendResponse(String.format("@%s, %s -> %s",
                        this.commandUtils.getSanitizedViewerName(messageEvent),
                        command.get().getTrigger(), command.get().getDescription()));
            } else {
                messageEvent.sendResponse(String.format("@%s, I don't see a command triggered by %s.",
                        this.commandUtils.getSanitizedViewerName(messageEvent), commandTrigger));
            }
        } else {
            this.commandUtils.sendUsage(messageEvent, String.format("%s <commandTrigger>", getTrigger()));
        }
    }

}
