package com.mechjacktv.mechjackbot.configuration;

import java.util.*;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.util.ExecutionUtils;

public class MapAppConfiguration implements AppConfiguration {

  private final ExecutionUtils executionUtils;
  private final Map<String, String> configuration;

  @Inject
  public MapAppConfiguration(final ExecutionUtils executionUtils) {
    this.executionUtils = executionUtils;
    this.configuration = new HashMap<>();
  }

  @Override
  public final Optional<String> get(final String key) {
    return Optional.ofNullable(this.get(key, null));
  }

  @Override
  public String get(String key, String defaultValue) {
    Objects.requireNonNull(key, this.executionUtils.nullMessageForName("key"));

    final Object value = this.configuration.get(key);

    return value != null ? value.toString() : defaultValue;
  }

  public void set(String key, String value) {
    Objects.requireNonNull(key, this.executionUtils.nullMessageForName("key"));
    Objects.requireNonNull(value, this.executionUtils.nullMessageForName("value"));
    this.configuration.put(key, value);
  }

  @Override
  public Collection<String> getKeys() {
    return Collections.unmodifiableSet(this.configuration.keySet());
  }

}
