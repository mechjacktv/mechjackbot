package com.mechjacktv.mechjackbot.command;

import org.junit.Test;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.test.ArbitraryDataGenerator;
import com.mechjacktv.util.scheduleservice.ScheduleService;

import static com.mechjacktv.mechjackbot.command.QuitCommand.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public class QuitCommandUnitTests extends CommandContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private final AbstractCommandTestUtils commandTestUtils = new AbstractCommandTestUtils(this.arbitraryDataGenerator);

  @Override
  protected Command givenASubjectToTest(final AppConfiguration appConfiguration) {
    return this.givenASubjectToTest(appConfiguration, mock(ScheduleService.class));
  }

  private Command givenASubjectToTest(final AppConfiguration appConfiguration, final ScheduleService scheduleService) {
    return new QuitCommand(appConfiguration, this.commandTestUtils.givenACommandUtils(appConfiguration),
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

  private AppConfiguration givenAFakeAppConfiguration(final String messageFormat) {
    final AppConfiguration appConfiguration = this.givenAFakeAppConfiguration();

    when(appConfiguration.get(eq(COMMAND_MESSAGE_KEY), isA(String.class))).thenReturn(messageFormat);
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
  public final void handleMessageEvent_defaultFormat_returnsDefaultMessage() {
    final String message = COMMAND_MESSAGE_DEFAULT;
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    final Command subjectUnderTest = this.givenASubjectToTest(this.givenAFakeAppConfiguration(message));

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(messageEvent).sendResponse(eq(Message.of(message)));
  }

  @Test
  public final void handleMessageEvent_customFormat_returnsCustomMessage() {
    final String message = this.arbitraryDataGenerator.getString();
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    final Command subjectUnderTest = this.givenASubjectToTest(this.givenAFakeAppConfiguration(message));

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(messageEvent).sendResponse(eq(Message.of(message)));
  }

  @Test
  public final void handleMessageEvent_whenCalled_stopsChatBot() {
    final ChatBot chatBot = mock(ChatBot.class);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent(chatBot);
    final Command subjectUnderTest = this.givenASubjectToTest(this.givenAFakeAppConfiguration(COMMAND_MESSAGE_DEFAULT));

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(chatBot).stop();
  }

  @Test
  public final void handleMessageEvent_whenCalled_stopsScheduleService() {
    final ScheduleService scheduleService = mock(ScheduleService.class);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent();
    final Command subjectUnderTest = this.givenASubjectToTest(this.givenAFakeAppConfiguration(COMMAND_MESSAGE_DEFAULT),
        scheduleService);

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(scheduleService).stop();
  }

}
