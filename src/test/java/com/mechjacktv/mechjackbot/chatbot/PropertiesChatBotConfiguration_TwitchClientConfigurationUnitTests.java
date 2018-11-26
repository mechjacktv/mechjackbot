package com.mechjacktv.mechjackbot.chatbot;

import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.mechjacktv.twitchclient.TwitchClientConfiguration;
import com.mechjacktv.twitchclient.TwitchClientConfigurationContractTests;
import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.MapPropertiesSource;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public class PropertiesChatBotConfiguration_TwitchClientConfigurationUnitTests extends
    TwitchClientConfigurationContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  @Override
  protected TwitchClientConfiguration givenASubjectToTest(Optional<String> clientId) {
    final Map<String, String> properties = new HashMap<>();

    clientId.ifPresent((value) -> properties.put(PropertiesChatBotConfiguration.TWITCH_CLIENT_ID_KEY, value));
    properties.put(PropertiesChatBotConfiguration.TWITCH_CHANNEL_KEY, this.arbitraryDataGenerator.getString());
    properties.put(PropertiesChatBotConfiguration.TWITCH_PASSWORD_KEY, this.arbitraryDataGenerator.getString());
    properties.put(PropertiesChatBotConfiguration.TWITCH_USERNAME_KEY, this.arbitraryDataGenerator.getString());
    return new PropertiesChatBotConfiguration(this.arbitraryDataGenerator.getString(),
        new MapPropertiesSource(properties), mock(ScheduleService.class));
  }

}
