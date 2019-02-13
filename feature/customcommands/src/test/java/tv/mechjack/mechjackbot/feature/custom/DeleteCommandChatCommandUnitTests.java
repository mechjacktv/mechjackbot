package tv.mechjack.mechjackbot.feature.custom;

import static org.assertj.core.api.Assertions.assertThat;
import static tv.mechjack.mechjackbot.api.BaseChatCommand.KEY_DESCRIPTION;
import static tv.mechjack.mechjackbot.api.BaseChatCommand.KEY_MESSAGE_FORMAT;
import static tv.mechjack.mechjackbot.api.BaseChatCommand.KEY_TRIGGER;

import org.junit.Test;

import tv.mechjack.mechjackbot.api.BaseChatCommandContractTests;
import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.CommandMessageFormat;
import tv.mechjack.mechjackbot.api.TestChatMessageEvent;
import tv.mechjack.mechjackbot.api.UserRole;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.configuration.ConfigurationKey;
import tv.mechjack.platform.configuration.MapConfiguration;
import tv.mechjack.platform.configuration.TestConfigurationModule;
import tv.mechjack.platform.keyvaluestore.TestKeyValueStoreModule;
import tv.mechjack.platform.protobuf.TestProtobufModule;

public class DeleteCommandChatCommandUnitTests extends BaseChatCommandContractTests {

  @Override
  protected void installModules() {
    super.installModules();
    this.testFramework.registerModule(new TestConfigurationModule());
    this.testFramework.registerModule(new TestCustomCommandModule());
    this.testFramework.registerModule(new TestKeyValueStoreModule());
    this.testFramework.registerModule(new TestProtobufModule());
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

  @Test
  public final void handleMessageEvent_emptyMessageBody_resultIsSendsUsage() {
    this.installModules();
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final DeleteCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(subjectUnderTest.getTrigger().value));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFramework.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        commandUtils.createUsageMessage(subjectUnderTest, messageEvent)));
  }

  @Override
  protected DeleteCommandChatCommand givenASubjectToTest() {
    return new DeleteCommandChatCommand(this.testFramework.getInstance(CommandConfigurationBuilder.class),
        this.testFramework.getInstance(Configuration.class),
        this.testFramework.getInstance(ChatCommandUtils.class),
        this.testFramework.getInstance(CustomChatCommandService.class));
  }

  @Test
  public final void handleMessageEvent_notExistingCustomCommandWithDefaultMessage_resultIsSendsDefaultNotCustomCommandMessage() {
    this.installModules();
    final String trigger = this.testFramework.arbitraryData().getString();
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final DeleteCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(), trigger)));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFramework.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(String.format(DeleteCommandChatCommand.DEFAULT_NOT_CUSTOM_COMMAND_MESSAGE_FORMAT, trigger))));
  }

  @Test
  public final void handleMessageEvent_notExistingCustomCommandWithCustomMessage_resultIsSendsCustomNotCustomCommandMessage() {
    this.installModules();
    final String customMessageFormat = this.testFramework.arbitraryData().getString() + "%s";
    final MapConfiguration configuration = this.testFramework.getInstance(MapConfiguration.class);
    configuration.set(DeleteCommandChatCommand.KEY_NOT_CUSTOM_COMMAND_MESSAGE_FORMAT, customMessageFormat);
    final String trigger = this.testFramework.arbitraryData().getString();
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final DeleteCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(), trigger)));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFramework.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(String.format(customMessageFormat, trigger))));
  }

  @Test
  public final void handleMessageEvent_existingCustomCommandWithDefaultMessage_resultIsDefaultMessage() {
    this.installModules();
    final String trigger = this.testFramework.arbitraryData().getString();
    final CustomChatCommandService service = this.testFramework.getInstance(CustomChatCommandService.class);
    service.createCustomChatCommand(ChatCommandTrigger.of(trigger),
        CommandBody.of(this.testFramework.arbitraryData().getString()),
        ChatCommandDescription.of(this.testFramework.arbitraryData().getString()), UserRole.VIEWER);
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final DeleteCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(), trigger)));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFramework.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(String.format(this.getMessageFormatDefault().value, trigger))));
  }

  private CommandMessageFormat getMessageFormatDefault() {
    return CommandMessageFormat.of(DeleteCommandChatCommand.DEFAULT_MESSAGE_FORMAT);
  }

  @Test
  public final void handleMessageEvent_existingCustomCommandWithCustomMessage_resultIsCustomMessage() {
    this.installModules();
    final String customMessageFormat = this.testFramework.arbitraryData().getString() + " %s";
    final MapConfiguration configuration = this.testFramework.getInstance(MapConfiguration.class);
    configuration.set(this.getMessageFormatKey(), customMessageFormat);
    final String trigger = this.testFramework.arbitraryData().getString();
    final CustomChatCommandService service = this.testFramework.getInstance(CustomChatCommandService.class);
    service.createCustomChatCommand(ChatCommandTrigger.of(trigger),
        CommandBody.of(this.testFramework.arbitraryData().getString()),
        ChatCommandDescription.of(this.testFramework.arbitraryData().getString()), UserRole.VIEWER);
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final DeleteCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(), trigger)));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFramework.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(String.format(customMessageFormat, trigger))));
  }

  private ConfigurationKey getMessageFormatKey() {
    return ConfigurationKey.of(KEY_MESSAGE_FORMAT, DeleteCommandChatCommand.class);
  }

}
