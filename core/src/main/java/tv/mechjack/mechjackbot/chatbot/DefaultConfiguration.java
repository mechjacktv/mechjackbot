package tv.mechjack.mechjackbot.chatbot;

import java.io.File;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.platform.application.Application;
import tv.mechjack.platform.configuration.PropertiesConfiguration;
import tv.mechjack.platform.util.ExecutionUtils;
import tv.mechjack.platform.util.scheduleservice.FileHotUpdatePropertiesSource;
import tv.mechjack.platform.util.scheduleservice.HotUpdatePropertiesSource;
import tv.mechjack.platform.util.scheduleservice.ScheduleService;

public final class DefaultConfiguration extends PropertiesConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConfiguration.class);

  public static final String CONFIG_PROPERTIES_FILE_NAME = "application.config";

  @Inject
  DefaultConfiguration(final Application application, final ExecutionUtils executionUtils,
      final ScheduleService scheduleService) {
    this(new FileHotUpdatePropertiesSource(new File(application.getApplicationDataLocation().value,
        CONFIG_PROPERTIES_FILE_NAME)), executionUtils, scheduleService);
  }

  DefaultConfiguration(final HotUpdatePropertiesSource hotUpdatePropertiesSource, final ExecutionUtils executionUtils,
      final ScheduleService scheduleService) {
    super(hotUpdatePropertiesSource, executionUtils, scheduleService, LOGGER);
  }

}
