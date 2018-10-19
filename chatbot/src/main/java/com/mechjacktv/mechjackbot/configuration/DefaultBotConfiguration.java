package com.mechjacktv.mechjackbot.configuration;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.BotConfiguration;

import javax.inject.Inject;

public final class DefaultBotConfiguration implements BotConfiguration {

    private static final String BOT_USERNAME = "bot.username";
    private static final String BOT_PASSWORD = "bot.password";
    private static final String BOT_CHANNEL = "bot.channel";

    private final String username;
    private final String password;
    private final String channel;

    @Inject
    public DefaultBotConfiguration(final AppConfiguration appConfiguration) {
        this.username = appConfiguration.getProperty(BOT_USERNAME);
        this.password = appConfiguration.getProperty(BOT_PASSWORD);
        this.channel = appConfiguration.getProperty(BOT_CHANNEL);
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
