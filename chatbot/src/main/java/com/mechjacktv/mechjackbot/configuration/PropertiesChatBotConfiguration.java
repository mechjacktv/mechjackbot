package com.mechjacktv.mechjackbot.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.google.common.base.Strings;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.twitchclient.TwitchClientConfiguration;
import com.mechjacktv.twitchclient.TwitchClientId;

final class PropertiesChatBotConfiguration implements ChatBotConfiguration, TwitchClientConfiguration {

    private static final String DATA_LOCATION_KEY = "mechjackbot.data_location";
    private static final String DATA_LOCATION_DEFAULT = System.getProperty("user.home") + "/.mechjackbot";
    private static final String DATA_LOCATION = System.getProperty(DATA_LOCATION_KEY, DATA_LOCATION_DEFAULT);
    private static final String CONFIG_PROPERTIES_FILE_NAME = "chat_bot.config";

    private static final String TWITCH_CHANNEL_KEY = "twitch.channel";
    private static final String TWITCH_CLIENT_ID_KEY = "twitch.client_id";
    private static final String TWITCH_PASSWORD_KEY = "twitch.password";
    private static final String TWITCH_USERNAME_KEY = "twitch.username";

    private final DataLocation dataLocation;
    private final TwitchChannel twitchChannel;
    private final TwitchClientId twitchClientId;
    private final TwitchPassword twitchPassword;
    private final TwitchUsername twitchUsername;

    public PropertiesChatBotConfiguration() throws IOException {
        final Properties configProperties = this.loadConfigProperties();

        this.dataLocation = DataLocation.of(DATA_LOCATION);
        this.twitchChannel = TwitchChannel.of(configProperties.getProperty(TWITCH_CHANNEL_KEY));
        this.twitchClientId = TwitchClientId.of(configProperties.getProperty(TWITCH_CLIENT_ID_KEY));
        this.twitchPassword = TwitchPassword.of(configProperties.getProperty(TWITCH_PASSWORD_KEY));
        this.twitchUsername = TwitchUsername.of(configProperties.getProperty(TWITCH_USERNAME_KEY));
    }

    private Properties loadConfigProperties() throws IOException {
        final Properties configProperties = new Properties();

        if (this.didCreateConfigProperties()) {
            throw new IllegalStateException(String.format("Please configure your chat bot (%s)",
                    new File(new File(DATA_LOCATION), CONFIG_PROPERTIES_FILE_NAME).getCanonicalPath()));
        }
        try (final FileInputStream fileInputStream = new FileInputStream(new File(new File(DATA_LOCATION)
                , CONFIG_PROPERTIES_FILE_NAME))) {
            configProperties.load(fileInputStream);
        }
        if (this.isMissingRequiredValues(configProperties)) {
            throw new IllegalStateException(String.format("Please complete your chat bot configuration (%s)",
                    new File(new File(DATA_LOCATION), CONFIG_PROPERTIES_FILE_NAME).getCanonicalPath()));
        }
        return configProperties;
    }

    @SuppressWarnings("ignored")
    private boolean didCreateConfigProperties() throws IOException {
        final File dataLocation = new File(DATA_LOCATION);

        if (!dataLocation.exists()) {
            if (!dataLocation.mkdirs()) {
                throw new IOException(String.format("Failed to create %s", dataLocation.getCanonicalPath()));
            }
        } else if (!dataLocation.isDirectory()) {
            throw new IOException(dataLocation.getCanonicalPath() + " MUST be a directory");
        }
        return new File(dataLocation, CONFIG_PROPERTIES_FILE_NAME).createNewFile();
    }

    private boolean isMissingRequiredValues(final Properties configProperties) {
        return Strings.isNullOrEmpty(configProperties.getProperty(TWITCH_USERNAME_KEY))
                || Strings.isNullOrEmpty(configProperties.getProperty(TWITCH_PASSWORD_KEY))
                || Strings.isNullOrEmpty(configProperties.getProperty(TWITCH_CHANNEL_KEY))
                || Strings.isNullOrEmpty(configProperties.getProperty(TWITCH_CLIENT_ID_KEY));
    }

    @Override
    public DataLocation getDataLocation() {
        return this.dataLocation;
    }

    @Override
    public TwitchChannel getTwitchChannel() {
        return this.twitchChannel;
    }

    @Override
    public TwitchClientId getTwitchClientId() {
        return this.twitchClientId;
    }

    @Override
    public TwitchPassword getTwitchPassword() {
        return this.twitchPassword;
    }

    @Override
    public TwitchUsername getTwitchUsername() {
        return this.twitchUsername;
    }

}
