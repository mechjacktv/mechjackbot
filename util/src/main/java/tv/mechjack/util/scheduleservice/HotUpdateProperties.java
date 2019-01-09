package tv.mechjack.util.scheduleservice;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

public class HotUpdateProperties extends Properties {

  public static final String KEY_GLOBAL_UPDATE_PERIOD = "hot_update_properties.global_update_period.minutes";
  public static final String DEFAULT_GLOBAL_UPDATE_PERIOD = "10";

  private final Logger logger;

  public HotUpdateProperties(final HotUpdatePropertiesSource hotUpdatePropertiesSource,
      final ScheduleService scheduleService, final Logger logger) {
    this(hotUpdatePropertiesSource,
        Integer.parseInt(System.getProperty(KEY_GLOBAL_UPDATE_PERIOD, DEFAULT_GLOBAL_UPDATE_PERIOD)),
        scheduleService, logger);
  }

  public HotUpdateProperties(final HotUpdatePropertiesSource hotUpdatePropertiesSource, final Integer periodInMinutes,
      final ScheduleService scheduleService, final Logger logger) {
    this.logger = logger;
    this.loadProperties(hotUpdatePropertiesSource);
    scheduleService.schedule(() -> this.loadProperties(hotUpdatePropertiesSource), periodInMinutes, TimeUnit.MINUTES,
        true);
  }

  private void loadProperties(final HotUpdatePropertiesSource hotUpdatePropertiesSource) {
    try {
      hotUpdatePropertiesSource.read(this::load);
    } catch (final Throwable t) {
      this.logger.error(String.format("Failure while loading properties file: %s", t.getMessage()), t);
    }
  }

}
