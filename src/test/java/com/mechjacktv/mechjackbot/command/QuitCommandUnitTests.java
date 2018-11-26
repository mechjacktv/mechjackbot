package com.mechjacktv.mechjackbot.command;

import static com.mechjacktv.mechjackbot.command.QuitCommand.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.mechjackbot.*;
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
    return new QuitCommand(configuration, this.commandTestUtils.givenACommandUtils(configuration),
        scheduleService);
  }

  @Override
  protected CommandTriggerKey getCommandTriggerKey() {
    return CommandTriggerKey.of(COMMAND_TRIGGER_KEY);
  }

  @Override
  protected CommandTrigger getCommandTriggerDefault() {
    return CommandTrigger.of(COMMAND_TRIGGER_DEFAULT);
  }

  private MapConfiguration givenAnAppConfiguration(final String message) {
    final MapConfiguration appConfiguration = this.givenAnAppConfiguration();

    appConfiguration.set(COMMAND_MESSAGE_KEY, message);
    return appConfiguration;
  }

  private MessageEvent givenAFakeMessageEvent() {
    return this.givenAFakeMessageEvent(mock(ChatBot.class));
  }

  private MessageEvent givenAFakeMessageEvent(final ChatBot chatBot) {
    final MessageEvent messageEvent = mock(MessageEvent.class);

    when(messageEvent.getChatBot()).thenReturn(chatBot);
    return messageEvent;
  }

  @Test
  public final void handleMessageEvent_defaultFormat_sendsDefaultMessage() {
    final String message = COMMAND_MESSAGE_DEFAULT;
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
    final Command subjectUnderTest = this.givenASubjectToTest(COMMAND_MESSAGE_DEFAULT);

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(chatBot).stop();
  }

  @Test
  public final void handleMessageEvent_whenCalled_stopsScheduleService() {
    final ScheduleService scheduleService = mock(ScheduleService.class);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    final Command subjectUnderTest = this.givenASubjectToTest(COMMAND_MESSAGE_DEFAULT, scheduleService);

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(scheduleService).stop();
  }

}
