package com.mechjacktv.mechjackbot.command;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.*;

@SuppressWarnings("CanBeFinal")
public class CommandsCommand extends AbstractCommand {

    private final MessageEventHandler messageEventHandler;

    @Inject
    public CommandsCommand(final CommandUtils commandUtils, final MessageEventHandler messageEventHandler) {
        super(CommandTrigger.of("!commands"), commandUtils);
        this.messageEventHandler = messageEventHandler;
    }

    @Override
    public CommandDescription getDescription() {
        return CommandDescription.of("Lists all the commands available to users.");
    }

    @Override
    @GlobalCoolDown
    public void handleMessage(final MessageEvent messageEvent) {
        final StringBuilder builder = new StringBuilder("Channel Commands:");

        for (final Command command : this.getSortedCommands()) {
            if (command.isTriggerable()) {
                builder.append(String.format(" %s", command.getTrigger()));
            }
        }
        messageEvent.sendResponse(Message.of(builder.toString()));
    }

    private Set<Command> getSortedCommands() {
        final SortedSet<Command> sortedCommands = new TreeSet<>(this::compareCommands);

        sortedCommands.addAll(this.messageEventHandler.getCommands());
        return sortedCommands;
    }

    private int compareCommands(final Command command1, final Command command2) {
        return command1.getTrigger().value.compareTo(command2.getTrigger().value);
    }

}
