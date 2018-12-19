package com.mechjacktv.configuration;

import java.util.Collection;
import java.util.Optional;

public interface Configuration {

  Optional<String> get(ConfigurationKey key);

  Optional<String> get(String key);

  String get(ConfigurationKey key, String defaultValue);

  String get(String key, String defaultValue);

  Collection<String> getKeys();

}
