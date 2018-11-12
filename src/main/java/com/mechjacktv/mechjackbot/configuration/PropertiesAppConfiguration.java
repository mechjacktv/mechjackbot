package com.mechjacktv.mechjackbot.configuration;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.FileInputStreamSupplier;
import com.mechjacktv.util.HotUpdatePropertiesWrapper;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public final class PropertiesAppConfiguration extends HotUpdatePropertiesWrapper implements AppConfiguration {

  private static final Logger log = LoggerFactory.getLogger(PropertiesAppConfiguration.class);

  private static final String CONFIG_PROPERTIES_FILE_NAME = "application.config";

  private final ExecutionUtils executionUtils;

  @Inject
  PropertiesAppConfiguration(final ChatBotConfiguration chatBotConfiguration,
      final ExecutionUtils executionUtils, final ScheduleService scheduleService) {
    this(new FileInputStreamSupplier(executionUtils, new File(chatBotConfiguration.getDataLocation().value,
        CONFIG_PROPERTIES_FILE_NAME)), executionUtils, scheduleService);
  }

  PropertiesAppConfiguration(final Supplier<InputStream> propertiesSupplier, final ExecutionUtils executionUtils,
      final ScheduleService scheduleService) {
    super(propertiesSupplier, scheduleService, log);
    this.executionUtils = executionUtils;
  }

  @Override
  public final Optional<String> get(final String key) {
    return Optional.ofNullable(this.get(key, null));
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
