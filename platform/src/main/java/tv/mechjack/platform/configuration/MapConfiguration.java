package tv.mechjack.platform.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import tv.mechjack.platform.utils.ExecutionUtils;

public class MapConfiguration implements Configuration {

  private final ExecutionUtils executionUtils;
  private final Map<String, String> data;

  @Inject
  public MapConfiguration(final ExecutionUtils executionUtils) {
    this(executionUtils, new HashMap<>());
  }

  public MapConfiguration(final ExecutionUtils executionUtils, final Map<String, String> data) {
    this.executionUtils = executionUtils;
    this.data = data;
  }

  @Override
  public final Optional<String> get(final ConfigurationKey key) {
    return this.get(key.value);
  }

  @Override
  public final Optional<String> get(final String key) {
    return Optional.ofNullable(this.get(key, null));
  }

  @Override
  public String get(final ConfigurationKey key, final String defaultValue) {
    return this.get(key.value, defaultValue);
  }

  @Override
  public String get(final String key, final String defaultValue) {
    Objects.requireNonNull(key, this.executionUtils.nullMessageForName("key"));

    final String value = this.data.get(key);

    return value != null ? value : defaultValue;
  }

  public void set(final ConfigurationKey key, final String value) {
    this.set(key.value, value);
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
