package tv.mechjack.mechjackbot.command.custom;

import static org.assertj.core.api.Assertions.assertThat;
import static tv.mechjack.mechjackbot.command.BaseChatCommand.KEY_DESCRIPTION;
import static tv.mechjack.mechjackbot.command.BaseChatCommand.KEY_MESSAGE_FORMAT;
import static tv.mechjack.mechjackbot.command.BaseChatCommand.KEY_TRIGGER;

import org.junit.Test;

import tv.mechjack.configuration.Configuration;
import tv.mechjack.configuration.ConfigurationKey;
import tv.mechjack.configuration.MapConfiguration;
import tv.mechjack.configuration.TestConfigurationModule;
import tv.mechjack.keyvaluestore.TestKeyValueStoreModule;
import tv.mechjack.mechjackbot.ChatCommandDescription;
import tv.mechjack.mechjackbot.ChatCommandTrigger;
import tv.mechjack.mechjackbot.ChatCommandUtils;
import tv.mechjack.mechjackbot.ChatMessage;
import tv.mechjack.mechjackbot.TestChatMessageEvent;
import tv.mechjack.mechjackbot.UserRole;
import tv.mechjack.mechjackbot.command.BaseChatCommandContractTests;
import tv.mechjack.mechjackbot.command.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.command.CommandMessageFormat;

public class DeleteCommandChatCommandUnitTests extends BaseChatCommandContractTests {

  @Override
  protected void installModules() {
    super.installModules();
    this.testFrameworkRule.installModule(new TestConfigurationModule());
    this.testFrameworkRule.installModule(new TestCustomCommandModule());
    this.testFrameworkRule.installModule(new TestKeyValueStoreModule());
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
    return CommandMessageFormat.of(DeleteCommandChatCommand.DEFAULT_MESSAGE_FORMAT);
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
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        commandUtils.createUsageMessage(subjectUnderTest, messageEvent)));
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

    final ChatCommandUtils commandUtils = this.testFrameworkRule.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(String.format(DeleteCommandChatCommand.DEFAULT_NOT_CUSTOM_COMMAND_MESSAGE_FORMAT, trigger))));
  }

  @Test
  public final void handleMessageEvent_notExistingCustomCommandWithCustomMessage_resultIsSendsCustomNotCustomCommandMessage() {
    this.installModules();
    final String customMessageFormat = this.testFrameworkRule.getArbitraryString() + "%s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(DeleteCommandChatCommand.KEY_NOT_CUSTOM_COMMAND_MESSAGE_FORMAT, customMessageFormat);
    final String trigger = this.testFrameworkRule.getArbitraryString();
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final DeleteCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(), trigger)));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFrameworkRule.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(String.format(customMessageFormat, trigger))));
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

    final ChatCommandUtils commandUtils = this.testFrameworkRule.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(String.format(this.getMessageFormatDefault().value, trigger))));
  }

  @Test
  public final void handleMessageEvent_existingCustomCommandWithCustomMessage_resultIsCustomMessage() {
    this.installModules();
    final String customMessageFormat = this.testFrameworkRule.getArbitraryString() + " %s";
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

    final ChatCommandUtils commandUtils = this.testFrameworkRule.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(String.format(customMessageFormat, trigger))));
  }

}