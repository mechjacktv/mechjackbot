package com.mechjacktv.mechjackbot.command.core;

import static com.mechjacktv.mechjackbot.command.AbstractCommand.MESSAGE_FORMAT_KEY;
import static com.mechjacktv.mechjackbot.command.core.QuitCommand.MESSAGE_FORMAT_DEFAULT;
import static com.mechjacktv.mechjackbot.command.core.QuitCommand.TRIGGER_DEFAULT;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.configuration.SettingKey;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.AbstractCommand;
import com.mechjacktv.mechjackbot.command.ArbitraryCommandTestUtils;
import com.mechjacktv.mechjackbot.command.DefaultCommandConfigurationBuilder;
import com.mechjacktv.twitchclient.TwitchLogin;
import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.scheduleservice.ScheduleService;

public class QuitCommandUnitTests extends CommandContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private final ArbitraryCommandTestUtils commandTestUtils = new ArbitraryCommandTestUtils(this.arbitraryDataGenerator);

  @Override
  protected Command givenASubjectToTest(final Configuration configuration) {
    return this.givenASubjectToTest(configuration, mock(ScheduleService.class));
  }

  private Command givenASubjectToTest(final String message) {
    return this.givenASubjectToTest(message, mock(ScheduleService.class));
  }

  private Command givenASubjectToTest(final String message, final ScheduleService scheduleService) {
    return this.givenASubjectToTest(this.givenAnAppConfiguration(message), scheduleService);
  }

  private Command givenASubjectToTest(final Configuration configuration, final ScheduleService scheduleService) {
    final DefaultCommandConfigurationBuilder builder = new DefaultCommandConfigurationBuilder(
        this.commandTestUtils.givenACommandUtils(configuration),
        configuration);

    return new QuitCommand(builder, scheduleService);
  }

  @Override
  protected SettingKey getCommandTriggerKey() {
    return SettingKey.of(QuitCommand.class, AbstractCommand.TRIGGER_KEY);
  }

  @Override
  protected CommandTrigger getCommandTriggerDefault() {
    return CommandTrigger.of(TRIGGER_DEFAULT);
  }

  private MapConfiguration givenAnAppConfiguration(final String message) {
    final MapConfiguration appConfiguration = this.givenAnAppConfiguration();

    appConfiguration.set(SettingKey.of(QuitCommand.class, MESSAGE_FORMAT_KEY).value, message);
    return appConfiguration;
  }

  private MessageEvent givenAFakeMessageEvent() {
    return this.givenAFakeMessageEvent(mock(ChatBot.class));
  }

  private MessageEvent givenAFakeMessageEvent(final ChatBot chatBot) {
    final MessageEvent messageEvent = mock(MessageEvent.class);
    final ChatUser chatUser = mock(ChatUser.class);
    final TwitchLogin twitchLogin = TwitchLogin.of(this.arbitraryDataGenerator.getString());

    when(messageEvent.getChatBot()).thenReturn(chatBot);
    when(messageEvent.getChatUser()).thenReturn(chatUser);
    when(chatUser.getTwitchLogin()).thenReturn(twitchLogin);
    return messageEvent;
  }

  @Test
  public final void handleMessageEvent_defaultFormat_sendsDefaultMessage() {
    final String message = MESSAGE_FORMAT_DEFAULT;
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    final Command subjectUnderTest = this.givenASubjectToTest(message);

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(messageEvent).sendResponse(eq(Message.of(message)));
  }

  @Test
  public final void handleMessageEvent_customFormat_sendsCustomMessage() {
    final String message = this.arbitraryDataGenerator.getString();
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    final Command subjectUnderTest = this.givenASubjectToTest(message);

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(messageEvent).sendResponse(eq(Message.of(message)));
  }

  @Test
  public final void handleMessageEvent_whenCalled_stopsChatBot() {
    final ChatBot chatBot = mock(ChatBot.class);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent(chatBot);
    final Command subjectUnderTest = this.givenASubjectToTest(MESSAGE_FORMAT_DEFAULT);

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(chatBot).stop();
  }

  @Test
  public final void handleMessageEvent_whenCalled_stopsScheduleService() {
    final ScheduleService scheduleService = mock(ScheduleService.class);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    final Command subjectUnderTest = this.givenASubjectToTest(MESSAGE_FORMAT_DEFAULT, scheduleService);

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(scheduleService).stop();
  }

}