package com.mechjacktv.mechjackbot.chatbot;

import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.util.ExecutionUtils;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.Listener;

import javax.inject.Inject;
import java.io.IOException;

public final class PircBotXChatBot implements ChatBot {

    private final ExecutionUtils executionUtils;
    private final PircBotX pircBotX;

    @Inject
    @SuppressWarnings("unused")
    public PircBotXChatBot(final ChatBotConfiguration chatBotConfiguration, final ExecutionUtils executionUtils,
                           final Listener listener) {
        final Configuration configuration = new Configuration.Builder()
                .setName(chatBotConfiguration.getTwitchUsername())
                .addServer("irc.chat.twitch.tv", 6667)
                .setServerPassword(chatBotConfiguration.getTwitchPassword())
                .addListener(listener)
                .addAutoJoinChannel("#" + chatBotConfiguration.getTwitchChannel())
                .buildConfiguration();

        this.executionUtils = executionUtils;
        this.pircBotX = new PircBotX(configuration);
    }

    PircBotXChatBot(final ExecutionUtils executionUtils, final PircBotX pircBotX) {
        this.executionUtils = executionUtils;
        this.pircBotX = pircBotX;
    }

    @Override
    public void start() {
        this.executionUtils.softenException(this.pircBotX::startBot, PircBotXStartupException.class);
    }

    @Override
    public void stop() {
        this.pircBotX.stopBotReconnect();
        this.pircBotX.sendIRC().quitServer("Shutdown");
    }

}
