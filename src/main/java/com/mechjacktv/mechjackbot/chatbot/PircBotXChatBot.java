package com.mechjacktv.mechjackbot.chatbot;

import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.BotConfiguration;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.Listener;

import javax.inject.Inject;
import java.io.IOException;

public final class PircBotXChatBot implements ChatBot {

    private final PircBotX pircBotX;

    @Inject
    public PircBotXChatBot(final BotConfiguration botConfiguration, final Listener listener) {
        final Configuration configuration = new Configuration.Builder()
                .setName(botConfiguration.getUsername())
                .addServer("irc.chat.twitch.tv", 6667)
                .setServerPassword(botConfiguration.getPassword())
                .addListener(listener)
                .addAutoJoinChannel("#" + botConfiguration.getChannel())
                .buildConfiguration();

        this.pircBotX = new PircBotX(configuration);
    }

    public PircBotXChatBot(final PircBotX pircBotX) {
        this.pircBotX = pircBotX;
    }

    @Override
    public void start() {
        try {
            this.pircBotX.startBot();
        } catch (final IOException | IrcException e) {
            throw new ChatBotStartupException(e);
        }
    }

    @Override
    public void stop() {
        this.pircBotX.stopBotReconnect();
        this.pircBotX.sendIRC().quitServer("Shutdown");
    }
}
