package com.mechjacktv.mechjackbot;

import static com.mechjacktv.mechjackbot.CommandUtils.DEFAULT_COMMAND_COOL_DOWN;
import static com.mechjacktv.mechjackbot.CommandUtils.DEFAULT_USAGE_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.CommandUtils.DEFAULT_USER_COOL_DOWN;
import static com.mechjacktv.mechjackbot.CommandUtils.KEY_COMMAND_COOL_DOWN;
import static com.mechjacktv.mechjackbot.CommandUtils.KEY_USAGE_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.CommandUtils.KEY_USER_COOL_DOWN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;

import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;

import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.configuration.ConfigurationTestModule;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.mechjackbot.chatbot.ChatBotTestModule;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandTestModule;
import com.mechjacktv.testframework.TestFrameworkRule;
import com.mechjacktv.util.UtilTestModule;

public abstract class CommandUtilsContractTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  protected void installModules() {
    this.testFrameworkRule.installModule(new ChatBotTestModule());
    this.testFrameworkRule.installModule(new CommandTestModule());
    this.testFrameworkRule.installModule(new ConfigurationTestModule());
    this.testFrameworkRule.installModule(new UtilTestModule());
  }

  protected abstract CommandUtils givenASubjectToTest();

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
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.hasAccessLevel(null, messageEvent));

    this.testFrameworkRule.assertNullPointerException(thrown, "command");
  }

  @Test
  public final void hasAccessLevel_nullMessageEvent_throwsNullPointerException() {
    this.installModules();
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.hasAccessLevel(command, null));

    this.testFrameworkRule.assertNullPointerException(thrown, "messageEvent");
  }

  @Test
  public final void hasAccessLevel_commandHasNoRestrictions_resultIsTrue() {
    this.installModules();
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.hasAccessLevel(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void hasAccessLevel_messageEventIsFromChannelOwner_resultIsTrue() {
    this.installModules();
    final ChatBotConfiguration chatBotConfiguration = this.testFrameworkRule.getInstance(ChatBotConfiguration.class);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final TestChatUser chatUser = (TestChatUser) messageEvent.getChatUser();
    chatUser.setTwitchLogin(chatBotConfiguration.getTwitchLogin());
    chatUser.setAccessLevelCheck(accessLevel -> true);
    final Command command = new RequiresAccessLevelSubscriberCommand(
        this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class));
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.hasAccessLevel(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void hasAccessLevel_messageEventIsFromUserWithRequiredRole_resultIsTrue() {
    this.installModules();
    final ChatBotConfiguration chatBotConfiguration = this.testFrameworkRule.getInstance(ChatBotConfiguration.class);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final TestChatUser chatUser = (TestChatUser) messageEvent.getChatUser();
    // chatUser is SUBSCRIBER
    chatUser.setAccessLevelCheck(accessLevel -> AccessLevel.SUBSCRIBER.value() <= accessLevel.value());
    final Command command = new RequiresAccessLevelSubscriberCommand(
        this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class));
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.hasAccessLevel(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void hasAccessLevel_messageEventIsFromUserWithoutRequiredRole_resultIsFalse() {
    this.installModules();
    final ChatBotConfiguration chatBotConfiguration = this.testFrameworkRule.getInstance(ChatBotConfiguration.class);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final TestChatUser chatUser = (TestChatUser) messageEvent.getChatUser();
    // chatUser is FOLLOWER
    chatUser.setAccessLevelCheck(accessLevel -> AccessLevel.FOLLOWER.value() <= accessLevel.value());
    final Command command = new RequiresAccessLevelSubscriberCommand(
        this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class));
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.hasAccessLevel(command, messageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isCooledDown_nullCommand_throwsNullPointerException() {
    this.installModules();
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.isCooledDown(null, messageEvent));

    this.testFrameworkRule.assertNullPointerException(thrown, "command");
  }

  @Test
  public final void isCooledDown_nullMessageEvent_throwsNullPointerException() {
    this.installModules();
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.isCooledDown(command, null));

    this.testFrameworkRule.assertNullPointerException(thrown, "messageEvent");
  }

  @Test
  public final void isCooledDown_commandAndUserFirstCalled_resultIsTrue() {
    this.installModules();
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isCooledDown(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isCooledDown_commandIsCooling_resultIsFalse() {
    this.installModules();
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for command
    subjectUnderTest.isCooledDown(command, this.testFrameworkRule.getInstance(MessageEvent.class));
    // advance time to the command cool down duration
    this.testFrameworkRule.currentTimeDelta(this.getCommandCoolDownDefault(), TimeUnit.SECONDS);

    final boolean result = subjectUnderTest.isCooledDown(command, messageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isCooledDown_commandIsCooled_resultIsTrue() {
    this.installModules();
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for command
    subjectUnderTest.isCooledDown(command, this.testFrameworkRule.getInstance(MessageEvent.class));
    // advance time to one millisecond longer than the command cool down duration
    this.testFrameworkRule.currentTimeDelta(this.getCommandCoolDownDefault(), TimeUnit.SECONDS, 1);

    final boolean result = subjectUnderTest.isCooledDown(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isCooledDown_userIsCooling_resultIsFalse() {
    this.installModules();
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for user
    subjectUnderTest.isCooledDown(this.testFrameworkRule.getInstance(TestCommand.class), messageEvent);
    // advance time to the user cool down duration
    this.testFrameworkRule.currentTimeDelta(this.getUserCoolDownDefault(), TimeUnit.SECONDS);

    final boolean result = subjectUnderTest.isCooledDown(command, messageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isCooledDown_userIsCooled_resultIsTrue() {
    this.installModules();
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for user
    subjectUnderTest.isCooledDown(this.testFrameworkRule.getInstance(TestCommand.class), messageEvent);
    // advance time to one millisecond longer than the user cool down duration
    this.testFrameworkRule.currentTimeDelta(this.getUserCoolDownDefault(), TimeUnit.SECONDS, 1);

    final boolean result = subjectUnderTest.isCooledDown(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isCooledDown_commandAndUserAreCooling_resultIsFalse() {
    this.installModules();
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for command and user
    subjectUnderTest.isCooledDown(command, messageEvent);
    // advance time to the shorter cool down duration
    this.testFrameworkRule.currentTimeDelta(Math.min(this.getCommandCoolDownDefault(),
        this.getUserCoolDownDefault()), TimeUnit.SECONDS);

    final boolean result = subjectUnderTest.isCooledDown(command, messageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isCooledDown_commandAndUserAreCooled_resultIsTrue() {
    this.installModules();
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for command and user
    subjectUnderTest.isCooledDown(command, messageEvent);
    // advance time to one millisecond longer than the longer cool down duration
    this.testFrameworkRule.currentTimeDelta(Math.max(this.getCommandCoolDownDefault(),
        this.getUserCoolDownDefault()), TimeUnit.SECONDS, 1);

    final boolean result = subjectUnderTest.isCooledDown(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isCooledDown_noCoolDownCommandBothWouldBeCooling_resultIsTrue() {
    this.installModules();
    final Command command = new NoCoolDownCommand(
        this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class));
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for command and user
    subjectUnderTest.isCooledDown(command, messageEvent);
    // advance time to the shorter cool down duration
    this.testFrameworkRule.currentTimeDelta(Math.min(this.getCommandCoolDownDefault(),
        this.getUserCoolDownDefault()), TimeUnit.SECONDS);

    final boolean result = subjectUnderTest.isCooledDown(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isCooledDown_privilegedUserBothWouldBeCooling_resultIsTrue() {
    this.installModules();
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final TestChatUser chatUser = (TestChatUser) messageEvent.getChatUser();
    // chatUser is MODERATOR
    chatUser.setAccessLevelCheck(accessLevel -> AccessLevel.MODERATOR.value() >= accessLevel.value());
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for command and user
    subjectUnderTest.isCooledDown(command, messageEvent);
    // advance time to the shorter cool down duration
    this.testFrameworkRule.currentTimeDelta(Math.min(this.getCommandCoolDownDefault(),
        this.getUserCoolDownDefault()), TimeUnit.SECONDS);

    final boolean result = subjectUnderTest.isCooledDown(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isCooledDown_customCommandCoolDownCommandIsCooling_resultIsFalse() {
    this.installModules();
    final int customCommandCoolDown = this.getCommandCoolDownDefault() + 1;
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getCommandCoolDownKey(), Integer.toString(customCommandCoolDown));
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for command
    subjectUnderTest.isCooledDown(command, this.testFrameworkRule.getInstance(MessageEvent.class));
    // advance time to the custom command cool down duration
    this.testFrameworkRule.currentTimeDelta(customCommandCoolDown, TimeUnit.SECONDS);

    final boolean result = subjectUnderTest.isCooledDown(command, messageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isCooledDown_customCommandCoolDownCommandIsCooled_resultIsTrue() {
    this.installModules();
    final int customCommandCoolDown = this.getCommandCoolDownDefault() + 1;
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getCommandCoolDownKey(), Integer.toString(customCommandCoolDown));
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for command
    subjectUnderTest.isCooledDown(command, this.testFrameworkRule.getInstance(MessageEvent.class));
    // advance time to one millisecond past custom command cool down duration
    this.testFrameworkRule.currentTimeDelta(customCommandCoolDown, TimeUnit.SECONDS, 1);

    final boolean result = subjectUnderTest.isCooledDown(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isCooledDown_customUserCoolDownUserIsCooling_resultIsFalse() {
    this.installModules();
    final int customUserCoolDown = this.getUserCoolDownDefault() + 1;
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getUserCoolDownKey(), Integer.toString(customUserCoolDown));
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for user
    subjectUnderTest.isCooledDown(this.testFrameworkRule.getInstance(TestCommand.class), messageEvent);
    // advance time to the custom user cool down duration
    this.testFrameworkRule.currentTimeDelta(customUserCoolDown, TimeUnit.SECONDS);

    final boolean result = subjectUnderTest.isCooledDown(command, messageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isCooledDown_customUserCoolDownUserIsCooled_resultIsTrue() {
    this.installModules();
    final int customUserCoolDown = this.getUserCoolDownDefault() + 1;
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getUserCoolDownKey(), Integer.toString(customUserCoolDown));
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final MessageEvent messageEvent = this.testFrameworkRule.getInstance(MessageEvent.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    // set lastTrigger for user
    subjectUnderTest.isCooledDown(this.testFrameworkRule.getInstance(TestCommand.class), messageEvent);
    // advance time to one millisecond longer than the custom user cool down
    // duration
    this.testFrameworkRule.currentTimeDelta(customUserCoolDown, TimeUnit.SECONDS, 1);

    final boolean result = subjectUnderTest.isCooledDown(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isTriggered_nullCommand_throwsNullPointerException() {
    this.installModules();
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.isTriggered(null, mock(MessageEvent.class)));

    this.testFrameworkRule.assertNullPointerException(thrown, "command");
  }

  @Test
  public final void isTriggered_nullMessageEvent_throwsNullPointerException() {
    this.installModules();
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.isTriggered(mock(Command.class), null));

    this.testFrameworkRule.assertNullPointerException(thrown, "messageEvent");
  }

  @Test
  public final void isTriggered_commandIsTriggered_resultIsTrue() {
    this.installModules();
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    messageEvent.setMessage(Message.of(command.getTrigger().value));
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isTriggered(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isTriggered_commandIsNotTriggered_resultIsFalse() {
    this.installModules();
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isTriggered(command, messageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isTriggered_triggerIsWrongCase_resultIsTrue() {
    this.installModules();
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    messageEvent.setMessage(Message.of(command.getTrigger().value.toUpperCase()));
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isTriggered(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void createUsageMessage_nullCommand_throwsNullPointerException() {
    this.installModules();
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.createUsageMessage(null, mock(MessageEvent.class)));

    this.testFrameworkRule.assertNullPointerException(thrown, "command");
  }

  @Test
  public final void createUsageMessage_nullMessageEvent_throwsNullPointerException() {
    this.installModules();
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.createUsageMessage(mock(Command.class), null));

    this.testFrameworkRule.assertNullPointerException(thrown, "messageEvent");
  }

  @Test
  public final void createUsageMessage_noUsageMessageFormatConfigured_resultIsDefaultUsageMessage() {
    this.installModules();
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Message result = subjectUnderTest.createUsageMessage(command, messageEvent);

    assertThat(result).isEqualTo(Message.of(String.format(this.getUsageMessageFormatDefault(),
        messageEvent.getChatUser().getTwitchLogin(), command.getTrigger(), command.getUsage())));
  }

  @Test
  public final void createUsageMessage_customUsageMessageFormatConfigured_resultIsCustomUsageMessage() {
    this.installModules();
    final String customMessageFormat = this.testFrameworkRule.getArbitraryString() + "%s %s %s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getUsageMessageFormatKey(), customMessageFormat);
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Message result = subjectUnderTest.createUsageMessage(command, messageEvent);

    assertThat(result).isEqualTo(Message.of(String.format(customMessageFormat,
        messageEvent.getChatUser().getTwitchLogin(), command.getTrigger(), command.getUsage())));
  }

  @Test
  public final void stripTriggerFromMessage_nullCommand_throwsNullPointerException() {
    this.installModules();
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.stripTriggerFromMessage(null, mock(MessageEvent.class)));

    this.testFrameworkRule.assertNullPointerException(thrown, "command");
  }

  @Test
  public final void stripTriggerFromMessage_nullMessageEvent_throwsNullPointerException() {
    this.installModules();
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.stripTriggerFromMessage(mock(Command.class), null));

    this.testFrameworkRule.assertNullPointerException(thrown, "messageEvent");
  }

  @Test
  public final void stripTriggerFromMessage_forCommandAndMessageEvent_stripsTriggerFromMessage() {
    this.installModules();
    final String messageArgument = this.testFrameworkRule.getArbitraryString();
    final Command command = this.testFrameworkRule.getInstance(TestCommand.class);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setMessage(Message.of(String.format("%s %s", command.getTrigger(), messageArgument)));

    final Message result = subjectUnderTest.stripTriggerFromMessage(command, messageEvent);

    assertThat(result).isEqualTo(Message.of(messageArgument));
  }

  private static final class RequiresAccessLevelSubscriberCommand extends TestCommand {

    private RequiresAccessLevelSubscriberCommand(final CommandConfigurationBuilder commandConfigurationBuilder) {
      super(commandConfigurationBuilder);
    }

    @Override
    @RequiresAccessLevel(AccessLevel.SUBSCRIBER)
    public void handleMessageEvent(MessageEvent messageEvent) {
      super.handleMessageEvent(messageEvent);
    }

  }

  private static final class NoCoolDownCommand extends TestCommand {

    private NoCoolDownCommand(final CommandConfigurationBuilder commandConfigurationBuilder) {
      super(commandConfigurationBuilder);
    }

    @Override
    @NoCoolDown
    public void handleMessageEvent(MessageEvent messageEvent) {
      super.handleMessageEvent(messageEvent);
    }

  }

}
