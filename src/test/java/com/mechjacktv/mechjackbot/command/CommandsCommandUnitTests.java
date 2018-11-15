package com.mechjacktv.mechjackbot.command;

import static com.mechjacktv.mechjackbot.command.CommandsCommand.COMMAND_MESSAGE_FORMAT_DEFAULT;
import static com.mechjacktv.mechjackbot.command.CommandsCommand.COMMAND_MESSAGE_FORMAT_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.test.ArbitraryDataGenerator;

public class CommandsCommandUnitTests extends CommandContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private final AbstractCommandTestUtils commandTestUtils = new AbstractCommandTestUtils(this.arbitraryDataGenerator);

  @Override
  protected Command givenASubjectToTest(final AppConfiguration appConfiguration) {
    return this.givenASubjectToTest(appConfiguration, mock(CommandRegistry.class));
  }

  private Command givenASubjectToTest(final String messageFormat, final CommandRegistry commandRegistry) {
    return this.givenASubjectToTest(this.givenAFakeAppConfiguration(messageFormat), commandRegistry);
  }

  private Command givenASubjectToTest(final AppConfiguration appConfiguration, final CommandRegistry commandRegistry) {
    return new CommandsCommand(appConfiguration, this.commandTestUtils.givenACommandUtils(appConfiguration),
        commandRegistry);
  }

  @Override
  protected CommandTriggerKey getCommandTriggerKey() {
    return CommandTriggerKey.of(CommandsCommand.COMMAND_TRIGGER_KEY);
  }

  @Override
  protected CommandTrigger getCommandTriggerDefault() {
    return CommandTrigger.of(CommandsCommand.COMMAND_TRIGGER_DEFAULT);
  }

  private AppConfiguration givenAFakeAppConfiguration(final String messageFormat) {
    final AppConfiguration appConfiguration = this.givenAFakeAppConfiguration();

    when(appConfiguration.get(eq(COMMAND_MESSAGE_FORMAT_KEY), isA(String.class))).thenReturn(messageFormat);
    return appConfiguration;
  }

  private CommandRegistry givenAFakeCommandRegistry(final Set<Command> commands) {
    final CommandRegistry commandRegistry = mock(CommandRegistry.class);

    when(commandRegistry.getCommands()).thenReturn(commands);
    return commandRegistry;
  }

  private Set<Command> givenASetOfFakeCommands() {
    final Set<Command> commands = new HashSet<>();

    for (int i = 0; i < 3; i++) {
      commands.add(this.givenAFakeCommand());
    }
    return commands;
  }

  private Command givenAFakeCommand() {
    final Command command = mock(Command.class);

    when(command.getTrigger()).thenReturn(CommandTrigger.of(this.arbitraryDataGenerator.getString()));
    when(command.isViewerTriggerable()).thenReturn(true);
    return command;
  }

  @Test
  public final void handleMessageEvent_defaultFormat_sendsDefaultListOfCommands() {
    final Set<Command> commands = this.givenASetOfFakeCommands();
    final MessageEvent messageEvent = mock(MessageEvent.class);
    final ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
    doNothing().when(messageEvent).sendResponse(messageArgumentCaptor.capture());
    final Command subjectUnderTest = this.givenASubjectToTest(COMMAND_MESSAGE_FORMAT_DEFAULT,
        this.givenAFakeCommandRegistry(commands));

    subjectUnderTest.handleMessageEvent(messageEvent);

    final SoftAssertions softly = new SoftAssertions();
    final Message message = messageArgumentCaptor.getValue();
    softly.assertThat(message.value).contains(this.stripFormat(COMMAND_MESSAGE_FORMAT_DEFAULT));
    for (final Command command : commands) {
      softly.assertThat(message.value).contains(command.getTrigger().value);
    }
    softly.assertAll();
  }

  @Test
  public final void handleMessageEvent_customFormat_sendsCustomListOfCommands() {
    final Set<Command> commands = this.givenASetOfFakeCommands();
    final String messageFormat = this.arbitraryDataGenerator.getString() + ": %s";
    final MessageEvent messageEvent = mock(MessageEvent.class);
    final ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
    doNothing().when(messageEvent).sendResponse(messageArgumentCaptor.capture());
    final Command subjectUnderTest = this.givenASubjectToTest(messageFormat, this.givenAFakeCommandRegistry(commands));

    subjectUnderTest.handleMessageEvent(messageEvent);

    final SoftAssertions softly = new SoftAssertions();
    final Message message = messageArgumentCaptor.getValue();
    softly.assertThat(message.value).contains(this.stripFormat(messageFormat));
    for (final Command command : commands) {
      softly.assertThat(message.value).contains(command.getTrigger().value);
    }
    softly.assertAll();
  }

  private String stripFormat(final String messageFormat) {
    return messageFormat.replace("%s", "").trim();
  }

  @Test
  public final void handleMessageEvent_withNonTriggerableCommands_doesNotListNonTriggerableCommands() {
    final Command nonTriggerableCommand = this.givenAFakeCommand();
    when(nonTriggerableCommand.isViewerTriggerable()).thenReturn(false);
    final Set<Command> commands = Sets.newHashSet(nonTriggerableCommand, this.givenAFakeCommand(),
        this.givenAFakeCommand());
    final MessageEvent messageEvent = mock(MessageEvent.class);
    final ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
    doNothing().when(messageEvent).sendResponse(messageArgumentCaptor.capture());
    final Command subjectUnderTest = this.givenASubjectToTest(COMMAND_MESSAGE_FORMAT_DEFAULT,
        this.givenAFakeCommandRegistry(commands));

    subjectUnderTest.handleMessageEvent(messageEvent);

    assertThat(messageArgumentCaptor.getValue().value).doesNotContain(nonTriggerableCommand.getTrigger().value);
  }

}
