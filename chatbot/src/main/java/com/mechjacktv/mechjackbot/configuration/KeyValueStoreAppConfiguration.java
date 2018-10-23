package com.mechjacktv.mechjackbot.configuration;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.KeyValueStore;
import com.mechjacktv.mechjackbot.KeyValueStoreFactory;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

final class KeyValueStoreAppConfiguration implements AppConfiguration {

  private final KeyValueStore properties;

  @Inject
  public KeyValueStoreAppConfiguration(final KeyValueStoreFactory keyValueStoreFactory) {
    this.properties = keyValueStoreFactory.createOrOpenKeyValueStore("app_configuration");
  }

  @Override
  public Optional<String> get(String key) {
    final byte[] value = this.properties.get(key.getBytes()).orElse(null);

    if (value != null) {
      return Optional.of(new String(value));
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
      keys.add(new String(key));
    }
    return keys;
  }

  @Override
  public void remove(String key) {
    this.properties.remove(key.getBytes());
  }

  @Override
  public void set(String key, String value) {
    this.properties.put(key.getBytes(), value.getBytes());
  }

}
