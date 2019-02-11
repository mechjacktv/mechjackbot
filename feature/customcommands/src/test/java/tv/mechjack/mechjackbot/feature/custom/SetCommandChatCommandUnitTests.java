package tv.mechjack.mechjackbot.feature.custom;

import static org.assertj.core.api.Assertions.assertThat;
import static tv.mechjack.mechjackbot.api.BaseChatCommand.KEY_DESCRIPTION;
import static tv.mechjack.mechjackbot.api.BaseChatCommand.KEY_TRIGGER;

import java.util.Optional;

import org.assertj.core.api.Condition;
import org.junit.Test;

import tv.mechjack.mechjackbot.api.BaseChatCommandContractTests;
import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandRegistry;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatCommandUtils;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.PicoCliUtils;
import tv.mechjack.mechjackbot.api.TestChatMessageEvent;
import tv.mechjack.mechjackbot.api.UserRole;
import tv.mechjack.mechjackbot.feature.custom.ProtoMessage.CustomCommand;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.configuration.ConfigurationKey;
import tv.mechjack.platform.configuration.MapConfiguration;
import tv.mechjack.platform.configuration.TestConfigurationModule;
import tv.mechjack.platform.keyvaluestore.TestKeyValueStoreModule;
import tv.mechjack.protobuf.TestProtobufModule;

public class SetCommandChatCommandUnitTests extends BaseChatCommandContractTests {

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

