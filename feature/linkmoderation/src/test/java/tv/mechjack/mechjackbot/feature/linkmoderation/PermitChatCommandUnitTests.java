package tv.mechjack.mechjackbot.feature.linkmoderation;

import tv.mechjack.mechjackbot.api.BaseChatCommandContractTests;
import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.platform.configuration.ConfigurationKey;

import static tv.mechjack.mechjackbot.api.BaseChatCommand.KEY_DESCRIPTION;
import static tv.mechjack.mechjackbot.api.BaseChatCommand.KEY_TRIGGER;

public class PermitChatCommandUnitTests extends BaseChatCommandContractTests {

  @Override
  protected void installModules() {
    super.installModules();
    this.testFramework.registerModule(new TestLinkModerationFeatureModule());
  }

  @Override
  protected PermitChatCommand givenASubjectToTest() {
    return new PermitChatCommand(
        this.testFramework.getInstance(CommandConfigurationBuilder.class),
        this.testFramework.getInstance(ChatCommandUtils.class),
        this.testFramework.getInstance(LinkModeratorService.class));
  }

  @Override
  protected ChatCommandDescription getDescriptionDefault() {
    return ChatCommandDescription.of(PermitChatCommand.DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(KEY_DESCRIPTION, PermitChatCommand.class);
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, PermitChatCommand.class);
  }

  @Override
  protected ChatCommandTrigger getTriggerDefault() {
    return ChatCommandTrigger.of(PermitChatCommand.DEFAULT_TRIGGER);
  }

}
