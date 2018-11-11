package com.mechjacktv.mechjackbot.configuration;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
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

  @Inject
  PropertiesAppConfiguration(final ChatBotConfiguration chatBotConfiguration,
      final ExecutionUtils executionUtils, final ScheduleService scheduleService) {
    super(new FileInputStreamSupplier(executionUtils, new File(chatBotConfiguration.getDataLocation().value,
        CONFIG_PROPERTIES_FILE_NAME)), scheduleService, log);
  }

  @Override
  public final Optional<String> get(final String key) {
    return Optional.ofNullable(this.get(key, null));
  }

  @Override
  public final String get(final String key, final String defaultValue) {
    final Object value = this.getProperties().get(key);

    if (value != null) {
      return value.toString();
    }
    return defaultValue;
  }

  @Override
  public final Collection<String> getKeys() {
    return this.getProperties().keySet().stream().map(Object::toString).collect(Collectors.toSet());
  }

}
