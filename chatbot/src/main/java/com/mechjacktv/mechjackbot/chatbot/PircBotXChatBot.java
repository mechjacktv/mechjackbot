package com.mechjacktv.mechjackbot.chatbot;

import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.Listener;

import javax.inject.Inject;
import java.io.IOException;

public final class PircBotXChatBot implements ChatBot {

    private final PircBotX pircBotX;

    @Inject
    @SuppressWarnings("unused")
    public PircBotXChatBot(final ChatBotConfiguration chatBotConfiguration, final Listener listener) {
        final Configuration configuration = new Configuration.Builder()
                .setName(chatBotConfiguration.getTwitchUsername())
                .addServer("irc.chat.twitch.tv", 6667)
                .setServerPassword(chatBotConfiguration.getTwitchPassword())
                .addListener(listener)
                .addAutoJoinChannel("#" + chatBotConfiguration.getTwitchChannel())
                .buildConfiguration();

        this.pircBotX = new PircBotX(configuration);
    }

    PircBotXChatBot(final PircBotX pircBotX) {
        this.pircBotX = pircBotX;
    }

    @Override
    public void start() {
        try {
            this.pircBotX.startBot();
        } catch (final IOException | IrcException e) {
            throw new PircBotXStartupException(e);
        }
    }

    @Override
    public void stop() {
        this.pircBotX.stopBotReconnect();
        this.pircBotX.sendIRC().quitServer("Shutdown");
    }

}
