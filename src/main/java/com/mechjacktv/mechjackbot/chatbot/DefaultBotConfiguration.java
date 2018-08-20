package com.mechjacktv.mechjackbot.chatbot;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.BotConfiguration;

import javax.inject.Inject;

public final class DefaultBotConfiguration implements BotConfiguration {

    private final String username;
    private final String password;
    private final String channel;

    @Inject
    public DefaultBotConfiguration(final AppConfiguration appConfiguration) {
        this.username = appConfiguration.getProperty("bot.username");
        this.password = appConfiguration.getProperty("bot.password");
        this.channel = appConfiguration.getProperty("bot.channel");
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getChannel() {
        return channel;
    }
}
