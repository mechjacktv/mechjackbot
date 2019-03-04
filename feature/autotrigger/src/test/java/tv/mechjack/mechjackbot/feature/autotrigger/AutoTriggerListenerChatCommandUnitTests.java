package tv.mechjack.mechjackbot.feature.autotrigger;

import static org.assertj.core.api.Assertions.assertThat;
import static tv.mechjack.mechjackbot.api.BaseChatCommand.KEY_DESCRIPTION;
import static tv.mechjack.mechjackbot.api.BaseChatCommand.KEY_TRIGGER;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import tv.mechjack.mechjackbot.api.BaseChatCommandContractTests;
import tv.mechjack.mechjackbot.api.ChatCommand;
import tv.mechjack.mechjackbot.api.ChatCommandDescription;
import tv.mechjack.mechjackbot.api.ChatCommandRegistry;
import tv.mechjack.mechjackbot.api.ChatCommandTrigger;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.ChatUser;
import tv.mechjack.mechjackbot.api.CommandConfigurationBuilder;
import tv.mechjack.mechjackbot.api.TestChatCommand;
import tv.mechjack.mechjackbot.api.TestChatMessageEvent;
import tv.mechjack.platform.configuration.ConfigurationKey;
import tv.mechjack.platform.keyvaluestore.TestKeyValueStoreModule;
import tv.mechjack.protobuf.TestProtobufModule;
import tv.mechjack.twitchclient.TwitchLogin;

