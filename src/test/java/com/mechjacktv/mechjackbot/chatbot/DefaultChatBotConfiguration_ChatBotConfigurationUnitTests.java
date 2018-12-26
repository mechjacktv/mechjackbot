package com.mechjacktv.mechjackbot.chatbot;

import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.ChatBotConfigurationContractTests;
import com.mechjacktv.util.MapPropertiesSource;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public class DefaultChatBotConfiguration_ChatBotConfigurationUnitTests extends ChatBotConfigurationContractTests {

  @Override
  protected ChatBotConfiguration givenASubjectToTest(final String dataLocation, final Map<String, String> properties) {
    final Map<String, String> mappedProperties = this.mapKeysForProperties(properties);

    return new DefaultChatBotConfiguration(dataLocation, new MapPropertiesSource(mappedProperties),
        mock(ScheduleService.class));
  }

  private Map<String, String> mapKeysForProperties(final Map<String, String> properties) {
    final Map<String, String> realProperties = new HashMap<>();

    realProperties.put(DefaultChatBotConfiguration.TWITCH_CLIENT_ID_KEY,
        this.testFrameworkRule.getArbitraryString());
    if (properties.containsKey(ChatBotConfigurationContractTests.TWITCH_CHANNEL_KEY)) {
      realProperties.put(DefaultChatBotConfiguration.TWITCH_CHANNEL_KEY,
          properties.get(ChatBotConfigurationContractTests.TWITCH_CHANNEL_KEY));
    }
    if (properties.containsKey(ChatBotConfigurationContractTests.TWITCH_PASSWORD_KEY)) {
      realProperties.put(DefaultChatBotConfiguration.TWITCH_PASSWORD_KEY,
          properties.get(ChatBotConfigurationContractTests.TWITCH_PASSWORD_KEY));
    }
    if (properties.containsKey(ChatBotConfigurationContractTests.TWITCH_LOGIN_KEY)) {
      realProperties.put(DefaultChatBotConfiguration.TWITCH_LOGIN_KEY,
          properties.get(ChatBotConfigurationContractTests.TWITCH_LOGIN_KEY));
    }
    return realProperties;
  }

}
