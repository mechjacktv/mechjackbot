package tv.mechjack.mechjackbot.feature.points;

import static tv.mechjack.mechjackbot.feature.points.PointsListenerChatCommand.KEY_DESCRIPTION;
import static tv.mechjack.mechjackbot.feature.points.PointsListenerChatCommand.KEY_TRIGGER;

import tv.mechjack.mechjackbot.api.BaseChatCommandContractTests;
import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.platform.configuration.ConfigurationKey;

public class PointsListenerChatCommandUnitTests extends BaseChatCommandContractTests {

  @Override
  protected PointsListenerChatCommand givenASubjectToTest() {
    return new PointsListenerChatCommand(
        this.testFramework.getInstance(CommandConfigurationBuilder.class));
  }

  @Override
  protected ChatCommandDescription getDescriptionDefault() {
    return ChatCommandDescription.of(PointsListenerChatCommand.DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(KEY_DESCRIPTION, PointsListenerChatCommand.class);
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, PointsListenerChatCommand.class);
  }

  @Override
  protected ChatCommandTrigger getTriggerDefault() {
    return ChatCommandTrigger.of(PointsListenerChatCommand.DEFAULT_TRIGGER);
  }

}
