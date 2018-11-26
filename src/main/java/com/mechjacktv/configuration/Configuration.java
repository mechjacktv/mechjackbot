package com.mechjacktv.configuration;

import java.util.Collection;
import java.util.Optional;

public interface Configuration {

  Optional<String> get(String key);

  String get(String key, String defaultValue);

  Collection<String> getKeys();

}
