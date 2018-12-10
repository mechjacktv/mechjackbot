package com.mechjacktv.mechjackbot.chatbot;

import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.mechjacktv.twitchclient.TwitchClientConfiguration;
import com.mechjacktv.twitchclient.TwitchClientConfigurationContractTests;
import com.mechjacktv.util.MapPropertiesSource;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public class PropertiesChatBotConfiguration_TwitchClientConfigurationUnitTests extends
    TwitchClientConfigurationContractTests {

  @Override
  protected TwitchClientConfiguration givenASubjectToTest(Optional<String> clientId) {
    final Map<String, String> properties = new HashMap<>();

    clientId.ifPresent((value) -> properties.put(PropertiesChatBotConfiguration.TWITCH_CLIENT_ID_KEY, value));
    properties.put(PropertiesChatBotConfiguration.TWITCH_CHANNEL_KEY, this.testFrameworkRule.getArbitraryString());
    properties.put(PropertiesChatBotConfiguration.TWITCH_PASSWORD_KEY, this.testFrameworkRule.getArbitraryString());
    properties.put(PropertiesChatBotConfiguration.TWITCH_LOGIN_KEY, this.testFrameworkRule.getArbitraryString());
    return new PropertiesChatBotConfiguration(this.testFrameworkRule.getArbitraryString(),
        new MapPropertiesSource(properties), mock(ScheduleService.class));
  }

}
