package com.mechjacktv.mechjackbot.chatbot;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.MessageEventHandler;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public final class PircBotXMessageEventHandler implements MessageEventHandler {

    private final Map<String, Command> commands;

    public PircBotXMessageEventHandler() {
        this.commands = Collections.emptyMap();
    }

    @Override
    public final Collection<Command> getCommands() {
        return Collections.unmodifiableCollection(this.commands.values());
    }

    @Override
    public final Command getCommand(final String commandTrigger) {
        return this.commands.get(commandTrigger);
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
