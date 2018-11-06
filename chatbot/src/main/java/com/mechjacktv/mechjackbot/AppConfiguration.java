package com.mechjacktv.mechjackbot;

import java.util.Collection;
import java.util.Optional;

public interface AppConfiguration {

    Optional<String> get(String key);

    String get(String key, String defaultValue);

    Collection<String> getKeys();

}
