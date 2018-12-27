package tv.mechjack.mechjackbot.chatbot;

import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import tv.mechjack.mechjackbot.ChatBotConfiguration;
import tv.mechjack.mechjackbot.ChatBotConfigurationContractTests;
import tv.mechjack.util.MapPropertiesSource;
import tv.mechjack.util.scheduleservice.ScheduleService;

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
