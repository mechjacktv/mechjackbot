package com.mechjacktv.mechjackbot.chatbot;

import com.mechjacktv.mechjackbot.AppConfiguration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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
    public String getProperty(String key) {
        return this.properties.getProperty(key);
    }
}
