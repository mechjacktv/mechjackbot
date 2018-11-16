package com.mechjacktv.mechjackbot.configuration;

import java.util.HashMap;
import java.util.Map;

import org.mockito.Mockito;

import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.ChatBotConfigurationContractTests;
import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.PropertiesUtils;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public class PropertiesChatBotConfiguration_ChatBotConfigurationUnitTests extends ChatBotConfigurationContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();
  private final PropertiesUtils propertiesUtils = new PropertiesUtils();

  @Override
  protected ChatBotConfiguration givenASubjectToTest(final String dataLocation, final Map<String, String> properties) {
    return new PropertiesChatBotConfiguration(dataLocation,
        () -> this.propertiesUtils.propertiesMapAsInputStream(this.mapKeysForProperties(properties)),
        Mockito.mock(ScheduleService.class));
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
    if (properties.containsKey(ChatBotConfigurationContractTests.TWITCH_USERNAME_KEY)) {
      realProperties.put(PropertiesChatBotConfiguration.TWITCH_USERNAME_KEY,
          properties.get(ChatBotConfigurationContractTests.TWITCH_USERNAME_KEY));
    }
    return realProperties;
  }

}
