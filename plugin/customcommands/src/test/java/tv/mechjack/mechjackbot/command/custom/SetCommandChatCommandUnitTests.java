package tv.mechjack.mechjackbot.command.custom;

import static org.assertj.core.api.Assertions.assertThat;
import static tv.mechjack.mechjackbot.command.BaseChatCommand.KEY_DESCRIPTION;
import static tv.mechjack.mechjackbot.command.BaseChatCommand.KEY_TRIGGER;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.Test;

import tv.mechjack.configuration.Configuration;
import tv.mechjack.configuration.ConfigurationKey;
import tv.mechjack.configuration.MapConfiguration;
import tv.mechjack.configuration.TestConfigurationModule;
import tv.mechjack.keyvaluestore.TestKeyValueStoreModule;
import tv.mechjack.mechjackbot.ChatCommandDescription;
import tv.mechjack.mechjackbot.ChatCommandRegistry;
import tv.mechjack.mechjackbot.ChatCommandTrigger;
import tv.mechjack.mechjackbot.ChatCommandUtils;
import tv.mechjack.mechjackbot.ChatMessage;
import tv.mechjack.mechjackbot.TestChatMessageEvent;
import tv.mechjack.mechjackbot.UserRole;
import tv.mechjack.mechjackbot.command.BaseChatCommandContractTests;
import tv.mechjack.mechjackbot.command.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.command.custom.ProtoMessage.CustomCommand;

public class SetCommandChatCommandUnitTests extends BaseChatCommandContractTests {

  @Override
  protected void installModules() {
    super.installModules();
    this.testFrameworkRule.installModule(new TestConfigurationModule());
    this.testFrameworkRule.installModule(new TestCustomCommandModule());
    this.testFrameworkRule.installModule(new TestKeyValueStoreModule());
  }

  @Override
  protected ChatCommandDescription getDescriptionDefault() {
    return ChatCommandDescription.of(SetCommandChatCommand.DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(KEY_DESCRIPTION, SetCommandChatCommand.class);
  }

  @Override
  protected ChatCommandTrigger getTriggerDefault() {
    return ChatCommandTrigger.of(SetCommandChatCommand.DEFAULT_TRIGGER);
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
        ChatMessage.of(
            String.format(SetCommandChatCommand.DEFAULT_BODY_REQUIRED_MESSAGE_FORMAT, subjectUnderTest.getTrigger()))));
  }

  @Test
  public final void handleMessageEvent_newCommandNoBodyCustomNoBodyMessage_resultIsCustomBodyRequiredMessage() {
    this.installModules();
    final String messageFormat = this.testFrameworkRule.getArbitraryString() + "$(trigger): body required";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(SetCommandChatCommand.KEY_BODY_REQUIRED_MESSAGE_FORMAT, messageFormat);
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
        ChatMessage.of(String.format(SetCommandChatCommand.DEFAULT_MESSAGE_FORMAT, customTrigger))));
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
        ChatMessage.of(String.format(SetCommandChatCommand.DEFAULT_MESSAGE_FORMAT, commandTrigger))));
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
        ChatMessage.of(String.format(SetCommandChatCommand.DEFAULT_MESSAGE_FORMAT, commandTrigger))));
  }

}
