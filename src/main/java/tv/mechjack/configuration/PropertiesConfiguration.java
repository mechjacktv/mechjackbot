package tv.mechjack.configuration;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.util.ExecutionUtils;
import tv.mechjack.util.HotUpdatePropertiesWrapper;
import tv.mechjack.util.PropertiesSource;
import tv.mechjack.util.scheduleservice.ScheduleService;

public class PropertiesConfiguration extends HotUpdatePropertiesWrapper implements Configuration {

  private static final Logger log = LoggerFactory.getLogger(PropertiesConfiguration.class);

  private final ExecutionUtils executionUtils;

  protected PropertiesConfiguration(final PropertiesSource propertiesSource, final ExecutionUtils executionUtils,
      final ScheduleService scheduleService) {
    super(propertiesSource, scheduleService, log);
    this.executionUtils = executionUtils;
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

    final Object value = this.getProperties().get(key);

    return value != null ? value.toString() : defaultValue;
  }

  @Override
  public final Collection<String> getKeys() {
    return this.getProperties().keySet().stream().map(Object::toString).collect(Collectors.toSet());
  }

}
