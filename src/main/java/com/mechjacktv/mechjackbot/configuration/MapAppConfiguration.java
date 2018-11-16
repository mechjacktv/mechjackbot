package com.mechjacktv.mechjackbot.configuration;

import java.util.*;

import javax.inject.Inject;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.util.ExecutionUtils;

public class MapAppConfiguration implements AppConfiguration {

  private final ExecutionUtils executionUtils;
  private final Map<String, String> data;

  @Inject
  public MapAppConfiguration(final ExecutionUtils executionUtils) {
    this.executionUtils = executionUtils;
    this.data = new HashMap<>();
  }

  @Override
  public final Optional<String> get(final String key) {
    return Optional.ofNullable(this.get(key, null));
  }

  @Override
  public String get(final String key, final String defaultValue) {
    Objects.requireNonNull(key, this.executionUtils.nullMessageForName("key"));

    final String value = this.data.get(key);

    return value != null ? value : defaultValue;
  }

  public void set(final String key, final String value) {
    Objects.requireNonNull(key, this.executionUtils.nullMessageForName("key"));
    Objects.requireNonNull(value, this.executionUtils.nullMessageForName("value"));
    this.data.put(key, value);
  }

  @Override
  public Collection<String> getKeys() {
    return Collections.unmodifiableSet(this.data.keySet());
  }

}
