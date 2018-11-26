package com.mechjacktv.mechjackbot;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

import org.junit.Test;

import com.mechjacktv.util.ArbitraryDataGenerator;

public abstract class MessageEventHandlerContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  protected abstract MessageEventHandler givenASubjectToTest(final Set<Command> commands);

  private final Set<Command> givenASetOfCommands(final boolean triggerable) {
    final Set<Command> commands = new HashSet<>();

    for (int i = 0; i < 3; i++) {
      commands.add(this.givenAFakeCommand(triggerable));
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

  protected final MessageEvent givenAFakeMessageEvent() {
    final MessageEvent messageEvent = mock(MessageEvent.class);
    final ChatUser chatUser = mock(ChatUser.class);

    when(messageEvent.getChatUser()).thenReturn(chatUser);
    when(messageEvent.getMessage()).thenReturn(Message.of(this.arbitraryDataGenerator.getString()));
    when(chatUser.getUsername()).thenReturn(ChatUsername.of(this.arbitraryDataGenerator.getString()));
    return messageEvent;
  }

  @Test
  public final void handleMessageEvent_noCommandIsTriggered_noCommandHandlesMessageEvent() {
    final Command command = this.givenAFakeCommand(false);
    final MessageEventHandler subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command));

    subjectUnderTest.handleMessageEvent(this.givenAFakeMessageEvent());

    verify(command, never()).handleMessageEvent(isA(MessageEvent.class));
  }

  @Test
  public final void handleMessageEvent_commandIsTriggered_commandHandlesMessageEvent() {
    final Command command = this.givenAFakeCommand(true);
    final MessageEventHandler subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command));

    subjectUnderTest.handleMessageEvent(this.givenAFakeMessageEvent());

    verify(command).handleMessageEvent(isA(MessageEvent.class));
  }

  @Test
  public final void handleMessageEvent_forMessageEvent_allCommandsAreCheckedForTrigger() {
    final Set<Command> commands = this.givenASetOfCommands(true);
    final MessageEventHandler subjectUnderTest = this.givenASubjectToTest(commands);

    subjectUnderTest.handleMessageEvent(this.givenAFakeMessageEvent());

    for (final Command command : commands) {
      verify(command).isTriggered(isA(MessageEvent.class));
    }
  }

}
