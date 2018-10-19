package com.mechjacktv.mechjackbot.pircbotx;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.MessageEventHandler;

import java.util.*;

public final class PircBotXMessageEventHandler implements MessageEventHandler {

    private final Map<String, Command> commands;

    public PircBotXMessageEventHandler() {
        this.commands = new HashMap<>();
    }

    @Override
    public final Collection<Command> getCommands() {
        return Collections.unmodifiableCollection(this.commands.values());
    }

    @Override
    public final Optional<Command> getCommand(final String commandTrigger) {
        return Optional.ofNullable(this.commands.get(commandTrigger));
    }

    @Override
    public final void addCommand(Command command) {
        this.commands.put(command.getCommandTrigger(), command);
    }

    @Override
    public final void handleMessage(final MessageEvent messageEvent) {
        for(final Command command : getCommands()) {
            if(command.isHandledMessage(messageEvent)) {
                command.handleMessage(messageEvent);
            }
        }
    }

}
