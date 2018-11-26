package com.mechjacktv.configuration;

import java.util.*;

import javax.inject.Inject;

import com.mechjacktv.util.ExecutionUtils;

public class MapConfiguration implements Configuration {

  private final ExecutionUtils executionUtils;
  private final Map<String, String> data;

  @Inject
  public MapConfiguration(final ExecutionUtils executionUtils) {
    this(executionUtils, new HashMap<>());
  }

  @Inject
  public MapConfiguration(final ExecutionUtils executionUtils, final Map<String, String> data) {
    this.executionUtils = executionUtils;
    this.data = data;
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