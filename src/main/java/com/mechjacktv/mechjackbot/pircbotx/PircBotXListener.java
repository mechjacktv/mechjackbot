package com.mechjacktv.mechjackbot.pircbotx;

import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEventHandler;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import javax.inject.Inject;
import java.util.Set;

public class PircBotXListener extends ListenerAdapter {

    private final MessageEventHandler messageEventHandler;

    @Inject
    public PircBotXListener(final Set<Command> commands, final MessageEventHandler messageEventHandler) {
        this.messageEventHandler = messageEventHandler;
        for(final Command command : commands) {
            this.messageEventHandler.addCommand(command);
        }
    }

    @Override
    public final void onPing(final PingEvent event) {
        event.respond(String.format("PONG %s", event.getPingValue()));
    }

    @Override
    public final void onGenericMessage(final GenericMessageEvent event) {
        this.messageEventHandler.handleMessage(new PircBotXMessageEvent(event));
    }
}