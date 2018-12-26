package com.mechjacktv.mechjackbot.command.custom;

import static com.mechjacktv.mechjackbot.command.BaseChatCommand.KEY_DESCRIPTION;
import static com.mechjacktv.mechjackbot.command.BaseChatCommand.KEY_TRIGGER;
import static com.mechjacktv.mechjackbot.command.custom.SetCommandChatCommand.DEFAULT_BODY_REQUIRED_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.command.custom.SetCommandChatCommand.DEFAULT_DESCRIPTION;
import static com.mechjacktv.mechjackbot.command.custom.SetCommandChatCommand.DEFAULT_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.command.custom.SetCommandChatCommand.DEFAULT_TRIGGER;
import static com.mechjacktv.mechjackbot.command.custom.SetCommandChatCommand.KEY_BODY_REQUIRED_MESSAGE_FORMAT;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.assertj.core.api.Condition;
import org.junit.Test;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.configuration.ConfigurationTestModule;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.keyvaluestore.KeyValueStoreTestModule;
import com.mechjacktv.mechjackbot.ChatCommandDescription;
import com.mechjacktv.mechjackbot.ChatCommandRegistry;
import com.mechjacktv.mechjackbot.ChatCommandTrigger;
import com.mechjacktv.mechjackbot.ChatCommandUtils;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.mechjackbot.TestChatMessageEvent;
import com.mechjacktv.mechjackbot.UserRole;
import com.mechjacktv.mechjackbot.command.BaseChatCommandContractTests;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.proto.mechjackbot.command.custom.CustomComandDataStoreMessage.CustomCommand;

public class SetCommandChatCommandUnitTests extends BaseChatCommandContractTests {

  @Override
  protected void installModules() {
    super.installModules();
    this.testFrameworkRule.installModule(new ConfigurationTestModule());
    this.testFrameworkRule.installModule(new CustomCommandTestModule());
    this.testFrameworkRule.installModule(new KeyValueStoreTestModule());
  }

  @Override
  protected ChatCommandDescription getDescriptionDefault() {
    return ChatCommandDescription.of(DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(KEY_DESCRIPTION, SetCommandChatCommand.class);
  }

  @Override
  protected ChatCommandTrigger getTriggerDefault() {
    return ChatCommandTrigger.of(DEFAULT_TRIGGER);
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, SetCommandChatCommand.class);
  }

