package com.mechjacktv.mechjackbot.command;

import java.util.Optional;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.test.ArbitraryDataGenerator;

import static com.mechjacktv.mechjackbot.command.HelpCommand.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public class HelpCommandUnitTests extends CommandContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private final AbstractCommandTestUtils commandTestUtils = new AbstractCommandTestUtils(this.arbitraryDataGenerator);

  @Override
  protected Command givenASubjectToTest(final AppConfiguration appConfiguration) {
    return this.givenASubjectToTest(appConfiguration, this.commandTestUtils.givenACommandUtils(appConfiguration),
        mock(CommandRegistry.class));
  }

  private Command givenASubjectToTest(final CommandUtils commandUtils) {
    return this.givenASubjectToTest(this.givenAFakeAppConfiguration(), commandUtils);
  }

  private Command givenASubjectToTest(final AppConfiguration appConfiguration, final CommandUtils commandUtils) {
    return this.givenASubjectToTest(appConfiguration, commandUtils, mock(CommandRegistry.class));
  }

  private Command givenASubjectToTest(final AppConfiguration appConfiguration,
      final CommandUtils commandUtils, final CommandRegistry commandRegistry) {
    return new HelpCommand(appConfiguration, commandUtils, commandRegistry);
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

    when(appConfiguration.get(eq(COMMAND_MESSAGE_FORMAT_KEY), eq(COMMAND_MESSAGE_FORMAT_DEFAULT)))
        .thenReturn(messageFormat);
    when(appConfiguration.get(eq(COMMAND_MISSING_MESSAGE_FORMAT_KEY), eq(COMMAND_MISSING_MESSAGE_FORMAT_DEFAULT)))
        .thenReturn(messageFormat);
    return appConfiguration;
  }

  private Command givenAFakeCommand(final CommandTrigger commandTrigger, final boolean triggerable) {
    final Command command = mock(Command.class);

    when(command.isViewerTriggerable()).thenReturn(triggerable);
    when(command.getTrigger()).thenReturn(commandTrigger);
    when(command.getDescription()).thenReturn(CommandDescription.of(this.arbitraryDataGenerator.getString()));
    return command;
  }

  private CommandRegistry givenAFakeCommandRegistry(final Command command) {
    final CommandRegistry commandRegistry = mock(CommandRegistry.class);

    when(commandRegistry.getCommand(isA(CommandTrigger.class))).thenReturn(Optional.empty());
    when(commandRegistry.getCommand(eq(command.getTrigger()))).thenReturn(Optional.of(command));
    return commandRegistry;
  }

  private CommandUtils givenAFakeCommandUtils(final ChatUsername chatUsername, final CommandTrigger commandTrigger) {
    final CommandUtils commandUtils = mock(CommandUtils.class);

    when(commandUtils.stripTriggerOffMessage(isA(CommandTrigger.class), isA(Message.class)))
        .thenReturn(commandTrigger.value);
    when(commandUtils.getSanitizedViewerName(isA(MessageEvent.class))).thenReturn(chatUsername);
    return commandUtils;
  }

  private MessageEvent givenAFakeMessageEvent(final Command command, final CommandTrigger commandTrigger) {
    return this.givenAFakeMessageEvent(command, commandTrigger, ArgumentCaptor.forClass(Message.class));
  }

  private MessageEvent givenAFakeMessageEvent(final Command command, final CommandTrigger commandTrigger,
      final ArgumentCaptor<Message> argumentCaptor) {
    final MessageEvent messageEvent = mock(MessageEvent.class);
    final Message message = Message.of(String.format("%s %s", command.getTrigger(), commandTrigger));

    when(messageEvent.getMessage()).thenReturn(message);
    doNothing().when(messageEvent).sendResponse(argumentCaptor.capture());
    return messageEvent;
  }

  @Test
  public final void handleMessageEvent_notProperlyFormatted_sendsUsageMessage() {
    final CommandUtils commandUtils = mock(CommandUtils.class);
    final Command subjectUnderTest = this.givenASubjectToTest(commandUtils);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent(subjectUnderTest, CommandTrigger.of(""));

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(commandUtils).sendUsage(eq(messageEvent), isA(CommandUsage.class));
  }

  @Test
  public final void handleMessageEvent_missingCommandAndDefaultFormat_sendsDefaultMissingCommandMessage() {
    final AppConfiguration appConfiguration = this.givenAFakeAppConfiguration(COMMAND_MISSING_MESSAGE_FORMAT_DEFAULT);
    final ChatUsername chatUsername = ChatUsername.of(this.arbitraryDataGenerator.getString());
    final CommandTrigger commandTrigger = CommandTrigger.of(this.arbitraryDataGenerator.getString());
    final CommandUtils commandUtils = this.givenAFakeCommandUtils(chatUsername, commandTrigger);
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, commandUtils);
    final ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent(subjectUnderTest, commandTrigger, argumentCaptor);

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = argumentCaptor.getValue();

    assertThat(result.value).isEqualTo(String.format(COMMAND_MISSING_MESSAGE_FORMAT_DEFAULT,
        chatUsername, commandTrigger));
  }

  @Test
  public final void handleMessageEvent_missingCommandAndCustomFormat_sendsCustomMissingCommandMessage() {
    final String customFormat = this.arbitraryDataGenerator.getString() + " %s %s";
    final AppConfiguration appConfiguration = this.givenAFakeAppConfiguration(customFormat);
    final ChatUsername chatUsername = ChatUsername.of(this.arbitraryDataGenerator.getString());
    final CommandTrigger commandTrigger = CommandTrigger.of(this.arbitraryDataGenerator.getString());
    final CommandUtils commandUtils = this.givenAFakeCommandUtils(chatUsername, commandTrigger);
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, commandUtils);
    final ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent(subjectUnderTest, commandTrigger, argumentCaptor);

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = argumentCaptor.getValue();

    assertThat(result.value).isEqualTo(String.format(customFormat, chatUsername, commandTrigger));
  }

  @Test
  public final void handleMessageEvent_forTriggerableCommandAndDefaultFormat_sendsDefaultMessage() {
    final AppConfiguration appConfiguration = this.givenAFakeAppConfiguration(COMMAND_MESSAGE_FORMAT_DEFAULT);
    final ChatUsername chatUsername = ChatUsername.of(this.arbitraryDataGenerator.getString());
    final CommandTrigger commandTrigger = CommandTrigger.of(this.arbitraryDataGenerator.getString());
    final CommandUtils commandUtils = this.givenAFakeCommandUtils(chatUsername, commandTrigger);
    final Command command = this.givenAFakeCommand(commandTrigger, true);
    final CommandRegistry commandRegistry = this.givenAFakeCommandRegistry(command);
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, commandUtils, commandRegistry);
    final ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent(subjectUnderTest, commandTrigger, argumentCaptor);

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = argumentCaptor.getValue();

    assertThat(result.value).isEqualTo(String.format(COMMAND_MESSAGE_FORMAT_DEFAULT,
        chatUsername, commandTrigger, command.getDescription()));
  }

  @Test
  public final void handleMessageEvent_forNonTriggerableCommand_sendsMissingMessage() {
    final AppConfiguration appConfiguration = this.givenAFakeAppConfiguration(COMMAND_MISSING_MESSAGE_FORMAT_DEFAULT);
    final ChatUsername chatUsername = ChatUsername.of(this.arbitraryDataGenerator.getString());
    final CommandTrigger commandTrigger = CommandTrigger.of(this.arbitraryDataGenerator.getString());
    final CommandUtils commandUtils = this.givenAFakeCommandUtils(chatUsername, commandTrigger);
    final Command command = this.givenAFakeCommand(commandTrigger, false);
    final CommandRegistry commandRegistry = this.givenAFakeCommandRegistry(command);
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, commandUtils, commandRegistry);
    final ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent(subjectUnderTest, commandTrigger, argumentCaptor);

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = argumentCaptor.getValue();

    assertThat(result.value).isEqualTo(String.format(COMMAND_MISSING_MESSAGE_FORMAT_DEFAULT,
        chatUsername, commandTrigger));
  }

  @Test
  public final void handleMessageEvent_forTriggerableCommandAndCustomFormat_sendsCustomMessage() {
    final String customFormat = this.arbitraryDataGenerator.getString() + " %s %s %s";
    final AppConfiguration appConfiguration = this.givenAFakeAppConfiguration(customFormat);
    final ChatUsername chatUsername = ChatUsername.of(this.arbitraryDataGenerator.getString());
    final CommandTrigger commandTrigger = CommandTrigger.of(this.arbitraryDataGenerator.getString());
    final CommandUtils commandUtils = this.givenAFakeCommandUtils(chatUsername, commandTrigger);
    final Command command = this.givenAFakeCommand(commandTrigger, true);
    final CommandRegistry commandRegistry = this.givenAFakeCommandRegistry(command);
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, commandUtils, commandRegistry);
    final ArgumentCaptor<Message> argumentCaptor = ArgumentCaptor.forClass(Message.class);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent(subjectUnderTest, commandTrigger, argumentCaptor);

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = argumentCaptor.getValue();

    assertThat(result.value).isEqualTo(String.format(customFormat, chatUsername, commandTrigger,
        command.getDescription()));
  }

}
