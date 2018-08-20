package com.mechjacktv.mechjackbot.chatbot;

import com.mechjacktv.mechjackbot.BotConfiguration;
import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.mechjackbot.MessageEventHandler;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import javax.inject.Inject;
import java.util.Set;

public final class PircBotXMessageEventHandler extends ListenerAdapter implements MessageEventHandler {

    private final BotConfiguration botConfiguration;
    private final Set<Command> commands;

    @Inject
    public PircBotXMessageEventHandler(final BotConfiguration botConfiguration, final Set<Command> commands) {
        this.botConfiguration = botConfiguration;
        this.commands = commands;
    }

    @Override
    public final void handleMessage(final MessageEvent messageEvent) {
        for(final Command command : commands) {
            if(command.handleMessage(messageEvent)) {
                break;
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
