package com.mechjacktv.mechjackbot.chatbot;

import java.util.Set;

import com.google.common.collect.Sets;

import org.junit.Test;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.ArbitraryDataGenerator;
import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.ExecutionUtils;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PircBotXListenerUnitTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();
  private final ExecutionUtils executionUtils = new DefaultExecutionUtils();

  private PircBotXListener givenASubjectToTest() {
    return this.givenASubjectToTest(Sets.newHashSet());
  }

  private PircBotXListener givenASubjectToTest(final Set<Command> commands) {
    return new PircBotXListener(commands, mock(AppConfiguration.class), this.executionUtils,
        new DefaultCommandRegistry(this.executionUtils));
  }

  private Command givenAFakeCommand() {
    final Command command = mock(Command.class);

    when(command.getTrigger()).thenReturn(CommandTrigger.of(this.arbitraryDataGenerator.getString()));
    return command;
  }

  @Test
  public final void onPing_isCalled_respondsWithExpectedPingValue() {
    final PircBotXListener subjectUnderTest = this.givenASubjectToTest();
    final PingEvent pingEvent = mock(PingEvent.class);
    final String pingValue = this.arbitraryDataGenerator.getString();
    when(pingEvent.getPingValue()).thenReturn(pingValue);

    subjectUnderTest.onPing(pingEvent);

    verify(pingEvent).respond(eq(String.format("PONG %s", pingValue)));
  }

  @Test
  public final void onGenericMessage_noCommandIsTriggered_noCommandHandlesMessageEvent() {
    final Command command = this.givenAFakeCommand();
    when(command.isTriggered(isA(MessageEvent.class))).thenReturn(false);
    final PircBotXListener subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command));
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);

    subjectUnderTest.onGenericMessage(genericMessageEvent);

    verify(command, never()).handleMessageEvent(isA(MessageEvent.class));
  }

  @Test
  public final void onGenericMessage_commandIsTriggered_commandHandlesMessageEvent() {
    final Command command = this.givenAFakeCommand();
    when(command.isTriggered(isA(MessageEvent.class))).thenReturn(true);
    final PircBotXListener subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command));
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);

    subjectUnderTest.onGenericMessage(genericMessageEvent);

    verify(command).handleMessageEvent(isA(MessageEvent.class));
  }

  @Test
  public final void onGenericMessage_oneCommandIsTriggered_triggeredCommandHandlesMessageEvent() {
    final Command goodCommand = this.givenAFakeCommand();
    when(goodCommand.isTriggered(isA(MessageEvent.class))).thenReturn(true);
    final Command badCommand = this.givenAFakeCommand();
    when(badCommand.isTriggered(isA(MessageEvent.class))).thenReturn(false);
    final PircBotXListener subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(goodCommand, badCommand));
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);

    subjectUnderTest.onGenericMessage(genericMessageEvent);

    verify(goodCommand).handleMessageEvent(isA(MessageEvent.class));
    verify(badCommand, never()).handleMessageEvent(isA(MessageEvent.class));
  }

}
