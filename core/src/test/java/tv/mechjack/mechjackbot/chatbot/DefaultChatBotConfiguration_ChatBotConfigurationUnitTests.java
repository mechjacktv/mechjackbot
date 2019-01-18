package tv.mechjack.mechjackbot.chatbot;

import java.util.HashMap;
import java.util.Map;

import tv.mechjack.mechjackbot.api.ChatBotConfiguration;
import tv.mechjack.mechjackbot.api.ChatBotConfigurationContractTests;
import tv.mechjack.platform.application.Application;
import tv.mechjack.platform.application.TestApplicationModule;
import tv.mechjack.platform.utils.scheduleservice.MapHotUpdatePropertiesSource;
import tv.mechjack.platform.utils.scheduleservice.ScheduleService;
import tv.mechjack.platform.utils.scheduleservice.TestScheduleServiceModule;

public class DefaultChatBotConfiguration_ChatBotConfigurationUnitTests extends ChatBotConfigurationContractTests {

  @Override
  protected void installModules() {
    this.testFrameworkRule.installModule(new TestApplicationModule());
    this.testFrameworkRule.installModule(new TestScheduleServiceModule());
  }

  @Override
  protected ChatBotConfiguration givenASubjectToTest(final Map<String, String> properties) {
    final Map<String, String> mappedProperties = this.mapKeysForProperties(properties);

    return new DefaultChatBotConfiguration(this.testFrameworkRule.getInstance(Application.class),
        new MapHotUpdatePropertiesSource(mappedProperties), this.testFrameworkRule.getInstance(ScheduleService.class));
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
