package tv.mechjack.mechjackbot.chatbot;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import tv.mechjack.application.Application;
import tv.mechjack.application.TestApplicationModule;
import tv.mechjack.twitchclient.TwitchClientConfiguration;
import tv.mechjack.twitchclient.TwitchClientConfigurationContractTests;
import tv.mechjack.util.scheduleservice.MapHotUpdatePropertiesSource;
import tv.mechjack.util.scheduleservice.ScheduleService;
import tv.mechjack.util.scheduleservice.TestScheduleServiceModule;

public class DefaultChatBotConfiguration_TwitchClientConfigurationUnitTests extends
    TwitchClientConfigurationContractTests {

  @Override
  protected void installModules() {
    this.testFrameworkRule.installModule(new TestApplicationModule());
    this.testFrameworkRule.installModule(new TestScheduleServiceModule());
  }

  @Override
  protected TwitchClientConfiguration givenASubjectToTest(Optional<String> clientId) {
    final Map<String, String> properties = new HashMap<>();

    clientId.ifPresent((value) -> properties.put(DefaultChatBotConfiguration.TWITCH_CLIENT_ID_KEY, value));
    properties.put(DefaultChatBotConfiguration.TWITCH_CHANNEL_KEY, this.testFrameworkRule.getArbitraryString());
    properties.put(DefaultChatBotConfiguration.TWITCH_PASSWORD_KEY, this.testFrameworkRule.getArbitraryString());
    properties.put(DefaultChatBotConfiguration.TWITCH_LOGIN_KEY, this.testFrameworkRule.getArbitraryString());
    return new DefaultChatBotConfiguration(this.testFrameworkRule.getInstance(Application.class),
        new MapHotUpdatePropertiesSource(properties), this.testFrameworkRule.getInstance(ScheduleService.class));
  }

}