public class AutoTriggerListenerChatCommandUnitTests extends
    BaseChatCommandContractTests {

  @Override
  protected void registerModules() {
    super.registerModules();
    this.testFramework.registerModule(new TestAutoTriggerFeatureModule());
    this.testFramework.registerModule(new TestKeyValueStoreModule());
    this.testFramework.registerModule(new TestProtobufModule());
  }

  @Override
  protected ChatCommandDescription getDescriptionDefault() {
    return ChatCommandDescription.of(
        AutoTriggerListenerChatCommand.DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(
        KEY_DESCRIPTION, AutoTriggerListenerChatCommand.class);
  }

  @Override
  protected ChatCommandTrigger getTriggerDefault() {
    return ChatCommandTrigger.of(UUID.randomUUID().toString());
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(
        KEY_TRIGGER, AutoTriggerListenerChatCommand.class);
  }

  @Override
  protected AutoTriggerListenerChatCommand givenASubjectToTest() {
    return new AutoTriggerListenerChatCommand(this.testFramework.getInstance(
        CommandConfigurationBuilder.class),
        this.testFramework.getInstance(AutoTriggerService.class));
  }

  @Test
  public final void handleMessageEvent_requiredTimeNotMet_resultIsNoResponse() {
    this.registerModules();
    final TestChatMessageEvent messageEvent = this.givenATestChatMessageEvent();
    final ChatCommand command = this.givenAChatCommand();
    this.createAutoTriggerList(5, 0, 0, command);
    final AutoTriggerListenerChatCommand subjectUnderTest = this
        .givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);

    assertThat(messageEvent.getResponseChatMessage()).isNull();
  }

  @Test
  public final void handleMessageEvent_requiredTimeMet_resultIsResponseOnTriggeringMessage() {
    this.registerModules();
    final TestChatMessageEvent messageEvent = this.givenATestChatMessageEvent();
    final ChatCommand command = this.givenAChatCommand();
    this.createAutoTriggerList(5, 0, 0, command);
    final AutoTriggerListenerChatCommand subjectUnderTest = this
        .givenASubjectToTest();

    this.testFramework.testClock().updateTime(6, TimeUnit.MINUTES);
    subjectUnderTest.handleMessageEvent(messageEvent);

    assertThat(messageEvent.getResponseChatMessage()).isNotNull();
  }

  @Test
  public final void handleMessageEvent_requiredMessagesNotMet_resultIsNoResponse() {
    this.registerModules();
    final TestChatMessageEvent messageEvent = this.givenATestChatMessageEvent();
    final ChatCommand command = this.givenAChatCommand();
    this.createAutoTriggerList(0, 2, 0, command);
    final AutoTriggerListenerChatCommand subjectUnderTest = this
        .givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);

    assertThat(messageEvent.getResponseChatMessage()).isNull();
  }

  @Test
  public final void handleMessageEvent_requiredMessagesMet_resultIsResponseOnTriggeringMessage() {
    this.registerModules();
    final TestChatMessageEvent messageEvent1 = this.givenATestChatMessageEvent();
    final TestChatMessageEvent messageEvent2 = this.givenATestChatMessageEvent();
    final ChatCommand command = this.givenAChatCommand();
    this.createAutoTriggerList(0, 2, 0, command);
    final AutoTriggerListenerChatCommand subjectUnderTest = this
        .givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent1);
    subjectUnderTest.handleMessageEvent(messageEvent2);

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(messageEvent1.getResponseChatMessage()).isNull();
    softly.assertThat(messageEvent2.getResponseChatMessage()).isNotNull();
    softly.assertAll();
  }

  @Test
  public final void handleMessageEvent_requiredChattersNotMet_resultIsNoResponse() {
    this.registerModules();
    final TestChatMessageEvent messageEvent = this.givenATestChatMessageEvent();
    final ChatCommand command = this.givenAChatCommand();
    this.createAutoTriggerList(0, 0, 2, command);
    final AutoTriggerListenerChatCommand subjectUnderTest = this
        .givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);

    assertThat(messageEvent.getResponseChatMessage()).isNull();
  }

  @Test
  public final void handleMessageEvent_requiredChattersMet_resultIsResponseOnTriggeringMessage() {
    this.registerModules();
    final TestChatMessageEvent messageEvent1 = this.givenATestChatMessageEvent();
    final TestChatMessageEvent messageEvent2 = this.givenATestChatMessageEvent();
    final ChatCommand command = this.givenAChatCommand();
    this.createAutoTriggerList(0, 0, 2, command);
    final AutoTriggerListenerChatCommand subjectUnderTest = this
        .givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent1);
    subjectUnderTest.handleMessageEvent(messageEvent2);

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(messageEvent1.getResponseChatMessage()).isNull();
    softly.assertThat(messageEvent2.getResponseChatMessage()).isNotNull();
    softly.assertAll();
  }

  private ChatCommand givenAChatCommand() {
    final TestChatCommand command = this.testFramework
        .getInstance(TestChatCommand.class);
    this.testFramework.getInstance(ChatCommandRegistry.class)
        .addCommand(command);

    command.setMessageEventHandler(
        chatMessageEvent -> chatMessageEvent.sendResponse(
            ChatMessage.of(this.testFramework.arbitraryData().getString())));
    return command;
  }

  private TestChatMessageEvent givenATestChatMessageEvent() {
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(
        TestChatMessageEvent.class);
    final TwitchLogin twitchLogin = TwitchLogin.of(this.testFramework.arbitraryData().getString());
    final ChatUser chatUser = this.testFramework.fakeFactory()
        .builder(ChatUser.class)
        .forMethod("getTwitchLogin")
        .setHandler(invocationContext -> twitchLogin)
        .build();

    messageEvent.setChatUser(chatUser);
    return messageEvent;
  }

  private void createAutoTriggerList(final int timeRequired,
      final int messageCount, final int chatterCount,
      final ChatCommand... commands) {
    final AutoTriggerService autoTriggerService = this.testFramework
        .getInstance(AutoTriggerService.class);
    final List<ChatCommandTrigger> triggers = new ArrayList<>();

    for (final ChatCommand command : commands) {
      triggers.add(command.getTrigger());
    }
    autoTriggerService.createAutoTriggerList(
        ListName.of(this.testFramework.arbitraryData().getString()),
        TimeRequired.of(timeRequired), MessageCount.of(messageCount),
        ChatterCount.of(chatterCount), null, triggers);
  }

}
