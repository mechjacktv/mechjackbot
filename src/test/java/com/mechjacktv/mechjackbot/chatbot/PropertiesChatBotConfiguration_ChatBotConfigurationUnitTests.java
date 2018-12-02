package com.mechjacktv.mechjackbot.chatbot;

import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.ChatBotConfigurationContractTests;
import com.mechjacktv.testframework.ArbitraryDataGenerator;
import com.mechjacktv.util.MapPropertiesSource;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public class PropertiesChatBotConfiguration_ChatBotConfigurationUnitTests extends ChatBotConfigurationContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  @Override
  protected ChatBotConfiguration givenASubjectToTest(final String dataLocation, final Map<String, String> properties) {
    final Map<String, String> mappedProperties = this.mapKeysForProperties(properties);

    return new PropertiesChatBotConfiguration(dataLocation, new MapPropertiesSource(mappedProperties),
        mock(ScheduleService.class));
  }

  private Map<String, String> mapKeysForProperties(final Map<String, String> properties) {
    final Map<String, String> realProperties = new HashMap<>();

    realProperties.put(PropertiesChatBotConfiguration.TWITCH_CLIENT_ID_KEY, this.arbitraryDataGenerator.getString());
    if (properties.containsKey(ChatBotConfigurationContractTests.TWITCH_CHANNEL_KEY)) {
      realProperties.put(PropertiesChatBotConfiguration.TWITCH_CHANNEL_KEY,
          properties.get(ChatBotConfigurationContractTests.TWITCH_CHANNEL_KEY));
    }
    if (properties.containsKey(ChatBotConfigurationContractTests.TWITCH_PASSWORD_KEY)) {
      realProperties.put(PropertiesChatBotConfiguration.TWITCH_PASSWORD_KEY,
          properties.get(ChatBotConfigurationContractTests.TWITCH_PASSWORD_KEY));
    }
    if (properties.containsKey(ChatBotConfigurationContractTests.TWITCH_LOGIN_KEY)) {
      realProperties.put(PropertiesChatBotConfiguration.TWITCH_LOGIN_KEY,
          properties.get(ChatBotConfigurationContractTests.TWITCH_LOGIN_KEY));
    }
    return realProperties;
  }

}
