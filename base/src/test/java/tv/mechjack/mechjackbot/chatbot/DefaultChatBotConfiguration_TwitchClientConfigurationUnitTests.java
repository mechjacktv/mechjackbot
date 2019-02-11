package tv.mechjack.mechjackbot.chatbot;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import tv.mechjack.platform.application.Application;
import tv.mechjack.platform.application.TestApplicationModule;
import tv.mechjack.platform.utils.scheduleservice.MapHotUpdatePropertiesSource;
import tv.mechjack.platform.utils.scheduleservice.ScheduleService;
import tv.mechjack.platform.utils.scheduleservice.TestScheduleServiceModule;
import tv.mechjack.twitchclient.TwitchClientConfiguration;
import tv.mechjack.twitchclient.TwitchClientConfigurationContractTests;

public class DefaultChatBotConfiguration_TwitchClientConfigurationUnitTests extends
    TwitchClientConfigurationContractTests {

  @Override
  protected void installModules() {
    this.testFrameworkRule.registerModule(new TestApplicationModule());
    this.testFrameworkRule.registerModule(new TestScheduleServiceModule());
  }

  @Override
  protected TwitchClientConfiguration givenASubjectToTest(Optional<String> clientId) {
    final Map<String, String> properties = new HashMap<>();

    clientId.ifPresent((value) -> properties.put(DefaultChatBotConfiguration.TWITCH_CLIENT_ID_KEY, value));
    properties.put(DefaultChatBotConfiguration.TWITCH_CHANNEL_KEY, this.testFrameworkRule.arbitraryData().getString());
    properties.put(DefaultChatBotConfiguration.TWITCH_PASSWORD_KEY, this.testFrameworkRule.arbitraryData().getString());
    properties.put(DefaultChatBotConfiguration.TWITCH_LOGIN_KEY, this.testFrameworkRule.arbitraryData().getString());
    return new DefaultChatBotConfiguration(this.testFrameworkRule.getInstance(Application.class),
        new MapHotUpdatePropertiesSource(properties), this.testFrameworkRule.getInstance(ScheduleService.class));
  }

}
