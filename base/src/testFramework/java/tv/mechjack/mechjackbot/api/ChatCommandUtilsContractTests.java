package tv.mechjack.mechjackbot.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static tv.mechjack.mechjackbot.api.ChatCommandUtils.DEFAULT_COMMAND_COOL_DOWN;
import static tv.mechjack.mechjackbot.api.ChatCommandUtils.DEFAULT_USAGE_MESSAGE_FORMAT;
import static tv.mechjack.mechjackbot.api.ChatCommandUtils.DEFAULT_USER_COOL_DOWN;
import static tv.mechjack.mechjackbot.api.ChatCommandUtils.KEY_COMMAND_COOL_DOWN;
import static tv.mechjack.mechjackbot.api.ChatCommandUtils.KEY_USAGE_MESSAGE_FORMAT;
import static tv.mechjack.mechjackbot.api.ChatCommandUtils.KEY_USER_COOL_DOWN;

import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.mechjackbot.chatbot.TestChatBotModule;
import tv.mechjack.platform.configuration.ConfigurationKey;
import tv.mechjack.platform.configuration.MapConfiguration;
import tv.mechjack.platform.configuration.TestConfigurationModule;
import tv.mechjack.platform.utils.TestUtilsModule;
import tv.mechjack.testframework.TestFramework;

public abstract class ChatCommandUtilsContractTests {

  @Rule
  public final TestFramework testFrameworkRule = new TestFramework();

