package tv.mechjack.mechjackbot.chatbot;

import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import tv.mechjack.twitchclient.TwitchClientConfiguration;
import tv.mechjack.twitchclient.TwitchClientConfigurationContractTests;
import tv.mechjack.util.MapPropertiesSource;
import tv.mechjack.util.scheduleservice.ScheduleService;

public class DefaultChatBotConfiguration_TwitchClientConfigurationUnitTests extends
    TwitchClientConfigurationContractTests {

  @Override
  protected TwitchClientConfiguration givenASubjectToTest(Optional<String> clientId) {
    final Map<String, String> properties = new HashMap<>();

    clientId.ifPresent((value) -> properties.put(DefaultChatBotConfiguration.TWITCH_CLIENT_ID_KEY, value));
    properties.put(DefaultChatBotConfiguration.TWITCH_CHANNEL_KEY, this.testFrameworkRule.getArbitraryString());
    properties.put(DefaultChatBotConfiguration.TWITCH_PASSWORD_KEY, this.testFrameworkRule.getArbitraryString());
    properties.put(DefaultChatBotConfiguration.TWITCH_LOGIN_KEY, this.testFrameworkRule.getArbitraryString());
    return new DefaultChatBotConfiguration(this.testFrameworkRule.getArbitraryString(),
        new MapPropertiesSource(properties), mock(ScheduleService.class));
  }

}
