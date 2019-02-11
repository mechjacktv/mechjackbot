package tv.mechjack.mechjackbot.feature.core;

import static org.assertj.core.api.Assertions.assertThat;
import static tv.mechjack.mechjackbot.feature.core.HelpChatCommand.KEY_DESCRIPTION;
import static tv.mechjack.mechjackbot.feature.core.HelpChatCommand.KEY_MESSAGE_FORMAT;
import static tv.mechjack.mechjackbot.feature.core.HelpChatCommand.KEY_TRIGGER;

import org.junit.Test;

import tv.mechjack.mechjackbot.api.BaseChatCommandContractTests;
import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandRegistry;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.CommandMessageFormat;
import tv.mechjack.mechjackbot.api.TestChatCommand;
import tv.mechjack.mechjackbot.api.TestChatMessageEvent;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.configuration.ConfigurationKey;
import tv.mechjack.platform.configuration.MapConfiguration;

public class HelpChatCommandUnitTests extends BaseChatCommandContractTests {

  protected final HelpChatCommand givenASubjectToTest() {
    return new HelpChatCommand(this.testFramework.getInstance(CommandConfigurationBuilder.class),
        this.testFramework.getInstance(ChatCommandRegistry.class),
        this.testFramework.getInstance(ChatCommandUtils.class),
        this.testFramework.getInstance(Configuration.class));
  }

  @Override
  protected ChatCommandDescription getDescriptionDefault() {
    return ChatCommandDescription.of(HelpChatCommand.DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(KEY_DESCRIPTION, HelpChatCommand.class);
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, HelpChatCommand.class);
  }

  @Override
  protected ChatCommandTrigger getTriggerDefault() {
    return ChatCommandTrigger.of(HelpChatCommand.DEFAULT_TRIGGER);
  }

  private CommandMessageFormat getMessageFormatDefault() {
    return CommandMessageFormat.of(HelpChatCommand.DEFAULT_MESSAGE_FORMAT);
  }

  private ConfigurationKey getMessageFormatKey() {
    return ConfigurationKey.of(KEY_MESSAGE_FORMAT, HelpChatCommand.class);
  }

  private CommandMessageFormat getMissingMessageFormatDefault() {
    return CommandMessageFormat.of(HelpChatCommand.DEFAULT_MISSING_MESSAGE_FORMAT);
  }

  private ConfigurationKey getMissingMessageFormatKey() {
    return ConfigurationKey.of(HelpChatCommand.KEY_MISSING_MESSAGE_FORMAT, HelpChatCommand.class);
  }

  @Test
  public final void handleMessageEvent_messageNotProperlyFormatted_resultIsUsageMessage() {
    this.installModules();
    final TestChatCommand command = this.testFramework.getInstance(TestChatCommand.class);
    final ChatCommandRegistry chatCommandRegistry = this.testFramework.getInstance(ChatCommandRegistry.class);
    chatCommandRegistry.addCommand(command);
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final HelpChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(subjectUnderTest.getTrigger().value));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    this.assertUsageMessageForCommand(result, subjectUnderTest, messageEvent);
  }

  @Test
  public final void handleMessageEvent_noMessageFormatConfiguredWithTriggerableCommand_resultIsDefaultMessage() {
    this.installModules();
    final TestChatCommand command = this.testFramework.getInstance(TestChatCommand.class);
    final ChatCommandRegistry chatCommandRegistry = this.testFramework.getInstance(ChatCommandRegistry.class);
    chatCommandRegistry.addCommand(command);
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final HelpChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent
        .setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(), command.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFramework.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(command, messageEvent,
        ChatMessage.of(String.format(this.getMessageFormatDefault().value, command.getTrigger(),
            command.getDescription()))));
  }

  @Test
  public final void handleMessageEvent_customMessageFormatConfiguredWithTriggerableCommand_resultIsCustomMessage() {
    this.installModules();
    final String customMessageFormat = this.testFramework.arbitraryData().getString() + " $(user) %s %s";
    final MapConfiguration configuration = this.testFramework.getInstance(MapConfiguration.class);
    configuration.set(this.getMessageFormatKey(), customMessageFormat);
    final TestChatCommand command = this.testFramework.getInstance(TestChatCommand.class);
    final ChatCommandRegistry chatCommandRegistry = this.testFramework.getInstance(ChatCommandRegistry.class);
    chatCommandRegistry.addCommand(command);
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final HelpChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent
        .setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(), command.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFramework.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(command, messageEvent,
        ChatMessage.of(String.format(customMessageFormat, command.getTrigger(), command.getDescription()))));
  }

  @Test
  public final void handleMessageEvent_noMissingMessageFormatConfigured_resultIsDefaultMissingMessage() {
    this.installModules();
    final TestChatCommand command = this.testFramework.getInstance(TestChatCommand.class);
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final HelpChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent
        .setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(), command.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFramework.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(command, messageEvent,
        ChatMessage.of(String.format(this.getMissingMessageFormatDefault().value, command.getTrigger()))));
  }

  @Test
  public final void handleMessageEvent_customMissingMessageFormatConfigured_resultIsCustomMissingMessage() {
    this.installModules();
    final String customMessageFormat = this.testFramework.arbitraryData().getString() + " $(user) %s";
    final MapConfiguration configuration = this.testFramework.getInstance(MapConfiguration.class);
    configuration.set(this.getMissingMessageFormatKey(), customMessageFormat);
    final TestChatCommand command = this.testFramework.getInstance(TestChatCommand.class);
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final HelpChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent
        .setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(), command.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFramework.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(command, messageEvent,
        ChatMessage.of(String.format(customMessageFormat, command.getTrigger()))));
  }

  @Test
  public final void handleMessageEvent_noMissingMessageFormatConfiguredWithNonTriggerableCommand_resultIsDefaultMissingMessage() {
    this.installModules();
    final TestChatCommand command = this.testFramework.getInstance(TestChatCommand.class);
    command.setTriggerable(false);
    final ChatCommandRegistry chatCommandRegistry = this.testFramework.getInstance(ChatCommandRegistry.class);
    chatCommandRegistry.addCommand(command);
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final HelpChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent
        .setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(), command.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFramework.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(command, messageEvent,
        ChatMessage.of(String.format(this.getMissingMessageFormatDefault().value, command.getTrigger()))));
  }

}
