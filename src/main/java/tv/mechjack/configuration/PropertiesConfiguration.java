package tv.mechjack.configuration;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.util.ExecutionUtils;
import tv.mechjack.util.scheduleservice.HotUpdateProperties;
import tv.mechjack.util.scheduleservice.HotUpdatePropertiesSource;
import tv.mechjack.util.scheduleservice.ScheduleService;

public class PropertiesConfiguration implements Configuration {

  private final ExecutionUtils executionUtils;
  private final Properties properties;

  protected PropertiesConfiguration(final HotUpdatePropertiesSource hotUpdatePropertiesSource,
      final ExecutionUtils executionUtils, final ScheduleService scheduleService) {
    this(hotUpdatePropertiesSource, executionUtils, scheduleService,
        LoggerFactory.getLogger(PropertiesConfiguration.class));
  }

  protected PropertiesConfiguration(final HotUpdatePropertiesSource hotUpdatePropertiesSource,
      final ExecutionUtils executionUtils, final ScheduleService scheduleService, final Logger logger) {
    this.executionUtils = executionUtils;
    this.properties = new HotUpdateProperties(hotUpdatePropertiesSource, scheduleService, logger);
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
  public final String get(final ConfigurationKey key, final String defaultValue) {
    return this.get(key.value, defaultValue);
  }

  @Override
  public final String get(final String key, final String defaultValue) {
    Objects.requireNonNull(key, this.executionUtils.nullMessageForName("key"));

    final Object value = this.properties.get(key);

    return value != null ? value.toString() : defaultValue;
  }

  @Override
  public final Collection<String> getKeys() {
    return this.properties.keySet().stream().map(Object::toString).collect(Collectors.toSet());
  }

}
