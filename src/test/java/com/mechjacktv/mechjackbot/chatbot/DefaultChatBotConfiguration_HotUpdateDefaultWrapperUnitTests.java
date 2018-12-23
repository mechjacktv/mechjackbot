package com.mechjacktv.mechjackbot.chatbot;

import static com.mechjacktv.mechjackbot.chatbot.DefaultChatBotConfiguration.TWITCH_CHANNEL_KEY;
import static com.mechjacktv.mechjackbot.chatbot.DefaultChatBotConfiguration.TWITCH_CLIENT_ID_KEY;
import static com.mechjacktv.mechjackbot.chatbot.DefaultChatBotConfiguration.TWITCH_LOGIN_KEY;
import static com.mechjacktv.mechjackbot.chatbot.DefaultChatBotConfiguration.TWITCH_PASSWORD_KEY;

import java.util.HashMap;
import java.util.Map;

import com.mechjacktv.util.HotUpdatePropertiesWrapperContractTests;
import com.mechjacktv.util.PropertiesSource;
import com.mechjacktv.util.scheduleservice.ScheduleService;

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
