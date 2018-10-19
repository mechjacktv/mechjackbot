package com.mechjacktv.mechjackbot.chatbot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.google.common.base.Strings;
import com.mechjacktv.mechjackbot.AppConfiguration;

public class PropertiesAppConfiguration implements AppConfiguration {

    private static final String CONFIG_LOCATION = System.getProperty("user.home") + "/.mechjackbot.config";

    private final Properties properties;

    public PropertiesAppConfiguration() throws IOException {
        this.properties = new Properties();
        try (final FileInputStream configFile = new FileInputStream(CONFIG_LOCATION)) {
            this.properties.load(configFile);
        }
    }

    @Override
    public String getProperty(final String key) {
        return this.properties.getProperty(key);
    }

    @Override
    public String getProperty(final String key, final String defaultValue) {
        final String value = this.properties.getProperty(key);

        return Strings.isNullOrEmpty(value) ? defaultValue : value;
    }
}
