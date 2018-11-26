package com.mechjacktv.mechjackbot.chatbot;

import java.io.File;

import com.mechjacktv.configuration.PropertiesConfiguration;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.FilePropertiesSource;
import com.mechjacktv.util.scheduleservice.ScheduleService;

class DefaultPropertiesConfiguration extends PropertiesConfiguration {

  private static final String CONFIG_PROPERTIES_FILE_NAME = "application.config";

  DefaultPropertiesConfiguration(final ChatBotConfiguration chatBotConfiguration,
      final ExecutionUtils executionUtils,
      final ScheduleService scheduleService) {
    super(
        new FilePropertiesSource(new File(chatBotConfiguration.getDataLocation().value, CONFIG_PROPERTIES_FILE_NAME)),
        executionUtils, scheduleService);
  }

}
