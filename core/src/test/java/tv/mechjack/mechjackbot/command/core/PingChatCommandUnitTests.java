package tv.mechjack.mechjackbot.command.core;

import static org.assertj.core.api.Assertions.assertThat;
import static tv.mechjack.mechjackbot.command.core.PingChatCommand.KEY_DESCRIPTION;
import static tv.mechjack.mechjackbot.command.core.PingChatCommand.KEY_MESSAGE_FORMAT;
import static tv.mechjack.mechjackbot.command.core.PingChatCommand.KEY_TRIGGER;

import org.junit.Test;

import tv.mechjack.configuration.ConfigurationKey;
import tv.mechjack.configuration.MapConfiguration;
import tv.mechjack.mechjackbot.ChatCommandDescription;
import tv.mechjack.mechjackbot.ChatCommandTrigger;
import tv.mechjack.mechjackbot.ChatCommandUtils;
import tv.mechjack.mechjackbot.ChatMessage;
import tv.mechjack.mechjackbot.TestChatMessageEvent;
import tv.mechjack.mechjackbot.command.BaseChatCommandContractTests;
import tv.mechjack.mechjackbot.command.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.command.CommandMessageFormat;

public class PingChatCommandUnitTests extends BaseChatCommandContractTests {

  @Override
  protected PingChatCommand givenASubjectToTest() {
    return new PingChatCommand(this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class));
  }

  @Override
  protected ChatCommandDescription getDescriptionDefault() {
    return ChatCommandDescription.of(PingChatCommand.DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(KEY_DESCRIPTION, PingChatCommand.class);
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, PingChatCommand.class);
  }

  @Override
  protected ChatCommandTrigger getTriggerDefault() {
    return ChatCommandTrigger.of(PingChatCommand.DEFAULT_TRIGGER);
  }

  private CommandMessageFormat getMessageFormatDefault() {
    return CommandMessageFormat.of(PingChatCommand.DEFAULT_MESSAGE_FORMAT);
  }

  private ConfigurationKey getMessageFormatKey() {
    return ConfigurationKey.of(KEY_MESSAGE_FORMAT, PingChatCommand.class);
  }

  @Test
  public final void handleMessageEvent_noMessageFormatConfigured_resultIsDefaultMessage() {
    this.installModules();
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final PingChatCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils chatCommandUtils = this.testFrameworkRule.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(chatCommandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(this.getMessageFormatDefault().value)));
  }

  @Test
  public final void handleMessageEvent_customMessageFormatConfigured_resultIsCustomMessage() {
    this.installModules();
    final String customMessageFormat = this.testFrameworkRule.getArbitraryString() + "$(user)";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getMessageFormatKey(), customMessageFormat);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final PingChatCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils chatCommandUtils = this.testFrameworkRule.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(chatCommandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(customMessageFormat)));
  }

}
