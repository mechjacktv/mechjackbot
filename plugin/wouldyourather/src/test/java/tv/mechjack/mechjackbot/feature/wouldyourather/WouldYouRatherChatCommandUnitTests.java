package tv.mechjack.mechjackbot.feature.wouldyourather;

import java.io.IOException;

import tv.mechjack.application.Application;
import tv.mechjack.application.TestApplicationModule;
import tv.mechjack.configuration.ConfigurationKey;
import tv.mechjack.configuration.TestConfigurationModule;
import tv.mechjack.mechjackbot.api.BaseChatCommandContractTests;
import tv.mechjack.mechjackbot.api.ChatCommand;
import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.TestCommandModule;
import tv.mechjack.util.TestUtilModule;

import static tv.mechjack.mechjackbot.api.BaseChatCommand.KEY_DESCRIPTION;
import static tv.mechjack.mechjackbot.api.BaseChatCommand.KEY_TRIGGER;

public class WouldYouRatherChatCommandUnitTests extends BaseChatCommandContractTests {

  @Override
  protected void installModules() {
    this.testFrameworkRule.installModule(new TestApplicationModule());
    this.testFrameworkRule.installModule(new TestConfigurationModule());
    this.testFrameworkRule.installModule(new TestCommandModule());
    this.testFrameworkRule.installModule(new TestUtilModule());
  }

  @Override
  protected ChatCommand givenASubjectToTest() {
    try {
      return new WouldYouRatherChatCommand(this.testFrameworkRule.getInstance(Application.class),
          this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class));
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(KEY_DESCRIPTION, WouldYouRatherChatCommand.class);
  }

  @Override
  protected ChatCommandDescription getDescriptionDefault() {
    return ChatCommandDescription.of(WouldYouRatherChatCommand.DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, WouldYouRatherChatCommand.class);
  }

  @Override
  protected ChatCommandTrigger getTriggerDefault() {
    return ChatCommandTrigger.of(WouldYouRatherChatCommand.DEFAULT_TRIGGER);
  }

}
