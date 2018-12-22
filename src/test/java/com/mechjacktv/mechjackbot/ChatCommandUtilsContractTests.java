package com.mechjacktv.mechjackbot;

import java.util.concurrent.TimeUnit;

import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.configuration.ConfigurationTestModule;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.mechjackbot.chatbot.ChatBotTestModule;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandTestModule;
import com.mechjacktv.testframework.TestFrameworkRule;
import com.mechjacktv.util.UtilTestModule;

import org.junit.Rule;
import org.junit.Test;

import static com.mechjacktv.mechjackbot.ChatCommandUtils.DEFAULT_COMMAND_COOL_DOWN;
import static com.mechjacktv.mechjackbot.ChatCommandUtils.DEFAULT_USAGE_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.ChatCommandUtils.DEFAULT_USER_COOL_DOWN;
import static com.mechjacktv.mechjackbot.ChatCommandUtils.KEY_COMMAND_COOL_DOWN;
import static com.mechjacktv.mechjackbot.ChatCommandUtils.KEY_USAGE_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.ChatCommandUtils.KEY_USER_COOL_DOWN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;

public abstract class ChatCommandUtilsContractTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  protected void installModules() {
    this.testFrameworkRule.installModule(new ChatBotTestModule());
    this.testFrameworkRule.installModule(new CommandTestModule());
    this.testFrameworkRule.installModule(new ConfigurationTestModule());
    this.testFrameworkRule.installModule(new UtilTestModule());
  }

  protected abstract ChatCommandUtils givenASubjectToTest();

  protected Integer getCommandCoolDownDefault() {
    return Integer.parseInt(DEFAULT_COMMAND_COOL_DOWN);
  }

  protected ConfigurationKey getCommandCoolDownKey() {
    return ConfigurationKey.of(KEY_COMMAND_COOL_DOWN);
  }

  protected Integer getUserCoolDownDefault() {
    return Integer.parseInt(DEFAULT_USER_COOL_DOWN);
  }

  protected ConfigurationKey getUserCoolDownKey() {
    return ConfigurationKey.of(KEY_USER_COOL_DOWN);
  }

  protected String getUsageMessageFormatDefault() {
    return DEFAULT_USAGE_MESSAGE_FORMAT;
  }

  protected ConfigurationKey getUsageMessageFormatKey() {
    return ConfigurationKey.of(KEY_USAGE_MESSAGE_FORMAT);
  }

  @Test
  public final void hasAccessLevel_nullCommand_throwsNullPointerException() {
    this.installModules();
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.hasAccessLevel(null, chatMessageEvent));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatCommand");
  }

  @Test
  public final void hasAccessLevel_nullMessageEvent_throwsNullPointerException() {
    this.installModules();
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.hasAccessLevel(chatCommand, null));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatMessageEvent");
  }

  @Test
  public final void hasAccessLevel_commandHasNoRestrictions_resultIsTrue() {
    this.installModules();
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.hasAccessLevel(chatCommand, chatMessageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void hasAccessLevel_messageEventIsFromChannelOwner_resultIsTrue() {
    this.installModules();
    final ChatBotConfiguration chatBotConfiguration = this.testFrameworkRule.getInstance(ChatBotConfiguration.class);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final TestChatUser chatUser = (TestChatUser) chatMessageEvent.getChatUser();
    chatUser.setTwitchLogin(chatBotConfiguration.getTwitchLogin());
    chatUser.setAccessLevelCheck(accessLevel -> true);
    final ChatCommand chatCommand = new RequiresAccessLevelSubscriberChatCommand(
        this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class));
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.hasAccessLevel(chatCommand, chatMessageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void hasAccessLevel_messageEventIsFromUserWithRequiredRole_resultIsTrue() {
    this.installModules();
    final ChatBotConfiguration chatBotConfiguration = this.testFrameworkRule.getInstance(ChatBotConfiguration.class);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final TestChatUser chatUser = (TestChatUser) chatMessageEvent.getChatUser();
    // chatUser is SUBSCRIBER
    chatUser.setAccessLevelCheck(accessLevel -> UserRole.SUBSCRIBER.value() <= accessLevel.value());
    final ChatCommand chatCommand = new RequiresAccessLevelSubscriberChatCommand(
        this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class));
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.hasAccessLevel(chatCommand, chatMessageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void hasAccessLevel_messageEventIsFromUserWithoutRequiredRole_resultIsFalse() {
    this.installModules();
    final ChatBotConfiguration chatBotConfiguration = this.testFrameworkRule.getInstance(ChatBotConfiguration.class);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final TestChatUser chatUser = (TestChatUser) chatMessageEvent.getChatUser();
    // chatUser is FOLLOWER
    chatUser.setAccessLevelCheck(accessLevel -> UserRole.FOLLOWER.value() <= accessLevel.value());
    final ChatCommand chatCommand = new RequiresAccessLevelSubscriberChatCommand(
        this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class));
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.hasAccessLevel(chatCommand, chatMessageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isCooledDown_nullCommand_throwsNullPointerException() {
    this.installModules();
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.isCooledDown(null, chatMessageEvent));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatCommand");
  }

  @Test
  public final void isCooledDown_nullMessageEvent_throwsNullPointerException() {
    this.installModules();
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.isCooledDown(chatCommand, null));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatMessageEvent");
  }

  @Test
  public final void isCooledDown_commandAndUserFirstCalled_resultIsTrue() {
    this.installModules();
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isCooledDown(chatCommand, chatMessageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isCooledDown_commandIsCooling_resultIsFalse() {
    this.installModules();
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for chatCommand
    subjectUnderTest.isCooledDown(chatCommand, this.testFrameworkRule.getInstance(ChatMessageEvent.class));
    // advance time to the chatCommand cool down duration
    this.testFrameworkRule.currentTimeDelta(this.getCommandCoolDownDefault(), TimeUnit.SECONDS);

    final boolean result = subjectUnderTest.isCooledDown(chatCommand, chatMessageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isCooledDown_commandIsCooled_resultIsTrue() {
    this.installModules();
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for chatCommand
    subjectUnderTest.isCooledDown(chatCommand, this.testFrameworkRule.getInstance(ChatMessageEvent.class));
    // advance time to one millisecond longer than the chatCommand cool down
    // duration
    this.testFrameworkRule.currentTimeDelta(this.getCommandCoolDownDefault(), TimeUnit.SECONDS, 1);

    final boolean result = subjectUnderTest.isCooledDown(chatCommand, chatMessageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isCooledDown_userIsCooling_resultIsFalse() {
    this.installModules();
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for user
    subjectUnderTest.isCooledDown(this.testFrameworkRule.getInstance(TestChatCommand.class), chatMessageEvent);
    // advance time to the user cool down duration
    this.testFrameworkRule.currentTimeDelta(this.getUserCoolDownDefault(), TimeUnit.SECONDS);

    final boolean result = subjectUnderTest.isCooledDown(chatCommand, chatMessageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isCooledDown_userIsCooled_resultIsTrue() {
    this.installModules();
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for user
    subjectUnderTest.isCooledDown(this.testFrameworkRule.getInstance(TestChatCommand.class), chatMessageEvent);
    // advance time to one millisecond longer than the user cool down duration
    this.testFrameworkRule.currentTimeDelta(this.getUserCoolDownDefault(), TimeUnit.SECONDS, 1);

    final boolean result = subjectUnderTest.isCooledDown(chatCommand, chatMessageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isCooledDown_commandAndUserAreCooling_resultIsFalse() {
    this.installModules();
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for chatCommand and user
    subjectUnderTest.isCooledDown(chatCommand, chatMessageEvent);
    // advance time to the shorter cool down duration
    this.testFrameworkRule.currentTimeDelta(Math.min(this.getCommandCoolDownDefault(),
        this.getUserCoolDownDefault()), TimeUnit.SECONDS);

    final boolean result = subjectUnderTest.isCooledDown(chatCommand, chatMessageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isCooledDown_commandAndUserAreCooled_resultIsTrue() {
    this.installModules();
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for chatCommand and user
    subjectUnderTest.isCooledDown(chatCommand, chatMessageEvent);
    // advance time to one millisecond longer than the longer cool down duration
    this.testFrameworkRule.currentTimeDelta(Math.max(this.getCommandCoolDownDefault(),
        this.getUserCoolDownDefault()), TimeUnit.SECONDS, 1);

    final boolean result = subjectUnderTest.isCooledDown(chatCommand, chatMessageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isCooledDown_noCoolDownCommandBothWouldBeCooling_resultIsTrue() {
    this.installModules();
    final ChatCommand chatCommand = new NoCoolDownChatCommand(
        this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class));
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for chatCommand and user
    subjectUnderTest.isCooledDown(chatCommand, chatMessageEvent);
    // advance time to the shorter cool down duration
    this.testFrameworkRule.currentTimeDelta(Math.min(this.getCommandCoolDownDefault(),
        this.getUserCoolDownDefault()), TimeUnit.SECONDS);

    final boolean result = subjectUnderTest.isCooledDown(chatCommand, chatMessageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isCooledDown_privilegedUserBothWouldBeCooling_resultIsTrue() {
    this.installModules();
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final TestChatUser chatUser = (TestChatUser) chatMessageEvent.getChatUser();
    // chatUser is MODERATOR
    chatUser.setAccessLevelCheck(accessLevel -> UserRole.MODERATOR.value() >= accessLevel.value());
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for chatCommand and user
    subjectUnderTest.isCooledDown(chatCommand, chatMessageEvent);
    // advance time to the shorter cool down duration
    this.testFrameworkRule.currentTimeDelta(Math.min(this.getCommandCoolDownDefault(),
        this.getUserCoolDownDefault()), TimeUnit.SECONDS);

    final boolean result = subjectUnderTest.isCooledDown(chatCommand, chatMessageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isCooledDown_customCommandCoolDownCommandIsCooling_resultIsFalse() {
    this.installModules();
    final int customCommandCoolDown = this.getCommandCoolDownDefault() + 1;
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getCommandCoolDownKey(), Integer.toString(customCommandCoolDown));
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for chatCommand
    subjectUnderTest.isCooledDown(chatCommand, this.testFrameworkRule.getInstance(ChatMessageEvent.class));
    // advance time to the custom chatCommand cool down duration
    this.testFrameworkRule.currentTimeDelta(customCommandCoolDown, TimeUnit.SECONDS);

    final boolean result = subjectUnderTest.isCooledDown(chatCommand, chatMessageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isCooledDown_customCommandCoolDownCommandIsCooled_resultIsTrue() {
    this.installModules();
    final int customCommandCoolDown = this.getCommandCoolDownDefault() + 1;
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getCommandCoolDownKey(), Integer.toString(customCommandCoolDown));
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for chatCommand
    subjectUnderTest.isCooledDown(chatCommand, this.testFrameworkRule.getInstance(ChatMessageEvent.class));
    // advance time to one millisecond past custom chatCommand cool down duration
    this.testFrameworkRule.currentTimeDelta(customCommandCoolDown, TimeUnit.SECONDS, 1);

    final boolean result = subjectUnderTest.isCooledDown(chatCommand, chatMessageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isCooledDown_customUserCoolDownUserIsCooling_resultIsFalse() {
    this.installModules();
    final int customUserCoolDown = this.getUserCoolDownDefault() + 1;
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getUserCoolDownKey(), Integer.toString(customUserCoolDown));
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for user
    subjectUnderTest.isCooledDown(this.testFrameworkRule.getInstance(TestChatCommand.class), chatMessageEvent);
    // advance time to the custom user cool down duration
    this.testFrameworkRule.currentTimeDelta(customUserCoolDown, TimeUnit.SECONDS);

    final boolean result = subjectUnderTest.isCooledDown(chatCommand, chatMessageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isCooledDown_customUserCoolDownUserIsCooled_resultIsTrue() {
    this.installModules();
    final int customUserCoolDown = this.getUserCoolDownDefault() + 1;
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getUserCoolDownKey(), Integer.toString(customUserCoolDown));
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for user
    subjectUnderTest.isCooledDown(this.testFrameworkRule.getInstance(TestChatCommand.class), chatMessageEvent);
    // advance time to one millisecond longer than the custom user cool down
    // duration
    this.testFrameworkRule.currentTimeDelta(customUserCoolDown, TimeUnit.SECONDS, 1);

    final boolean result = subjectUnderTest.isCooledDown(chatCommand, chatMessageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isTriggered_nullCommand_throwsNullPointerException() {
    this.installModules();
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.isTriggered(null, mock(ChatMessageEvent.class)));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatCommand");
  }

  @Test
  public final void isTriggered_nullMessageEvent_throwsNullPointerException() {
    this.installModules();
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.isTriggered(mock(ChatCommand.class), null));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatMessageEvent");
  }

  @Test
  public final void isTriggered_commandIsTriggered_resultIsTrue() {
    this.installModules();
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    messageEvent.setChatMessage(ChatMessage.of(chatCommand.getTrigger().value));
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isTriggered(chatCommand, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isTriggered_commandIsNotTriggered_resultIsFalse() {
    this.installModules();
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isTriggered(chatCommand, messageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isTriggered_triggerIsWrongCase_resultIsTrue() {
    this.installModules();
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    messageEvent.setChatMessage(ChatMessage.of(chatCommand.getTrigger().value.toUpperCase()));
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isTriggered(chatCommand, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void createUsageMessage_nullCommand_throwsNullPointerException() {
    this.installModules();
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.createUsageMessage(null, mock(ChatMessageEvent.class)));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatCommand");
  }

  @Test
  public final void createUsageMessage_nullMessageEvent_throwsNullPointerException() {
    this.installModules();
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.createUsageMessage(mock(ChatCommand.class), null));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatMessageEvent");
  }

  @Test
  public final void createUsageMessage_noUsageMessageFormatConfigured_resultIsDefaultUsageMessage() {
    this.installModules();
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final ChatMessage result = subjectUnderTest.createUsageMessage(chatCommand, messageEvent);

    assertThat(result).isEqualTo(ChatMessage.of(String.format(this.getUsageMessageFormatDefault(),
        messageEvent.getChatUser().getTwitchLogin(), chatCommand.getTrigger(), chatCommand.getUsage())));
  }

  @Test
  public final void createUsageMessage_customUsageMessageFormatConfigured_resultIsCustomUsageMessage() {
    this.installModules();
    final String customMessageFormat = this.testFrameworkRule.getArbitraryString() + "%s %s %s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getUsageMessageFormatKey(), customMessageFormat);
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final ChatMessage result = subjectUnderTest.createUsageMessage(chatCommand, messageEvent);

    assertThat(result).isEqualTo(ChatMessage.of(String.format(customMessageFormat,
        messageEvent.getChatUser().getTwitchLogin(), chatCommand.getTrigger(), chatCommand.getUsage())));
  }

  @Test
  public final void stripTriggerFromMessage_nullCommand_throwsNullPointerException() {
    this.installModules();
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.stripTriggerFromMessage(null, mock(ChatMessageEvent.class)));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatCommand");
  }

  @Test
  public final void stripTriggerFromMessage_nullMessageEvent_throwsNullPointerException() {
    this.installModules();
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.stripTriggerFromMessage(mock(ChatCommand.class), null));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatMessageEvent");
  }

  @Test
  public final void stripTriggerFromMessage_forCommandAndMessageEvent_stripsTriggerFromMessage() {
    this.installModules();
    final String messageArgument = this.testFrameworkRule.getArbitraryString();
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", chatCommand.getTrigger(), messageArgument)));

    final ChatMessage result = subjectUnderTest.stripTriggerFromMessage(chatCommand, messageEvent);

    assertThat(result).isEqualTo(ChatMessage.of(messageArgument));
  }

  private static final class RequiresAccessLevelSubscriberChatCommand extends TestChatCommand {

    private RequiresAccessLevelSubscriberChatCommand(final CommandConfigurationBuilder commandConfigurationBuilder) {
      super(commandConfigurationBuilder);
    }

    @Override
    @RequiresUserRole(UserRole.SUBSCRIBER)
    public void handleMessageEvent(ChatMessageEvent chatMessageEvent) {
      super.handleMessageEvent(chatMessageEvent);
    }

  }

  private static final class NoCoolDownChatCommand extends TestChatCommand {

    private NoCoolDownChatCommand(final CommandConfigurationBuilder commandConfigurationBuilder) {
      super(commandConfigurationBuilder);
    }

    @Override
    @NoCoolDown
    public void handleMessageEvent(ChatMessageEvent chatMessageEvent) {
      super.handleMessageEvent(chatMessageEvent);
    }

  }

}
