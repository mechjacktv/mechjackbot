package com.mechjacktv.util;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.mechjacktv.util.scheduleservice.ScheduleService;

public abstract class HotUpdatePropertiesWrapper {

  public static final String UPDATE_PERIOD_KEY = "properties_wrapper.update_period.minutes";
  public static final String DEFAULT_UPDATE_PERIOD = "10";

  private final Logger logger;
  private final Properties properties;

  public HotUpdatePropertiesWrapper(final PropertiesSource propertiesSource,
      final ScheduleService scheduleService, final Logger logger) {
    this.logger = logger;
    this.properties = new Properties();
    this.loadProperties(propertiesSource);
    scheduleService.schedule(() -> this.loadProperties(propertiesSource),
        Integer.parseInt(System.getProperty(UPDATE_PERIOD_KEY, DEFAULT_UPDATE_PERIOD)),
        TimeUnit.MINUTES, true);
  }

  private void loadProperties(final PropertiesSource propertiesSource) {
    try {
      propertiesSource.read(this.properties::load);
    } catch (final Throwable t) {
      this.logger.error(String.format("Failure while loading properties file: %s", t.getMessage()), t);
    }
  }

  protected Properties getProperties() {
    return this.properties;
  }

}
