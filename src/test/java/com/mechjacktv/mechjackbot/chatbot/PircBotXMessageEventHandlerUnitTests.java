package com.mechjacktv.mechjackbot.chatbot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.Optional;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import com.mechjacktv.ArbitraryDataGenerator;
import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.ExecutionUtils;

public class PircBotXMessageEventHandlerUnitTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();
  private final ExecutionUtils executionUtils = new DefaultExecutionUtils();

  private PircBotXMessageEventHandler givenASubjectToTest() {
    return new PircBotXMessageEventHandler(this.executionUtils);
  }

  private Command givenAFakeCommand() {
    return this.givenAFakeCommand(CommandTrigger.of(this.arbitraryDataGenerator.getString()));
  }

  private Command givenAFakeCommand(final CommandTrigger commandTrigger) {
    final Command command = mock(Command.class);

    when(command.getTrigger()).thenReturn(commandTrigger);
    return command;
  }

  @Test
  public final void addCommand_nullCommand_throwsNullPointerExceptionWithMessage() {
    final PircBotXMessageEventHandler subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.addCommand(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("command"));
  }

  @Test
  public final void addCommandAndGetCommands_forCommand_storesCommand() {
    final Command command = this.givenAFakeCommand();
    final PircBotXMessageEventHandler subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.addCommand(command);
    final Collection<Command> result = subjectUnderTest.getCommands();

    assertThat(result).containsOnly(command);
  }

  @Test
  public final void addCommandAndGetCommand_forCommand_mapsCommandToTrigger() {
    final CommandTrigger trigger = CommandTrigger.of(this.arbitraryDataGenerator.getString());
    final Command command = this.givenAFakeCommand(trigger);
    final PircBotXMessageEventHandler subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.addCommand(command);
    final Optional<Command> result = subjectUnderTest.getCommand(trigger);

    final SoftAssertions softly = new SoftAssertions();
    softly.assertThat(result).isNotEmpty();
    result.ifPresent((resultCommand) -> softly.assertThat(resultCommand).isEqualTo(command));
    softly.assertAll();
  }

  @Test
  public final void handleMessage_nullMessageEvent_throwsNullPointerExceptionWithMessage() {
    final PircBotXMessageEventHandler subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.handleMessageEvent(null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("messageEvent"));
  }

  @Test
  public final void handleMessage_noCommandIsTriggered_noCommandHandlesMessageEvent() {
    final PircBotXMessageEventHandler subjectUnderTest = this.givenASubjectToTest();
    final MessageEvent messageEvent = mock(MessageEvent.class);
    final Command command = this.givenAFakeCommand();
    when(command.isTriggered(eq(messageEvent))).thenReturn(false);
    subjectUnderTest.addCommand(command);

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(command, never()).handleMessageEvent(eq(messageEvent));
  }

  @Test
  public final void handleMessage_commandIsTriggered_commandHandlesMessageEvent() {
    final PircBotXMessageEventHandler subjectUnderTest = this.givenASubjectToTest();
    final MessageEvent messageEvent = mock(MessageEvent.class);
    final Command command = this.givenAFakeCommand();
    when(command.isTriggered(eq(messageEvent))).thenReturn(true);
    subjectUnderTest.addCommand(command);

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(command).handleMessageEvent(eq(messageEvent));
  }

  @Test
  public final void handleMessage_oneCommandIsTriggered_triggeredCommandHandlesMessageEvent() {
    final PircBotXMessageEventHandler subjectUnderTest = this.givenASubjectToTest();
    final MessageEvent messageEvent = mock(MessageEvent.class);
    final Command goodCommand = this.givenAFakeCommand();
    when(goodCommand.isTriggered(eq(messageEvent))).thenReturn(true);
    subjectUnderTest.addCommand(goodCommand);
    final Command badCommand = this.givenAFakeCommand();
    when(badCommand.isTriggered(eq(messageEvent))).thenReturn(false);
    subjectUnderTest.addCommand(badCommand);

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(goodCommand).handleMessageEvent(eq(messageEvent));
    verify(badCommand, never()).handleMessageEvent(eq(messageEvent));
  }

}
