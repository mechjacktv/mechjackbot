package com.mechjacktv.mechjackbot;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

import org.junit.Test;

import com.mechjacktv.testframework.ArbitraryDataGenerator;

public abstract class MessageEventHandlerContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  protected abstract MessageEventHandler givenASubjectToTest(final Set<Command> commands);

  private Set<Command> givenASetOfCommands() {
    final Set<Command> commands = new HashSet<>();

    for (int i = 0; i < 3; i++) {
      commands.add(this.givenAFakeCommand(true));
    }
    return commands;
  }

  protected final Command givenAFakeCommand(final boolean triggerable) {
    final Command command = mock(Command.class);

    when(command.getName()).thenReturn(CommandName.of(this.arbitraryDataGenerator.getString()));
    when(command.getTrigger()).thenReturn(CommandTrigger.of(this.arbitraryDataGenerator.getString()));
    when(command.isTriggered(isA(MessageEvent.class))).thenReturn(triggerable);
    return command;
  }

  protected final MessageEvent givenAMessageEvent() {
    return new TestMessageEvent(this.arbitraryDataGenerator);
  }

  @Test
  public final void handleMessageEvent_noCommandIsTriggered_noCommandHandlesMessageEvent() {
    final Command command = this.givenAFakeCommand(false);
    final MessageEventHandler subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command));

    subjectUnderTest.handleMessageEvent(this.givenAMessageEvent());

    verify(command, never()).handleMessageEvent(isA(MessageEvent.class));
  }

  @Test
  public final void handleMessageEvent_commandIsTriggered_commandHandlesMessageEvent() {
    final Command command = this.givenAFakeCommand(true);
    final MessageEventHandler subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command));

    subjectUnderTest.handleMessageEvent(this.givenAMessageEvent());

    verify(command).handleMessageEvent(isA(MessageEvent.class));
  }

  @Test
  public final void handleMessageEvent_forMessageEvent_allCommandsAreCheckedForTrigger() {
    final Set<Command> commands = this.givenASetOfCommands();
    final MessageEventHandler subjectUnderTest = this.givenASubjectToTest(commands);

    subjectUnderTest.handleMessageEvent(this.givenAMessageEvent());

    for (final Command command : commands) {
      verify(command).isTriggered(isA(MessageEvent.class));
    }
  }

}