  protected void installModules() {
    this.testFrameworkRule.registerModule(new TestChatBotModule());
    this.testFrameworkRule.registerModule(new TestCommandModule());
    this.testFrameworkRule.registerModule(new TestConfigurationModule());
    this.testFrameworkRule.registerModule(new TestUtilsModule());
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
    chatUser.setHasAccessLevelHandler(accessLevel -> true);
    final ChatCommand chatCommand = new RequiresAccessLevelSubscriberChatCommand(
        this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class));
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.hasAccessLevel(chatCommand, chatMessageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void hasAccessLevel_messageEventIsFromUserWithRequiredRole_resultIsTrue() {
    this.installModules();
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final TestChatUser chatUser = (TestChatUser) chatMessageEvent.getChatUser();
    // chatUser is SUBSCRIBER
    chatUser.setHasAccessLevelHandler(accessLevel -> UserRole.SUBSCRIBER.accessLevel() <= accessLevel.accessLevel());
    final ChatCommand chatCommand = new RequiresAccessLevelSubscriberChatCommand(
        this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class));
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.hasAccessLevel(chatCommand, chatMessageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void hasAccessLevel_messageEventIsFromUserWithoutRequiredRole_resultIsFalse() {
    this.installModules();
    final ChatMessageEvent chatMessageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final TestChatUser chatUser = (TestChatUser) chatMessageEvent.getChatUser();
    chatUser.setHasAccessLevelHandler(accessLevel -> UserRole.VIEWER.accessLevel() <= accessLevel.accessLevel());
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
    chatUser.setHasAccessLevelHandler(accessLevel -> UserRole.MODERATOR.accessLevel() <= accessLevel.accessLevel());
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

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.isTriggered(null, this.testFrameworkRule.fakeFactory().fake(ChatMessageEvent.class)));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatCommand");
  }

  @Test
  public final void isTriggered_nullMessageEvent_throwsNullPointerException() {
    this.installModules();
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.isTriggered(this.testFrameworkRule.fakeFactory().fake(ChatCommand.class), null));

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
        () -> subjectUnderTest.createUsageMessage(null, this.testFrameworkRule.fakeFactory().fake(ChatMessageEvent.class)));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatCommand");
  }

  @Test
  public final void createUsageMessage_nullMessageEvent_throwsNullPointerException() {
    this.installModules();
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.createUsageMessage(this.testFrameworkRule.fakeFactory().fake(ChatCommand.class), null));

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
        chatCommand.getUsage())));
  }

  @Test
  public final void createUsageMessage_customUsageMessageFormatConfigured_resultIsCustomUsageMessage() {
    this.installModules();
    final String customMessageFormat = this.testFrameworkRule.arbitraryData().getString() + "$(user) $(trigger) %s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getUsageMessageFormatKey(), customMessageFormat);
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final ChatMessage result = subjectUnderTest.createUsageMessage(chatCommand, messageEvent);

    assertThat(result).isEqualTo(ChatMessage.of(String.format(customMessageFormat, chatCommand.getUsage())));
  }

  @Test
  public final void replaceChatMessageVariables_nullCommand_throwsNullPointerException() {
    this.installModules();
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.replaceChatMessageVariables(null, this.testFrameworkRule.fakeFactory().fake(ChatMessageEvent.class),
            ChatMessage.of(this.testFrameworkRule.arbitraryData().getString())));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatCommand");
  }

  @Test
  public final void replaceChatMessageVariables_nullMessageEvent_throwsNullPointerException() {
    this.installModules();
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.replaceChatMessageVariables(this.testFrameworkRule.fakeFactory().fake(ChatCommand.class), null,
            ChatMessage.of(this.testFrameworkRule.arbitraryData().getString())));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatMessageEvent");
  }

  @Test
  public final void replaceChatMessageVariables_nullChatMessage_throwsNullPointerException() {
    this.installModules();
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.replaceChatMessageVariables(this.testFrameworkRule.fakeFactory().fake(ChatCommand.class),
            this.testFrameworkRule.fakeFactory().fake(ChatMessageEvent.class),
            null));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatMessage");
  }

  @Test
  public final void replaceChatMessageVariables_noVariablePresent_resultIsUnchangedMessage() {
    this.installModules();
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ChatMessage chatMessage = ChatMessage.of(this.testFrameworkRule.arbitraryData().getString());
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final ChatMessage result = subjectUnderTest.replaceChatMessageVariables(chatCommand, messageEvent, chatMessage);

    assertThat(result).isEqualTo(chatMessage);
  }

  @Test
  public final void replaceChatMessageVariables_triggerVariablePresent_resultIsTriggerVariableReplaced() {
    this.installModules();
    final String messageFormat = this.testFrameworkRule.arbitraryData().getString() + " %s";
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final ChatMessage result = subjectUnderTest.replaceChatMessageVariables(chatCommand, messageEvent,
        ChatMessage.of(String.format(messageFormat, "$(trigger)")));

    assertThat(result).isEqualTo(ChatMessage.of(String.format(messageFormat, chatCommand.getTrigger())));
  }

  @Test
  public final void replaceChatMessageVariables_userVariablePresent_resultIsUserVariableReplaced() {
    this.installModules();
    final String messageFormat = this.testFrameworkRule.arbitraryData().getString() + " %s";
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final ChatMessage result = subjectUnderTest.replaceChatMessageVariables(chatCommand, messageEvent,
        ChatMessage.of(String.format(messageFormat, "$(user)")));

    assertThat(result).isEqualTo(ChatMessage.of(String.format(messageFormat,
        messageEvent.getChatUser().getTwitchLogin())));
  }

  @Test
  public final void replaceChatMessageVariables_multipleVariablesPresent_resultIsAllVariablesReplaced() {
    this.installModules();
    final String messageFormat = this.testFrameworkRule.arbitraryData().getString() + " %s %s";
    final ChatCommand chatCommand = this.testFrameworkRule.getInstance(TestChatCommand.class);
    final ChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(ChatMessageEvent.class);
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final ChatMessage result = subjectUnderTest.replaceChatMessageVariables(chatCommand, messageEvent,
        ChatMessage.of(String.format(messageFormat, "$(trigger)", "$(user)")));

    assertThat(result).isEqualTo(ChatMessage.of(String.format(messageFormat, chatCommand.getTrigger(),
        messageEvent.getChatUser().getTwitchLogin())));
  }

  @Test
  public final void stripTriggerFromMessage_nullCommand_throwsNullPointerException() {
    this.installModules();
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.stripTriggerFromMessage(null, this.testFrameworkRule.fakeFactory().fake(ChatMessageEvent.class)));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatCommand");
  }

  @Test
  public final void stripTriggerFromMessage_nullMessageEvent_throwsNullPointerException() {
    this.installModules();
    final ChatCommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.stripTriggerFromMessage(this.testFrameworkRule.fakeFactory().fake(ChatCommand.class), null));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatMessageEvent");
  }

  @Test
  public final void stripTriggerFromMessage_forCommandAndMessageEvent_stripsTriggerFromMessage() {
    this.installModules();
    final String messageArgument = this.testFrameworkRule.arbitraryData().getString();
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
    @RequiresAccessLevel(UserRole.SUBSCRIBER)
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