  @Test
  public final void handleMessageEvent_invalidCommandOption_resultIsSendsUsage() {
    this.installModules();
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s --bad-flag %s %s", subjectUnderTest.getTrigger(),
        this.testFramework.arbitraryData().getString(), this.testFramework.arbitraryData().getString())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFramework.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        commandUtils.createUsageMessage(subjectUnderTest, messageEvent)));
  }

  @Override
  protected SetCommandChatCommand givenASubjectToTest() {
    return new SetCommandChatCommand(this.testFramework.getInstance(CommandConfigurationBuilder.class),
        this.testFramework.getInstance(Configuration.class),
        this.testFramework.getInstance(CustomChatCommandService.class),
        this.testFramework.getInstance(PicoCliUtils.class));
  }

  @Test
  public final void handleMessageEvent_invalidUserRole_resultIsSendsUsage() {
    this.installModules();
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s --access-level %s %s", subjectUnderTest.getTrigger(),
        this.testFramework.arbitraryData().getString(), this.testFramework.arbitraryData().getString())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFramework.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        commandUtils.createUsageMessage(subjectUnderTest, messageEvent)));
  }

  @Test
  public final void handleMessageEvent_newCommandNoBodyDefaultNoBodyMessage_resultIsDefaultBodyRequiredMessage() {
    this.installModules();
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(),
        this.testFramework.arbitraryData().getString())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFramework.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(SetCommandChatCommand.DEFAULT_BODY_REQUIRED_MESSAGE_FORMAT)));
  }

  @Test
  public final void handleMessageEvent_newCommandNoBodyCustomNoBodyMessage_resultIsCustomBodyRequiredMessage() {
    this.installModules();
    final String messageFormat = this.testFramework.arbitraryData().getString() + "$(trigger): body required";
    final MapConfiguration configuration = this.testFramework.getInstance(MapConfiguration.class);
    configuration.set(SetCommandChatCommand.KEY_BODY_REQUIRED_MESSAGE_FORMAT, messageFormat);
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(),
        this.testFramework.arbitraryData().getString())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFramework.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(messageFormat)));
  }

  @Test
  public final void handleMessageEvent_creatingCustomCommandDefaultRole_resultIsNewCommandAddedToRegistry() {
    this.installModules();
    final ChatCommandTrigger customTrigger = ChatCommandTrigger.of(this.testFramework
        .arbitraryData().getString());
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s %s", subjectUnderTest.getTrigger(), customTrigger,
        this.testFramework.arbitraryData().getString())));

    subjectUnderTest.handleMessageEvent(messageEvent);

    final ChatCommandRegistry commandRegistry = this.testFramework.getInstance(ChatCommandRegistry.class);
    assertThat(commandRegistry.getCommand(customTrigger)).isNotNull();
  }

  @Test
  public final void handleMessageEvent_creatingCustomCommandDefaultRole_resultIsNewCommandAddedToDataStoreWithBody() {
    this.installModules();
    final ChatCommandTrigger customTrigger = ChatCommandTrigger.of(this.testFramework
        .arbitraryData().getString());
    final CommandBody customBody = CommandBody.of(this.testFramework.arbitraryData().getString());
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s %s", subjectUnderTest.getTrigger(), customTrigger,
        customBody)));

    subjectUnderTest.handleMessageEvent(messageEvent);

    final CustomCommandDataStore dataStore = this.testFramework.getInstance(CustomCommandDataStore.class);
    assertThat(dataStore.get(dataStore.createCustomCommandKey(customTrigger))).is(new Condition<>(
        command -> command.isPresent() && customBody.value.equals(command.get().getCommandBody()),
        "command was added with expected body"));
  }

  @Test
  public final void handleMessageEvent_creatingCustomCommandSpecifiedRole_resultIsNewCommandHasExpectUserRole() {
    this.installModules();
    final ChatCommandTrigger customTrigger = ChatCommandTrigger.of(this.testFramework
        .arbitraryData().getString());
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent
        .setChatMessage(ChatMessage.of(String.format("%s %s --access-level %s %s", subjectUnderTest.getTrigger(),
            customTrigger, UserRole.SUBSCRIBER, this.testFramework.arbitraryData().getString())));

    subjectUnderTest.handleMessageEvent(messageEvent);

    final CustomCommandDataStore dataStore = this.testFramework.getInstance(CustomCommandDataStore.class);
    final Optional<CustomCommand> customCommand = dataStore.get(dataStore.createCustomCommandKey(customTrigger));
    assertThat(customCommand).is(new Condition<>(
        command -> command.isPresent() && UserRole.SUBSCRIBER.toString().equals(command.get().getAccessLevel()),
        "command was added with expected user role"));
  }

  @Test
  public final void handleMessageEvent_creatingCustomCommandSpecifiedDescription_resultIsNewCommandHasExpectDescription() {
    this.installModules();
    final ChatCommandTrigger customTrigger = ChatCommandTrigger.of(this.testFramework
        .arbitraryData().getString());
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    final String description = this.testFramework.arbitraryData().getString();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s --description \"%s\" %s",
        subjectUnderTest.getTrigger(), customTrigger, description,
        this.testFramework.arbitraryData().getString())));

    subjectUnderTest.handleMessageEvent(messageEvent);

    final CustomCommandDataStore dataStore = this.testFramework.getInstance(CustomCommandDataStore.class);
    final Optional<CustomCommand> customCommand = dataStore.get(dataStore.createCustomCommandKey(customTrigger));
    assertThat(customCommand).is(new Condition<>(
        command -> command.isPresent() && description.equals(command.get().getDescription()),
        "command was added with expected description"));
  }

  @Test
  public final void handleMessageEvent_creatingCustomCommand_resultIsSendSuccessMessage() {
    this.installModules();
    final ChatCommandTrigger customTrigger = ChatCommandTrigger.of(this.testFramework
        .arbitraryData().getString());
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s %s", subjectUnderTest.getTrigger(), customTrigger,
        this.testFramework.arbitraryData().getString())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFramework.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(String.format(SetCommandChatCommand.DEFAULT_MESSAGE_FORMAT, customTrigger))));
  }

  @Test
  public final void handleMessageEvent_updatingCustomCommandBody_resultIsBodyUpdated() {
    this.installModules();
    final ChatCommandTrigger commandTrigger = ChatCommandTrigger.of(this.testFramework
        .arbitraryData().getString());
    final CustomChatCommandService customChatCommandService = this.testFramework
        .getInstance(CustomChatCommandService.class);
    customChatCommandService.createCustomChatCommand(commandTrigger,
        CommandBody.of(this.testFramework.arbitraryData().getString()),
        ChatCommandDescription.of(this.testFramework.arbitraryData().getString()), null);
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final CommandBody commandBody = CommandBody.of(this.testFramework.arbitraryData().getString());
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s %s", subjectUnderTest.getTrigger(),
        commandTrigger, commandBody)));

    subjectUnderTest.handleMessageEvent(messageEvent);

    final CustomCommandDataStore dataStore = this.testFramework.getInstance(CustomCommandDataStore.class);
    final Optional<CustomCommand> customCommand = dataStore.get(dataStore.createCustomCommandKey(commandTrigger));
    assertThat(customCommand).is(new Condition<>(
        command -> command.isPresent() && commandBody.value.equals(command.get().getCommandBody()),
        "command was updated with expected body"));
  }

  @Test
  public final void handleMessageEvent_updatingCustomCommandRole_resultIsRoleUpdated() {
    this.installModules();
    final ChatCommandTrigger commandTrigger = ChatCommandTrigger.of(this.testFramework
        .arbitraryData().getString());
    final CustomChatCommandService customChatCommandService = this.testFramework
        .getInstance(CustomChatCommandService.class);
    customChatCommandService.createCustomChatCommand(commandTrigger,
        CommandBody.of(this.testFramework.arbitraryData().getString()),
        ChatCommandDescription.of(this.testFramework.arbitraryData().getString()), null);
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s --access-level %s", subjectUnderTest.getTrigger(),
        commandTrigger, UserRole.SUBSCRIBER)));

    subjectUnderTest.handleMessageEvent(messageEvent);

    final CustomCommandDataStore dataStore = this.testFramework.getInstance(CustomCommandDataStore.class);
    final Optional<CustomCommand> customCommand = dataStore.get(dataStore.createCustomCommandKey(commandTrigger));
    assertThat(customCommand).is(new Condition<>(
        command -> command.isPresent() && UserRole.SUBSCRIBER.toString().equals(command.get().getAccessLevel()),
        "command was updated with expected user role"));
  }

  @Test
  public final void handleMessageEvent_updatingCustomCommandDescription_resultIsDescriptionUpdated() {
    this.installModules();
    final ChatCommandTrigger commandTrigger = ChatCommandTrigger.of(this.testFramework
        .arbitraryData().getString());
    final CustomChatCommandService customChatCommandService = this.testFramework
        .getInstance(CustomChatCommandService.class);
    customChatCommandService.createCustomChatCommand(commandTrigger,
        CommandBody.of(this.testFramework.arbitraryData().getString()),
        ChatCommandDescription.of(this.testFramework.arbitraryData().getString()), null);
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final String newDescription = this.testFramework.arbitraryData().getString();
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s --description %s", subjectUnderTest.getTrigger(),
        commandTrigger, newDescription)));

    subjectUnderTest.handleMessageEvent(messageEvent);

    final CustomCommandDataStore dataStore = this.testFramework.getInstance(CustomCommandDataStore.class);
    final Optional<CustomCommand> customCommand = dataStore.get(dataStore.createCustomCommandKey(commandTrigger));
    assertThat(customCommand).is(new Condition<>(
        command -> command.isPresent() && newDescription.equals(command.get().getDescription()),
        "command was updated with expected description"));
  }

  @Test
  public final void handleMessageEvent_updatingCustomCommand_resultIsSendSuccessMessage() {
    this.installModules();
    final ChatCommandTrigger commandTrigger = ChatCommandTrigger.of(this.testFramework
        .arbitraryData().getString());
    final CustomChatCommandService customChatCommandService = this.testFramework
        .getInstance(CustomChatCommandService.class);
    customChatCommandService.createCustomChatCommand(commandTrigger,
        CommandBody.of(this.testFramework.arbitraryData().getString()),
        ChatCommandDescription.of(this.testFramework.arbitraryData().getString()), null);
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent
        .setChatMessage(ChatMessage.of(String.format("%s %s --access-level %s %s", subjectUnderTest.getTrigger(),
            commandTrigger, UserRole.SUBSCRIBER, this.testFramework.arbitraryData().getString())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFramework.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(String.format(SetCommandChatCommand.DEFAULT_MESSAGE_FORMAT, commandTrigger))));
  }

  @Test
  public final void handleMessageEvent_updatingCustomCommandNoUpdates_resultIsSendSuccessMessage() {
    this.installModules();
    final ChatCommandTrigger commandTrigger = ChatCommandTrigger.of(this.testFramework
        .arbitraryData().getString());
    final CustomChatCommandService customChatCommandService = this.testFramework
        .getInstance(CustomChatCommandService.class);
    customChatCommandService.createCustomChatCommand(commandTrigger,
        CommandBody.of(this.testFramework.arbitraryData().getString()),
        ChatCommandDescription.of(this.testFramework.arbitraryData().getString()), null);
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    final SetCommandChatCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(), commandTrigger)));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final ChatMessage result = messageEvent.getResponseChatMessage();

    final ChatCommandUtils commandUtils = this.testFramework.getInstance(ChatCommandUtils.class);
    assertThat(result).isEqualTo(commandUtils.replaceChatMessageVariables(subjectUnderTest, messageEvent,
        ChatMessage.of(String.format(SetCommandChatCommand.DEFAULT_MESSAGE_FORMAT, commandTrigger))));
  }

}
