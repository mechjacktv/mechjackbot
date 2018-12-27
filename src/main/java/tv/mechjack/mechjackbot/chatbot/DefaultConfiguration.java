package tv.mechjack.mechjackbot.chatbot;

import java.io.File;

import javax.inject.Inject;

import tv.mechjack.configuration.PropertiesConfiguration;
import tv.mechjack.mechjackbot.ChatBotConfiguration;
import tv.mechjack.util.ExecutionUtils;
import tv.mechjack.util.FilePropertiesSource;
import tv.mechjack.util.scheduleservice.ScheduleService;

public class DefaultConfiguration extends PropertiesConfiguration {

  private static final String CONFIG_PROPERTIES_FILE_NAME = "application.config";

  @Inject
  DefaultConfiguration(final ChatBotConfiguration chatBotConfiguration, final ExecutionUtils executionUtils,
      final ScheduleService scheduleService) {
    super(new FilePropertiesSource(new File(chatBotConfiguration.getDataLocation().value, CONFIG_PROPERTIES_FILE_NAME)),
        executionUtils, scheduleService);
  }

}
