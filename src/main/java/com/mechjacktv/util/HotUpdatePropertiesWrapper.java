package com.mechjacktv.util;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.slf4j.Logger;

import com.mechjacktv.util.scheduleservice.ScheduleService;

public abstract class HotUpdatePropertiesWrapper {

  private static final String UPDATE_PERIOD_KEY = "properties_wrapper.update_period.minutes";
  private static final String UPDATE_PERIOD = System.getProperty(UPDATE_PERIOD_KEY, "10");

  private final Logger log;
  private final Properties properties;

  public HotUpdatePropertiesWrapper(final Supplier<InputStream> propertiesSupplier,
      final ScheduleService scheduleService, final Logger log) {
    this.log = log;
    this.properties = new Properties();
    this.loadProperties(propertiesSupplier);
    scheduleService.schedule(() -> this.loadProperties(propertiesSupplier), Integer.parseInt(UPDATE_PERIOD),
        TimeUnit.MINUTES, true);
  }

  private void loadProperties(final Supplier<InputStream> propertiesSupplier) {
    try {
      try (final InputStream propertiesInputStream = propertiesSupplier.get()) {
        this.properties.load(propertiesInputStream);
      }
    } catch (final Throwable t) {
      this.log.error(String.format("Failure while loading properties file: %s", t.getMessage()), t);
    }
  }

  protected Properties getProperties() {
    return this.properties;
  }

}
