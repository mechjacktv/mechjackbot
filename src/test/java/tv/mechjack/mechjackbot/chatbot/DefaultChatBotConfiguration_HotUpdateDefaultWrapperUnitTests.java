package tv.mechjack.mechjackbot.chatbot;

import static tv.mechjack.mechjackbot.chatbot.DefaultChatBotConfiguration.TWITCH_CHANNEL_KEY;
import static tv.mechjack.mechjackbot.chatbot.DefaultChatBotConfiguration.TWITCH_CLIENT_ID_KEY;
import static tv.mechjack.mechjackbot.chatbot.DefaultChatBotConfiguration.TWITCH_LOGIN_KEY;
import static tv.mechjack.mechjackbot.chatbot.DefaultChatBotConfiguration.TWITCH_PASSWORD_KEY;

import java.util.HashMap;
import java.util.Map;

import tv.mechjack.util.HotUpdatePropertiesWrapperContractTests;
import tv.mechjack.util.PropertiesSource;
import tv.mechjack.util.scheduleservice.ScheduleService;

public class DefaultChatBotConfiguration_HotUpdateDefaultWrapperUnitTests
    extends HotUpdatePropertiesWrapperContractTests {

  @Override
  protected DefaultChatBotConfiguration givenASubjectToTest(final PropertiesSource propertiesSource) {
    return new DefaultChatBotConfiguration(this.testFrameworkRule.getArbitraryString(), propertiesSource,
        this.testFrameworkRule.getInstance(ScheduleService.class));
  }

  @Override
  protected Map<String, String> givenAPropertiesMap() {
    final Map<String, String> properties = new HashMap<>();

    properties.put(TWITCH_CHANNEL_KEY, this.testFrameworkRule.getArbitraryString());
    properties.put(TWITCH_CLIENT_ID_KEY, this.testFrameworkRule.getArbitraryString());
    properties.put(TWITCH_PASSWORD_KEY, this.testFrameworkRule.getArbitraryString());
    properties.put(TWITCH_LOGIN_KEY, this.testFrameworkRule.getArbitraryString());
    return properties;
  }

}
