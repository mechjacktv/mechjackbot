package com.mechjacktv.mechjackbot.configuration;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import com.mechjacktv.keyvaluestore.KeyValueStore;
import com.mechjacktv.keyvaluestore.KeyValueStoreFactory;
import com.mechjacktv.mechjackbot.AppConfiguration;

final class KeyValueStoreAppConfiguration implements AppConfiguration {

    private final KeyValueStore properties;

    @Inject
    public KeyValueStoreAppConfiguration(final KeyValueStoreFactory keyValueStoreFactory) {
        this.properties = keyValueStoreFactory.createOrOpenKeyValueStore("app_configuration");
    }

    @Override
    public Optional<String> get(String key) {
        final byte[] value = this.properties.get(key.getBytes(Charset.defaultCharset())).orElse(null);

        if (value != null) {
            return Optional.of(new String(value, Charset.defaultCharset()));
        }
        return Optional.empty();
    }

    @Override
    public String get(final String key, final String defaultValue) {
        return this.get(key).orElse(defaultValue);
    }

    @Override
    public Collection<String> getKeys() {
        final Set<String> keys = new HashSet<>();

        for (final byte[] key : this.properties.getKeys()) {
            keys.add(new String(key, Charset.defaultCharset()));
        }
        return keys;
    }

}
