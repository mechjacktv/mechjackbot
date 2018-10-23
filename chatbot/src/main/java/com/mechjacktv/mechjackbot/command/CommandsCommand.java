package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.GlobalCoolDown;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.MessageEventHandler;

import javax.inject.Inject;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@SuppressWarnings("CanBeFinal")
public class CommandsCommand extends AbstractCommand {

    private final MessageEventHandler messageEventHandler;

    @Inject
    public CommandsCommand(final CommandUtils commandUtils, final MessageEventHandler messageEventHandler) {
        super("!commands", commandUtils);
        this.messageEventHandler = messageEventHandler;
    }

    @Override
    public String getDescription() {
        return "Lists all the commands available to users.";
    }

    @Override
    @GlobalCoolDown
    public void handleMessage(final MessageEvent messageEvent) {
        final StringBuilder builder = new StringBuilder("Channel Commands:");

        for(final Command command : getSortedCommands()) {
            if(command.isTriggerable()) {
                builder.append(String.format(" %s", command.getTrigger()));
            }
        }
        messageEvent.sendResponse(builder.toString());
    }

    private Set<Command> getSortedCommands() {
        final SortedSet<Command> sortedCommands = new TreeSet<>(this::compareCommands);

        sortedCommands.addAll(messageEventHandler.getCommands());
        return sortedCommands;
    }

    private int compareCommands(final Command command1, final Command command2) {
        return command1.getTrigger().compareTo(command2.getTrigger());
    }

}
