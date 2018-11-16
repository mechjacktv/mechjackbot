package com.mechjacktv.mechjackbot;

import static com.mechjacktv.mechjackbot.CommandUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.mechjacktv.mechjackbot.configuration.ArbitraryChatBotConfiguration;
import com.mechjacktv.mechjackbot.configuration.MapAppConfiguration;
import com.mechjacktv.util.*;

public abstract class CommandUtilsContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private final ArbitraryChatBotConfiguration chatBotConfiguration = new ArbitraryChatBotConfiguration(
      this.arbitraryDataGenerator);

  private final ExecutionUtils executionUtils = new DefaultExecutionUtils();

  private final MapAppConfiguration appConfiguration = new MapAppConfiguration(this.executionUtils);

  private final TimeUtils timeUtils = new DefaultTimeUtils();

  private CommandUtils givenASubjectToTest() {
    return this.givenASubjectToTest(this.appConfiguration, this.chatBotConfiguration, this.timeUtils);
  }

  private CommandUtils givenASubjectToTest(final TimeUtils timeUtils) {
    return this.givenASubjectToTest(this.appConfiguration, this.chatBotConfiguration, timeUtils);
  }

  protected abstract CommandUtils givenASubjectToTest(final AppConfiguration appConfiguration,
      final ChatBotConfiguration chatBotConfiguration, final TimeUtils timeUtils);

  private MessageEvent givenAFakeMessageEvent() {
    final MessageEvent messageEvent = mock(MessageEvent.class);
    final ChatUser chatUser = mock(ChatUser.class);

    when(messageEvent.getChatUser()).thenReturn(chatUser);
    when(chatUser.getUsername()).thenReturn(ChatUsername.of(this.arbitraryDataGenerator.getString()));
    return messageEvent;
  }

  private TimeUtils givenAFakeTimeUtils() {
    final TimeUtils timeUtils = mock(TimeUtils.class);

    when(timeUtils.currentTime()).thenAnswer((invocation -> this.timeUtils.currentTime()));
    when(timeUtils.secondsAsMs(isA(Integer.class)))
        .thenAnswer((invocation -> this.timeUtils.secondsAsMs(invocation.getArgument(0))));
    when(timeUtils.hoursAsMs(isA(Integer.class)))
        .thenAnswer((invocation -> this.timeUtils.hoursAsMs(invocation.getArgument(0))));
    return timeUtils;
  }

  @Test
  public final void hasRole_nullCommand_throwsNullPointerException() {
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.hasRole(null, mock(MessageEvent.class)));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("command"));
  }

  @Test
  public final void hasRole_nullMessageEvent_throwsNullPointerException() {
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.hasRole(mock(Command.class), null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("messageEvent"));
  }

  @Test
  public final void hasRole_commandHasNoRestrictions_returnsTrue() {
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.hasRole(mock(Command.class), mock(MessageEvent.class));

    assertThat(result).isTrue();
  }

  @Test
  public final void hasRole_userIsOwner_returnsTrue() {
    final ChatUser chatUser = mock(ChatUser.class);
    final MessageEvent messageEvent = mock(MessageEvent.class);
    when(messageEvent.getChatUser()).thenReturn(chatUser);
    when(chatUser.getUsername()).thenReturn(ChatUsername.of(this.chatBotConfiguration.getTwitchChannel().value));
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    final Command command = new RestrictedCommand(this, subjectUnderTest);

    final boolean result = subjectUnderTest.hasRole(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void hasRole_userHasRoles_returnsTrue() {
    final ChatUser chatUser = mock(ChatUser.class);
    final MessageEvent messageEvent = mock(MessageEvent.class);
    when(messageEvent.getChatUser()).thenReturn(chatUser);
    when(chatUser.getUsername()).thenReturn(ChatUsername.of(this.arbitraryDataGenerator.getString()));
    when(chatUser.hasRole(eq(ViewerRole.SUBSCRIBER))).thenReturn(true);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    final Command command = new RestrictedCommand(this, subjectUnderTest);

    final boolean result = subjectUnderTest.hasRole(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void hasRole_userDoesNotHaveRoles_returnsFalse() {
    final ChatUser chatUser = mock(ChatUser.class);
    final MessageEvent messageEvent = mock(MessageEvent.class);
    when(messageEvent.getChatUser()).thenReturn(chatUser);
    when(chatUser.getUsername()).thenReturn(ChatUsername.of(this.arbitraryDataGenerator.getString()));
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    final Command command = new RestrictedCommand(this, subjectUnderTest);

    final boolean result = subjectUnderTest.hasRole(command, messageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isCooledDown_nullCommand_throwsNullPointerException() {
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.isCooledDown(null, mock(MessageEvent.class)));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("command"));
  }

  @Test
  public final void isCooledDown_nullMessageEvent_throwsNullPointerException() {
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.isCooledDown(mock(Command.class), null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("messageEvent"));
  }

  @Test
  public final void isCooledDown_commandAndUserFirstCalled_returnsTrue() {
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final boolean result = subjectUnderTest.isCooledDown(new RestrictedCommand(this, subjectUnderTest),
        this.givenAFakeMessageEvent());

    assertThat(result).isTrue();
  }

  @Test
  public final void isCooledDown_commandIsCooledViewerIsNot_returnsFalse() {
    final Long originTime = this.arbitraryDataGenerator.getLong();
    final TimeUtils timeUtils = this.givenAFakeTimeUtils();
    when(timeUtils.currentTime()).thenReturn(originTime, originTime + 1);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest(timeUtils);
    final Command command = new RestrictedCommand(this, subjectUnderTest);
    // set command lastTrigger
    subjectUnderTest.isCooledDown(command, this.givenAFakeMessageEvent());

    final boolean result = subjectUnderTest.isCooledDown(command, this.givenAFakeMessageEvent());

    assertThat(result).isFalse();
  }

  @Test
  public final void isCooledDown_viewerIsCoolCommandIsNot_returnsFalse() {
    final Long originTime = this.arbitraryDataGenerator.getLong();
    final TimeUtils timeUtils = this.givenAFakeTimeUtils();
    when(timeUtils.currentTime()).thenReturn(originTime, originTime + 1);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest(timeUtils);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    // set viewer lastTrigger
    subjectUnderTest.isCooledDown(new RestrictedCommand(this, subjectUnderTest), messageEvent);

    final boolean result = subjectUnderTest.isCooledDown(new RestrictedCommand(this, subjectUnderTest), messageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isCooledDown_viewerAndCommandAreCooled_returnsFalse() {
    final Long originTime = this.arbitraryDataGenerator.getLong();
    final int userCoolDown = Integer.parseInt(CommandUtils.COMMAND_VIEWER_COOL_DOWN_DEFAULT);
    final TimeUtils timeUtils = this.givenAFakeTimeUtils();
    when(timeUtils.currentTime()).thenReturn(originTime,
        originTime + this.timeUtils.secondsAsMs(userCoolDown + 1));
    final CommandUtils subjectUnderTest = this.givenASubjectToTest(timeUtils);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    final Command command = new RestrictedCommand(this, subjectUnderTest);
    // set lastTriggers
    subjectUnderTest.isCooledDown(command, messageEvent);

    final boolean result = subjectUnderTest.isCooledDown(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isCooledDown_noCoolDownCommand_returnsTrue() {
    final Long originTime = this.arbitraryDataGenerator.getLong();
    final TimeUtils timeUtils = this.givenAFakeTimeUtils();
    when(timeUtils.currentTime()).thenReturn(originTime, originTime + 1);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest(timeUtils);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    final Command command = new NoCoolDownCommand(this, subjectUnderTest);
    // set lastTriggers
    subjectUnderTest.isCooledDown(command, messageEvent);

    final boolean result = subjectUnderTest.isCooledDown(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isCooledDown_privilegedViewer_returnsTrue() {
    final Long originTime = this.arbitraryDataGenerator.getLong();
    final TimeUtils timeUtils = this.givenAFakeTimeUtils();
    when(timeUtils.currentTime()).thenReturn(originTime, originTime + 1);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest(timeUtils);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    when(messageEvent.getChatUser().hasRole(eq(ViewerRole.MODERATOR))).thenReturn(true);
    final Command command = new RestrictedCommand(this, subjectUnderTest);
    // set lastTriggers
    subjectUnderTest.isCooledDown(command, messageEvent);

    final boolean result = subjectUnderTest.isCooledDown(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isCooledDown_customCommandCoolDownNotCool_returnsFalse() {
    final Long originTime = this.arbitraryDataGenerator.getLong();
    final int defaultCommandCoolDown = Integer.parseInt(CommandUtils.COMMAND_COMMAND_COOL_DOWN_DEFAULT);
    final String customCommandCoolDown = Integer.toString(defaultCommandCoolDown + 1);
    this.appConfiguration.set(COMMAND_COMMAND_COOL_DOWN_KEY, customCommandCoolDown);
    final TimeUtils timeUtils = this.givenAFakeTimeUtils();
    when(timeUtils.currentTime()).thenReturn(originTime,
        originTime + 1 + this.timeUtils.secondsAsMs(defaultCommandCoolDown));
    final CommandUtils subjectUnderTest = this.givenASubjectToTest(timeUtils);
    final Command command = new RestrictedCommand(this, subjectUnderTest);
    // set command lastTrigger
    subjectUnderTest.isCooledDown(command, this.givenAFakeMessageEvent());

    final boolean result = subjectUnderTest.isCooledDown(command, this.givenAFakeMessageEvent());

    assertThat(result).isFalse();
  }

  @Test
  public final void isCooledDown_customCommandCoolDownCooledDown_returnsTrue() {
    final int delta = 1;
    final Long originTime = this.arbitraryDataGenerator.getLong();
    final int defaultCommandCoolDown = Integer.parseInt(CommandUtils.COMMAND_COMMAND_COOL_DOWN_DEFAULT);
    final String customCommandCoolDown = Integer.toString(defaultCommandCoolDown + delta);
    this.appConfiguration.set(COMMAND_COMMAND_COOL_DOWN_KEY, customCommandCoolDown);
    final TimeUtils timeUtils = this.givenAFakeTimeUtils();
    when(timeUtils.currentTime()).thenReturn(originTime,
        originTime + 1 + this.timeUtils.secondsAsMs(defaultCommandCoolDown + delta));
    final CommandUtils subjectUnderTest = this.givenASubjectToTest(timeUtils);
    final Command command = new RestrictedCommand(this, subjectUnderTest);
    // set command lastTrigger
    subjectUnderTest.isCooledDown(command, this.givenAFakeMessageEvent());

    final boolean result = subjectUnderTest.isCooledDown(command, this.givenAFakeMessageEvent());

    assertThat(result).isTrue();
  }

  @Test
  public final void isCooledDown_customViewerCoolDownNotCooled_returnsFalse() {
    final Long originTime = this.arbitraryDataGenerator.getLong();
    final int defaultViewerCoolDown = Integer.parseInt(CommandUtils.COMMAND_VIEWER_COOL_DOWN_DEFAULT);
    final String customViewerCoolDown = Integer.toString(defaultViewerCoolDown + 1);
    this.appConfiguration.set(COMMAND_VIEWER_COOL_DOWN_KEY, customViewerCoolDown);
    final TimeUtils timeUtils = this.givenAFakeTimeUtils();
    when(timeUtils.currentTime()).thenReturn(originTime,
        originTime + 1 + this.timeUtils.secondsAsMs(defaultViewerCoolDown));
    final CommandUtils subjectUnderTest = this.givenASubjectToTest(timeUtils);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    // set command lastTrigger
    subjectUnderTest.isCooledDown(new RestrictedCommand(this, subjectUnderTest), messageEvent);

    final boolean result = subjectUnderTest.isCooledDown(new RestrictedCommand(this, subjectUnderTest), messageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isCooledDown_customViewerCoolDownCooledDown_returnsTrue() {
    final int delta = 1;
    final Long originTime = this.arbitraryDataGenerator.getLong();
    final int defaultViewerCoolDown = Integer.parseInt(CommandUtils.COMMAND_VIEWER_COOL_DOWN_DEFAULT);
    final String customViewerCoolDown = Integer.toString(defaultViewerCoolDown + delta);
    this.appConfiguration.set(COMMAND_VIEWER_COOL_DOWN_KEY, customViewerCoolDown);
    final TimeUtils timeUtils = this.givenAFakeTimeUtils();
    when(timeUtils.currentTime()).thenReturn(originTime,
        originTime + 1 + this.timeUtils.secondsAsMs(defaultViewerCoolDown + delta));
    final CommandUtils subjectUnderTest = this.givenASubjectToTest(timeUtils);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    // set command lastTrigger
    subjectUnderTest.isCooledDown(new RestrictedCommand(this, subjectUnderTest), messageEvent);

    final boolean result = subjectUnderTest.isCooledDown(new RestrictedCommand(this, subjectUnderTest), messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isTriggered_nullCommand_throwsNullPointerException() {
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.isTriggered(null, mock(MessageEvent.class)));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("command"));
  }

  @Test
  public final void isTriggered_nullMessageEvent_throwsNullPointerException() {
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.isTriggered(mock(Command.class), null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("messageEvent"));
  }

  @Test
  public final void isTriggered_messageEventForCommand_returnsTrue() {
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    final Command command = new RestrictedCommand(this, subjectUnderTest);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    when(messageEvent.getMessage()).thenReturn(Message.of(command.getTrigger().value));

    final boolean result = subjectUnderTest.isTriggered(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isTriggered_messageEventNotForCommand_returnsTrue() {
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    final Command command = new RestrictedCommand(this, subjectUnderTest);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    when(messageEvent.getMessage()).thenReturn(Message.of(this.arbitraryDataGenerator.getString()));

    final boolean result = subjectUnderTest.isTriggered(command, messageEvent);

    assertThat(result).isFalse();
  }

  @Test
  public final void isTriggered_triggerIsWrongCase_returnsTrue() {
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    final Command command = new RestrictedCommand(this, subjectUnderTest);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    when(messageEvent.getMessage()).thenReturn(Message.of(command.getTrigger().value.toUpperCase()));

    final boolean result = subjectUnderTest.isTriggered(command, messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void sendUsage_nullCommand_throwsNullPointerException() {
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.sendUsage(null, mock(MessageEvent.class)));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("command"));
  }

  @Test
  public final void sendUsage_nullMessageEvent_throwsNullPointerException() {
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.sendUsage(mock(Command.class), null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("messageEvent"));
  }

  @Test
  public final void sendUsage_forCommand_retrievedChatUsername() {
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.sendUsage(mock(Command.class), messageEvent);

    verify(messageEvent.getChatUser()).getUsername();
  }

  @Test
  public final void sendUsage_forCommand_sanitizesChatUsername() {
    final ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    doNothing().when(messageEvent).sendResponse(argumentCaptor.capture());
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.sendUsage(mock(Command.class), messageEvent);
    final Message result = argumentCaptor.getValue();

    assertThat(result.value)
        .contains(subjectUnderTest.sanitizeChatUsername(messageEvent.getChatUser().getUsername()).value);
  }

  @Test
  public final void sendUsage_forCommand_retrievedTrigger() {
    final Command command = mock(Command.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.sendUsage(command, this.givenAFakeMessageEvent());

    verify(command).getTrigger();
  }

  @Test
  public final void sendUsage_forCommand_retrievedUsage() {
    final Command command = mock(Command.class);
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.sendUsage(command, this.givenAFakeMessageEvent());

    verify(command).getUsage();
  }

  @Test
  public final void sendUsage_customMessageFormat_sendsCustomMessageFormat() {
    final String customMessageFormat = "%s, %s takes %s";
    this.appConfiguration.set(COMMAND_USAGE_MESSAGE_FORMAT_KEY, customMessageFormat);
    final ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    doNothing().when(messageEvent).sendResponse(argumentCaptor.capture());
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    final Command command = new RestrictedCommand(this, subjectUnderTest);

    subjectUnderTest.sendUsage(command, messageEvent);
    final Message result = argumentCaptor.getValue();

    assertThat(result).isEqualTo(Message.of(String.format(customMessageFormat,
        subjectUnderTest.sanitizeChatUsername(messageEvent.getChatUser().getUsername()), command.getTrigger(),
        command.getUsage())));
  }

  @Test
  public final void messageWithoutTrigger_nullCommand_throwsNullPointerException() {
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(
        () -> subjectUnderTest.messageWithoutTrigger(null, mock(MessageEvent.class)));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("command"));
  }

  @Test
  public final void messageWithoutTrigger_nullMessageEvent_throwsNullPointerException() {
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.messageWithoutTrigger(mock(Command.class), null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("messageEvent"));
  }

  @Test
  public final void messageWithoutTrigger_forCommandAndMessageEvent_stripsTriggerFromMessage() {
    final String messageArguments = this.arbitraryDataGenerator.getString();
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();
    final Command command = new RestrictedCommand(this, subjectUnderTest);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    when(messageEvent.getMessage()).thenReturn(Message.of(String.format("%s %s", command.getTrigger(),
        messageArguments)));

    final Message result = subjectUnderTest.messageWithoutTrigger(command, messageEvent);

    assertThat(result).isEqualTo(Message.of(messageArguments));
  }

  @Test
  public final void sanitizeChatUsername_nullChatUsername_throwsNullPointerException() {
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.sanitizeChatUsername(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("chatUsername"));
  }

  @Test
  public final void sanitizeChatUsername_noSanitizationNeeded_returnsUnchanged() {
    final ChatUsername chatUsername = ChatUsername.of(this.arbitraryDataGenerator.getString().toLowerCase());
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final ChatUsername result = subjectUnderTest.sanitizeChatUsername(chatUsername);

    assertThat(result).isEqualTo(chatUsername);
  }

  @Test
  public final void sanitizeChatUsername_needLowerCasing_lowerCases() {
    final String chatUsername = this.arbitraryDataGenerator.getString();
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final ChatUsername result = subjectUnderTest.sanitizeChatUsername(ChatUsername.of(chatUsername));

    assertThat(result).isEqualTo(ChatUsername.of(chatUsername.toLowerCase()));
  }

  @Test
  public final void sanitizeChatUsername_hasAmpersand_stripsAmpersand() {
    final String chatUsername = this.arbitraryDataGenerator.getString().toLowerCase();
    final CommandUtils subjectUnderTest = this.givenASubjectToTest();

    final ChatUsername result = subjectUnderTest.sanitizeChatUsername(ChatUsername.of("@" + chatUsername));

    assertThat(result).isEqualTo(ChatUsername.of(chatUsername));
  }

  private static final class RestrictedCommand extends AbstractCommand {

    private RestrictedCommand(final CommandUtilsContractTests testSuite, final CommandUtils commandUtils) {
      super(new Configuration(testSuite.appConfiguration, commandUtils,
          CommandDescription.of(testSuite.arbitraryDataGenerator.getString()),
          CommandTriggerKey.of(testSuite.arbitraryDataGenerator.getString()),
          CommandTrigger.of(testSuite.arbitraryDataGenerator.getString())));
    }

    @Override
    @RestrictToRoles({ ViewerRole.MODERATOR, ViewerRole.SUBSCRIBER })
    public void handleMessageEvent(MessageEvent messageEvent) {
      // empty
    }
  }

  private static final class NoCoolDownCommand extends AbstractCommand {

    private NoCoolDownCommand(final CommandUtilsContractTests testSuite, final CommandUtils commandUtils) {
      super(new Configuration(testSuite.appConfiguration, commandUtils,
          CommandDescription.of(testSuite.arbitraryDataGenerator.getString()),
          CommandTriggerKey.of(testSuite.arbitraryDataGenerator.getString()),
          CommandTrigger.of(testSuite.arbitraryDataGenerator.getString())));
    }

    @Override
    @NoCoolDown
    public void handleMessageEvent(MessageEvent messageEvent) {
      // empty
    }
  }

}
