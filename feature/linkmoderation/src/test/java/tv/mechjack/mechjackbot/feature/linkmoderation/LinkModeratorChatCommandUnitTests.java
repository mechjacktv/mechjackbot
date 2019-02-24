package tv.mechjack.mechjackbot.feature.linkmoderation;

import static tv.mechjack.mechjackbot.feature.linkmoderation.LinkModeratorChatCommand.KEY_DESCRIPTION;
import static tv.mechjack.mechjackbot.feature.linkmoderation.LinkModeratorChatCommand.KEY_MESSAGE_FORMAT;
import static tv.mechjack.mechjackbot.feature.linkmoderation.LinkModeratorChatCommand.KEY_TRIGGER;

import java.util.UUID;

import tv.mechjack.mechjackbot.api.BaseChatCommandContractTests;
import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.CommandMessageFormat;
import tv.mechjack.platform.configuration.ConfigurationKey;

public class LinkModeratorChatCommandUnitTests
    extends BaseChatCommandContractTests {

  @Override
  protected void installModules() {
    super.installModules();
    this.testFramework.registerModule(new TestLinkModerationFeatureModule());
  }

  @Override
  protected LinkModeratorChatCommand givenASubjectToTest() {
    return new LinkModeratorChatCommand(
        this.testFramework.getInstance(CommandConfigurationBuilder.class),
        this.testFramework.getInstance(LinkModeratorService.class));
  }

  @Override
  protected ChatCommandDescription getDescriptionDefault() {
    return ChatCommandDescription
        .of(LinkModeratorChatCommand.DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey
        .of(KEY_DESCRIPTION, LinkModeratorChatCommand.class);
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, LinkModeratorChatCommand.class);
  }

  @Override
  protected ChatCommandTrigger getTriggerDefault() {
    return ChatCommandTrigger.of(UUID.randomUUID().toString());
  }

  private CommandMessageFormat getMessageFormatDefault() {
    return CommandMessageFormat
        .of(LinkModeratorChatCommand.DEFAULT_MESSAGE_FORMAT);
  }

  private ConfigurationKey getMessageFormatKey() {
    return ConfigurationKey
        .of(KEY_MESSAGE_FORMAT, LinkModeratorChatCommand.class);
  }

}
