package com.mechjacktv.mechjackbot.chatbot;

import java.util.Set;

import javax.inject.Inject;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.MessageEventHandler;

public final class PircBotXMessageEventHandler extends ListenerAdapter implements MessageEventHandler {

    private final Set<Command> commands;

    @Inject
    public PircBotXMessageEventHandler(final Set<Command> commands) {
        this.commands = commands;
    }

    @Override
    public final void handleMessage(final MessageEvent messageEvent) {
        for(final Command command : commands) {
            if(command.isHandledMessage(messageEvent)) {
                command.handleMessage(messageEvent);
            }
        }
    }

    @Override
    public final void onPing(final PingEvent event) {
        event.respond(String.format("PONG %s", event.getPingValue()));
    }

    @Override
    public final void onGenericMessage(final GenericMessageEvent event) {
        handleMessage(new PircBotXMessageEvent(event));
    }

}
