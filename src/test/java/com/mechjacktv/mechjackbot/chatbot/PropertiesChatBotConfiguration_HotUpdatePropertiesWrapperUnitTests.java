package com.mechjacktv.mechjackbot.chatbot;

import static com.mechjacktv.mechjackbot.chatbot.PropertiesChatBotConfiguration.*;

import java.util.HashMap;
import java.util.Map;

import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.HotUpdatePropertiesWrapperContractTests;
import com.mechjacktv.util.PropertiesSource;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public class PropertiesChatBotConfiguration_HotUpdatePropertiesWrapperUnitTests
    extends HotUpdatePropertiesWrapperContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  @Override
  protected PropertiesChatBotConfiguration givenASubjectToTest(final PropertiesSource propertiesSource,
      final ScheduleService scheduleService) {
    return new PropertiesChatBotConfiguration(this.arbitraryDataGenerator.getString(), propertiesSource,
        scheduleService);
  }

  @Override
  protected Map<String, String> givenAPropertiesMap() {
    final Map<String, String> properties = new HashMap<>();

    properties.put(TWITCH_CHANNEL_KEY, this.arbitraryDataGenerator.getString());
    properties.put(TWITCH_CLIENT_ID_KEY, this.arbitraryDataGenerator.getString());
    properties.put(TWITCH_PASSWORD_KEY, this.arbitraryDataGenerator.getString());
    properties.put(TWITCH_LOGIN_KEY, this.arbitraryDataGenerator.getString());
    return properties;
  }

}
