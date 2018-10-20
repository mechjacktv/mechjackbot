package com.mechjacktv.mechjackbot.configuration;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.KeyValueStore;
import com.mechjacktv.mechjackbot.KeyValueStoreFactory;

import javax.inject.Inject;
import java.util.Optional;

final class KeyValueStoreAppConfiguration implements AppConfiguration {

    private final KeyValueStore properties;

    @Inject
    public KeyValueStoreAppConfiguration(final KeyValueStoreFactory keyValueStoreFactory) {
        this.properties = keyValueStoreFactory.createOrOpenKeyValueStore("app_configuration");
    }

    @Override
    public String getProperty(final String key, final String defaultValue) {
        final Optional<byte[]> value = this.properties.get(key.getBytes());

        return new String(value.orElseGet(defaultValue::getBytes));
    }

    @Override
    public void setProperty(String key, String value) {
        this.properties.put(key.getBytes(), value.getBytes());
    }

}
