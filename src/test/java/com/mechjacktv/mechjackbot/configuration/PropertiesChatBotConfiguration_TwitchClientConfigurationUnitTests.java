package com.mechjacktv.mechjackbot.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.mockito.Mockito;

import com.mechjacktv.ArbitraryDataGenerator;
import com.mechjacktv.test.PropertiesUtils;
import com.mechjacktv.twitchclient.TwitchClientConfiguration;
import com.mechjacktv.twitchclient.TwitchClientConfigurationContractTests;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public class PropertiesChatBotConfiguration_TwitchClientConfigurationUnitTests extends
    TwitchClientConfigurationContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();
  private final PropertiesUtils propertiesUtils = new PropertiesUtils();

  @Override
  protected TwitchClientConfiguration givenASubjectToTest(Optional<String> clientId) {
    final Map<String, String> properties = new HashMap<>();

    clientId.ifPresent((value) -> properties.put(PropertiesChatBotConfiguration.TWITCH_CLIENT_ID_KEY, value));
    properties.put(PropertiesChatBotConfiguration.TWITCH_CHANNEL_KEY, this.arbitraryDataGenerator.getString());
    properties.put(PropertiesChatBotConfiguration.TWITCH_PASSWORD_KEY, this.arbitraryDataGenerator.getString());
    properties.put(PropertiesChatBotConfiguration.TWITCH_USERNAME_KEY, this.arbitraryDataGenerator.getString());
    return new PropertiesChatBotConfiguration(this.arbitraryDataGenerator.getString(),
        () -> this.propertiesUtils.propertiesMapAsInputStream(properties),
        Mockito.mock(ScheduleService.class));
  }

}
