package com.mechjacktv.mechjackbot.command.custom;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.configuration.ConfigurationTestModule;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.keyvaluestore.KeyValueStoreTestModule;
import com.mechjacktv.mechjackbot.ChatCommandDescription;
import com.mechjacktv.mechjackbot.ChatCommandTrigger;
import com.mechjacktv.mechjackbot.ChatCommandUtils;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.mechjackbot.TestChatMessageEvent;
import com.mechjacktv.mechjackbot.UserRole;
import com.mechjacktv.mechjackbot.command.BaseChatCommandContractTests;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandMessageFormat;

import org.junit.Test;

import static com.mechjacktv.mechjackbot.command.BaseChatCommand.KEY_DESCRIPTION;
import static com.mechjacktv.mechjackbot.command.BaseChatCommand.KEY_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.command.BaseChatCommand.KEY_TRIGGER;
import static com.mechjacktv.mechjackbot.command.custom.DeleteCommandChatCommand.DEFAULT_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.command.custom.DeleteCommandChatCommand.DEFAULT_NOT_CUSTOM_COMMAND_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.command.custom.DeleteCommandChatCommand.KEY_NOT_CUSTOM_COMMAND_MESSAGE_FORMAT;
import static org.assertj.core.api.Assertions.assertThat;

public class DeleteCommandChatCommandUnitTests extends BaseChatCommandContractTests {

  @Override
  protected void installModules() {
    super.installModules();
    this.testFrameworkRule.installModule(new ConfigurationTestModule());
    this.testFrameworkRule.installModule(new CustomCommandTestModule());
    this.testFrameworkRule.installModule(new KeyValueStoreTestModule());
  }

  @Override
  protected ChatCommandDescription getDescriptionDefault() {
    return ChatCommandDescription.of(DeleteCommandChatCommand.DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(KEY_DESCRIPTION, DeleteCommandChatCommand.class);
  }

  @Override
  protected ChatCommandTrigger getTriggerDefault() {
    return ChatCommandTrigger.of(DeleteCommandChatCommand.DEFAULT_TRIGGER);
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, DeleteCommandChatCommand.class);
  }

  @Override
  protected DeleteCommandChatCommand givenASubjectToTest() {
    return new DeleteCommandChatCommand(this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class),
        this.testFrameworkRule.getInstance(Configuration.class),
        this.testFrameworkRule.getInstance(ChatCommandUtils.class),
        this.testFrameworkRule.getInstance(CustomChatCommandService.class));
  }

  private CommandMessageFormat getMessageFormatDefault() {
    return CommandMessageFormat.of(DEFAULT_MESSAGE_FORMAT);
  }

  private ConfigurationKey getMessageFormatKey() {
    return ConfigurationKey.of(KEY_MESSAGE_FORMAT, DeleteCommandChatCommand.class);
  }

  @Test
  public final void handleMessageEvent_emptyMessageBody_resultIsSendsUsage() {
    this.installModules();
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final DeleteCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(subjectUnderTest.getTrigger().value));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFrameworkRule.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.createUsageMessage(subjectUnderTest, messageEvent));
  }

  @Test
  public final void handleMessageEvent_notExistingCustomCommandWithDefaultMessage_resultIsSendsDefaultNotCustomCommandMessage() {
    this.installModules();
    final String trigger = this.testFrameworkRule.getArbitraryString();
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final DeleteCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(), trigger)));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(String.format(DEFAULT_NOT_CUSTOM_COMMAND_MESSAGE_FORMAT,
        messageEvent.getChatUser().getTwitchLogin(), trigger)));
  }

  @Test
  public final void handleMessageEvent_notExistingCustomCommandWithCustomMessage_resultIsSendsCustomNotCustomCommandMessage() {
    this.installModules();
    final String customMessageFormat = this.testFrameworkRule.getArbitraryString() + "%s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(KEY_NOT_CUSTOM_COMMAND_MESSAGE_FORMAT, customMessageFormat);
    final String trigger = this.testFrameworkRule.getArbitraryString();
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final DeleteCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(), trigger)));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(String.format(customMessageFormat,
        messageEvent.getChatUser().getTwitchLogin(), trigger)));
  }

  @Test
  public final void handleMessageEvent_existingCustomCommandWithDefaultMessage_resultIsDefaultMessage() {
    this.installModules();
    final String trigger = this.testFrameworkRule.getArbitraryString();
    final CustomChatCommandService service = this.testFrameworkRule.getInstance(CustomChatCommandService.class);
    service.createCustomChatCommand(ChatCommandTrigger.of(trigger),
        CommandBody.of(this.testFrameworkRule.getArbitraryString()), UserRole.VIEWER);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final DeleteCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(), trigger)));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(String.format(this.getMessageFormatDefault().value,
        messageEvent.getChatUser().getTwitchLogin(), trigger)));
  }

  @Test
  public final void handleMessageEvent_existingCustomCommandWithCustomMessage_resultIsCustomMessage() {
    this.installModules();
    final String customMessageFormat = this.testFrameworkRule.getArbitraryString() + " %2$s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getMessageFormatKey(), customMessageFormat);
    final String trigger = this.testFrameworkRule.getArbitraryString();
    final CustomChatCommandService service = this.testFrameworkRule.getInstance(CustomChatCommandService.class);
    service.createCustomChatCommand(ChatCommandTrigger.of(trigger),
        CommandBody.of(this.testFrameworkRule.getArbitraryString()), UserRole.VIEWER);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final DeleteCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(), trigger)));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    assertThat(result).isEqualTo(ChatMessage.of(String.format(customMessageFormat,
        messageEvent.getChatUser().getTwitchLogin(), trigger)));
  }

}
