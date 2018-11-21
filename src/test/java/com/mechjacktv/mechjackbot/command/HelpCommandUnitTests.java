package com.mechjacktv.mechjackbot.command;

import static com.mechjacktv.mechjackbot.command.HelpCommand.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.chatbot.DefaultCommandRegistry;
import com.mechjacktv.mechjackbot.configuration.ArbitraryChatBotConfiguration;
import com.mechjacktv.mechjackbot.configuration.MapAppConfiguration;
import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultTimeUtils;

public class HelpCommandUnitTests extends CommandContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private final ArbitraryCommandTestUtils commandTestUtils = new ArbitraryCommandTestUtils(this.arbitraryDataGenerator);

  @Override
  protected Command givenASubjectToTest(final AppConfiguration appConfiguration) {
    return this.givenASubjectToTest(appConfiguration, this.commandTestUtils.givenACommandUtils(appConfiguration),
        mock(CommandRegistry.class));
  }

  private Command givenASubjectToTest(final CommandUtils commandUtils) {
    return this.givenASubjectToTest(this.givenAnAppConfiguration(), commandUtils);
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

  private MapAppConfiguration givenAnAppConfiguration(final String messageFormat) {
    final MapAppConfiguration appConfiguration = this.givenAnAppConfiguration();

    appConfiguration.set(COMMAND_MESSAGE_FORMAT_KEY, messageFormat);
    appConfiguration.set(COMMAND_MISSING_MESSAGE_FORMAT_KEY, messageFormat);
    return appConfiguration;
  }

  private Command givenAFakeCommand(final CommandTrigger commandTrigger, final boolean triggerable) {
    final Command command = mock(Command.class);

    when(command.isTriggerable()).thenReturn(triggerable);
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

    when(commandUtils.messageWithoutTrigger(isA(Command.class), isA(MessageEvent.class)))
        .thenReturn(Message.of(commandTrigger.value));
    when(commandUtils.sanitizeChatUsername(isA(ChatUsername.class))).thenReturn(chatUsername);
    return commandUtils;
  }

  private MessageEvent givenAFakeMessageEvent(final Command command, final CommandTrigger commandTrigger) {
    return this.givenAFakeMessageEvent(command, commandTrigger, ArgumentCaptor.forClass(Message.class));
  }

  private MessageEvent givenAFakeMessageEvent(final Command command, final CommandTrigger commandTrigger,
      final ArgumentCaptor<Message> argumentCaptor) {
    final MessageEvent messageEvent = mock(MessageEvent.class);
    final ChatUser chatUser = mock(ChatUser.class);
    final Message message = Message.of(String.format("%s %s", command.getTrigger(), commandTrigger));

    when(messageEvent.getMessage()).thenReturn(message);
    when(messageEvent.getChatUser()).thenReturn(chatUser);
    doNothing().when(messageEvent).sendResponse(argumentCaptor.capture());
    when(chatUser.getUsername()).thenReturn(ChatUsername.of(this.arbitraryDataGenerator.getString()));
    return messageEvent;
  }

  @Test
  public final void handleMessageEvent_notProperlyFormatted_sendsUsageMessage() {
    final String messageArguments = "";
    final CommandUtils commandUtils = mock(CommandUtils.class);
    final Command subjectUnderTest = this.givenASubjectToTest(commandUtils);
    final MessageEvent messageEvent = this.givenAFakeMessageEvent(subjectUnderTest,
        CommandTrigger.of(messageArguments));
    when(commandUtils.messageWithoutTrigger(isA(Command.class), isA(MessageEvent.class)))
        .thenReturn(Message.of(messageArguments));

    subjectUnderTest.handleMessageEvent(messageEvent);

    verify(commandUtils).sendUsage(eq(subjectUnderTest), eq(messageEvent));
  }

  @Test
  public final void handleMessageEvent_missingCommandAndDefaultFormat_sendsDefaultMissingCommandMessage() {
    final AppConfiguration appConfiguration = this.givenAnAppConfiguration(COMMAND_MISSING_MESSAGE_FORMAT_DEFAULT);
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
    final AppConfiguration appConfiguration = this.givenAnAppConfiguration(customFormat);
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
    final AppConfiguration appConfiguration = this.givenAnAppConfiguration(COMMAND_MESSAGE_FORMAT_DEFAULT);
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
    final AppConfiguration appConfiguration = this.givenAnAppConfiguration(COMMAND_MISSING_MESSAGE_FORMAT_DEFAULT);
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
    final MapAppConfiguration appConfiguration = this.givenAnAppConfiguration();
    final CommandUtils commandUtils = new DefaultCommandUtils(appConfiguration,
        new ArbitraryChatBotConfiguration(this.arbitraryDataGenerator), this.executionUtils, new DefaultTimeUtils());
    final CommandRegistry commandRegistry = new DefaultCommandRegistry(this.executionUtils);
    final Command command = new ArbitraryCommand(appConfiguration, commandUtils, this.arbitraryDataGenerator);
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, commandUtils, commandRegistry);
    appConfiguration.set(COMMAND_MESSAGE_FORMAT_KEY, customFormat);
    commandRegistry.addCommand(command);
    messageEvent.setMessage(Message.of(String.format("%s %s", subjectUnderTest.getTrigger(), command.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);

    assertThat(messageEvent.getResponseMessage().value).isEqualTo(String.format(customFormat,
        commandUtils.sanitizeChatUsername(messageEvent.getChatUser().getUsername()), command.getTrigger(),
        command.getDescription()));
  }

}