  @Override
  protected SetCommandChatCommand givenASubjectToTest() {
    return new SetCommandChatCommand(this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class),
        this.testFrameworkRule.getInstance(Configuration.class),
        this.testFrameworkRule.getInstance(CustomChatCommandService.class));
  }

  @Test
  public final void handleMessageEvent_invalidCommandOption_resultIsSendsUsage() {
    this.installModules();
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s --bad-flag %s %s", subjectUnderTest.getTrigger(),
        this.testFrameworkRule.getArbitraryString(), this.testFrameworkRule.getArbitraryString())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFrameworkRule.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        commandUtils.createUsageMessage(subjectUnderTest, messageEvent)));
  }

  @Test
  public final void handleMessageEvent_invalidUserRole_resultIsSendsUsage() {
    this.installModules();
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s --user-role %s %s", subjectUnderTest.getTrigger(),
        this.testFrameworkRule.getArbitraryString(), this.testFrameworkRule.getArbitraryString())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFrameworkRule.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        commandUtils.createUsageMessage(subjectUnderTest, messageEvent)));
  }

  @Test
  public final void handleMessageEvent_newCommandNoBodyDefaultNoBodyMessage_resultIsDefaultBodyRequiredMessage() {
    this.installModules();
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(),
        this.testFrameworkRule.getArbitraryString())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFrameworkRule.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(String.format(DEFAULT_BODY_REQUIRED_MESSAGE_FORMAT, subjectUnderTest.getTrigger()))));
  }

  @Test
  public final void handleMessageEvent_newCommandNoBodyCustomNoBodyMessage_resultIsCustomBodyRequiredMessage() {
    this.installModules();
    final String messageFormat = this.testFrameworkRule.getArbitraryString() + "$(trigger): body required";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(KEY_BODY_REQUIRED_MESSAGE_FORMAT, messageFormat);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(),
        this.testFrameworkRule.getArbitraryString())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFrameworkRule.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(messageFormat)));
  }

  @Test
  public final void handleMessageEvent_creatingCustomCommandDefaultRole_resultIsNewCommandAddedToRegistry() {
    this.installModules();
    final ChatCommandTrigger customTrigger = ChatCommandTrigger.of(this.testFrameworkRule.getArbitraryString());
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s %s", subjectUnderTest.getTrigger(), customTrigger,
        this.testFrameworkRule.getArbitraryString())));

    subjectUnderTest.handleMessageEvent(messageEvent);

    final ChatCommandRegistry commandRegistry = this.testFrameworkRule.getInstance(ChatCommandRegistry.class);
    assertThat(commandRegistry.getCommand(customTrigger)).isNotNull();
  }

  @Test
  public final void handleMessageEvent_creatingCustomCommandDefaultRole_resultIsNewCommandAddedToDataStoreWithBody() {
    this.installModules();
    final ChatCommandTrigger customTrigger = ChatCommandTrigger.of(this.testFrameworkRule.getArbitraryString());
    final CommandBody customBody = CommandBody.of(this.testFrameworkRule.getArbitraryString());
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s %s", subjectUnderTest.getTrigger(), customTrigger,
        customBody)));

    subjectUnderTest.handleMessageEvent(messageEvent);

    final CustomCommandDataStore dataStore = this.testFrameworkRule.getInstance(CustomCommandDataStore.class);
    assertThat(dataStore.get(dataStore.createCustomCommandKey(customTrigger))).is(new Condition<>(
        command -> command.isPresent() && customBody.value.equals(command.get().getCommandBody()),
        "command was added with expected body"));
  }

  @Test
  public final void handleMessageEvent_creatingCustomCommandSpecifiedRole_resultIsNewCommandHasExpectUserRole() {
    this.installModules();
    final ChatCommandTrigger customTrigger = ChatCommandTrigger.of(this.testFrameworkRule.getArbitraryString());
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s --user-role %s %s", subjectUnderTest.getTrigger(),
        customTrigger, UserRole.SUBSCRIBER, this.testFrameworkRule.getArbitraryString())));

    subjectUnderTest.handleMessageEvent(messageEvent);

    final CustomCommandDataStore dataStore = this.testFrameworkRule.getInstance(CustomCommandDataStore.class);
    final Optional<CustomCommand> customCommand = dataStore.get(dataStore.createCustomCommandKey(customTrigger));
    assertThat(customCommand).is(new Condition<>(
        command -> command.isPresent() && UserRole.SUBSCRIBER.toString().equals(command.get().getAccessLevel()),
        "command was added with expected user role"));
  }

  @Test
  public final void handleMessageEvent_creatingCustomCommand_resultIsSendSuccessMessage() {
    this.installModules();
    final ChatCommandTrigger customTrigger = ChatCommandTrigger.of(this.testFrameworkRule.getArbitraryString());
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s %s", subjectUnderTest.getTrigger(), customTrigger,
        this.testFrameworkRule.getArbitraryString())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFrameworkRule.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(String.format(DEFAULT_MESSAGE_FORMAT, customTrigger))));
  }

  @Test
  public final void handleMessageEvent_updatingCustomCommandBody_resultIsBodyUpdated() {
    this.installModules();
    final ChatCommandTrigger commandTrigger = ChatCommandTrigger.of(this.testFrameworkRule.getArbitraryString());
    final CustomChatCommandService customChatCommandService = this.testFrameworkRule
        .getInstance(CustomChatCommandService.class);
    customChatCommandService.createCustomChatCommand(commandTrigger,
        CommandBody.of(this.testFrameworkRule.getArbitraryString()), null);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final CommandBody commandBody = CommandBody.of(this.testFrameworkRule.getArbitraryString());
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s %s", subjectUnderTest.getTrigger(),
        commandTrigger, commandBody)));

    subjectUnderTest.handleMessageEvent(messageEvent);

    final CustomCommandDataStore dataStore = this.testFrameworkRule.getInstance(CustomCommandDataStore.class);
    final Optional<CustomCommand> customCommand = dataStore.get(dataStore.createCustomCommandKey(commandTrigger));
    assertThat(customCommand).is(new Condition<>(
        command -> command.isPresent() && commandBody.value.equals(command.get().getCommandBody()),
        "command was updated with expected body"));
  }

  @Test
  public final void handleMessageEvent_updatingCustomCommandRole_resultIsRoleUpdated() {
    this.installModules();
    final ChatCommandTrigger commandTrigger = ChatCommandTrigger.of(this.testFrameworkRule.getArbitraryString());
    final CustomChatCommandService customChatCommandService = this.testFrameworkRule
        .getInstance(CustomChatCommandService.class);
    customChatCommandService.createCustomChatCommand(commandTrigger,
        CommandBody.of(this.testFrameworkRule.getArbitraryString()), null);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s --user-role %s", subjectUnderTest.getTrigger(),
        commandTrigger, UserRole.SUBSCRIBER)));

    subjectUnderTest.handleMessageEvent(messageEvent);

    final CustomCommandDataStore dataStore = this.testFrameworkRule.getInstance(CustomCommandDataStore.class);
    final Optional<CustomCommand> customCommand = dataStore.get(dataStore.createCustomCommandKey(commandTrigger));
    assertThat(customCommand).is(new Condition<>(
        command -> command.isPresent() && UserRole.SUBSCRIBER.toString().equals(command.get().getAccessLevel()),
        "command was updated with expected user role"));
  }

  @Test
  public final void handleMessageEvent_updatingCustomCommand_resultIsSendSuccessMessage() {
    this.installModules();
    final ChatCommandTrigger commandTrigger = ChatCommandTrigger.of(this.testFrameworkRule.getArbitraryString());
    final CustomChatCommandService customChatCommandService = this.testFrameworkRule
        .getInstance(CustomChatCommandService.class);
    customChatCommandService.createCustomChatCommand(commandTrigger,
        CommandBody.of(this.testFrameworkRule.getArbitraryString()), null);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s --user-role %s %s", subjectUnderTest.getTrigger(),
        commandTrigger, UserRole.SUBSCRIBER, this.testFrameworkRule.getArbitraryString())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFrameworkRule.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(String.format(DEFAULT_MESSAGE_FORMAT, commandTrigger))));
  }

  @Test
  public final void handleMessageEvent_updatingCustomCommandNoUpdates_resultIsSendSuccessMessage() {
    this.installModules();
    final ChatCommandTrigger commandTrigger = ChatCommandTrigger.of(this.testFrameworkRule.getArbitraryString());
    final CustomChatCommandService customChatCommandService = this.testFrameworkRule
        .getInstance(CustomChatCommandService.class);
    customChatCommandService.createCustomChatCommand(commandTrigger,
        CommandBody.of(this.testFrameworkRule.getArbitraryString()), null);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(), commandTrigger)));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFrameworkRule.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(String.format(DEFAULT_MESSAGE_FORMAT, commandTrigger))));
  }

}
