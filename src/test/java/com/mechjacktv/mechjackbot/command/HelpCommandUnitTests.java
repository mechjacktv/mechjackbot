package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultTimeUtils;

import org.junit.Test;

import static com.mechjacktv.mechjackbot.CommandUtils.COMMAND_USAGE_MESSAGE_FORMAT_DEFAULT;
import static com.mechjacktv.mechjackbot.command.HelpCommand.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class HelpCommandUnitTests extends CommandContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private final ArbitraryCommandTestUtils commandTestUtils = new ArbitraryCommandTestUtils(this.arbitraryDataGenerator);

  @Override
  protected Command givenASubjectToTest(final Configuration configuration) {
    return this.givenASubjectToTest(configuration, this.commandTestUtils.givenACommandUtils(configuration),
        mock(CommandRegistry.class));
  }

  private Command givenASubjectToTest(final Configuration configuration, final CommandUtils commandUtils,
      final CommandRegistry commandRegistry) {
    return new HelpCommand(configuration, commandUtils, commandRegistry);
  }

  @Override
  protected CommandTriggerKey getCommandTriggerKey() {
    return CommandTriggerKey.of(COMMAND_TRIGGER_KEY);
  }

  @Override
  protected CommandTrigger getCommandTriggerDefault() {
    return CommandTrigger.of(COMMAND_TRIGGER_DEFAULT);
  }

  private CommandRegistry givenACommandRegistry(final Command command) {
    final CommandRegistry commandRegistry = new DefaultCommandRegistry(this.executionUtils);

    commandRegistry.addCommand(command);
    return commandRegistry;
  }

  private CommandUtils givenACommandUtils(final Configuration configuration) {
    return new DefaultCommandUtils(configuration, this.executionUtils, new DefaultTimeUtils());
  }

  @Test
  public final void handleMessageEvent_notProperlyFormatted_sendsUsageMessage() {
    final MapConfiguration configuration = this.givenAnAppConfiguration();
    final CommandUtils commandUtils = this.givenACommandUtils(configuration);
    final Command command = new ArbitraryCommand(configuration, commandUtils, this.arbitraryDataGenerator);
    final CommandRegistry commandRegistry = this.givenACommandRegistry(command);
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest(configuration, commandUtils, commandRegistry);
    messageEvent.setMessage(Message.of(subjectUnderTest.getTrigger().value));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result.value).isEqualTo(String.format(COMMAND_USAGE_MESSAGE_FORMAT_DEFAULT,
        messageEvent.getChatUser().getTwitchLogin(), subjectUnderTest.getTrigger(), COMMAND_USAGE));
  }

  @Test
  public final void handleMessageEvent_forTriggerableCommandAndDefaultFormat_sendsDefaultMessage() {
    final MapConfiguration configuration = this.givenAnAppConfiguration();
    final CommandUtils commandUtils = this.givenACommandUtils(configuration);
    final Command command = new ArbitraryCommand(configuration, commandUtils, this.arbitraryDataGenerator);
    final CommandRegistry commandRegistry = this.givenACommandRegistry(command);
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest(configuration, commandUtils, commandRegistry);
    messageEvent.setMessage(Message.of(String.format("%s %s", subjectUnderTest.getTrigger(), command.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result.value).isEqualTo(String.format(COMMAND_MESSAGE_FORMAT_DEFAULT,
        messageEvent.getChatUser().getTwitchLogin(), command.getTrigger(), command.getDescription()));
  }

  @Test
  public final void handleMessageEvent_forTriggerableCommandAndCustomFormat_sendsCustomMessage() {
    final String customFormat = this.arbitraryDataGenerator.getString() + " %s %s %s";
    final MapConfiguration configuration = this.givenAnAppConfiguration();
    configuration.set(COMMAND_MESSAGE_FORMAT_KEY, customFormat);
    final CommandUtils commandUtils = this.givenACommandUtils(configuration);
    final Command command = new ArbitraryCommand(configuration, commandUtils, this.arbitraryDataGenerator);
    final CommandRegistry commandRegistry = this.givenACommandRegistry(command);
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest(configuration, commandUtils, commandRegistry);
    messageEvent.setMessage(Message.of(String.format("%s %s", subjectUnderTest.getTrigger(), command.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result.value).isEqualTo(String.format(customFormat,
        messageEvent.getChatUser().getTwitchLogin(), command.getTrigger(), command.getDescription()));
  }

  @Test
  public final void handleMessageEvent_missingCommandAndDefaultFormat_sendsDefaultMissingMessage() {
    final MapConfiguration configuration = this.givenAnAppConfiguration();
    final CommandUtils commandUtils = this.givenACommandUtils(configuration);
    final Command command = new ArbitraryCommand(configuration, commandUtils, this.arbitraryDataGenerator);
    final CommandRegistry commandRegistry = this.givenACommandRegistry(
        new ArbitraryCommand(configuration, commandUtils, this.arbitraryDataGenerator));
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest(configuration, commandUtils, commandRegistry);
    messageEvent.setMessage(Message.of(String.format("%s %s", subjectUnderTest.getTrigger(), command.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result.value).isEqualTo(String.format(COMMAND_MISSING_MESSAGE_FORMAT_DEFAULT,
        messageEvent.getChatUser().getTwitchLogin(), command.getTrigger()));
  }

  @Test
  public final void handleMessageEvent_missingCommandAndCustomFormat_sendsCustomMissingMessage() {
    final String customFormat = this.arbitraryDataGenerator.getString() + " %s %s";
    final MapConfiguration configuration = this.givenAnAppConfiguration();
    configuration.set(COMMAND_MISSING_MESSAGE_FORMAT_KEY, customFormat);
    final CommandUtils commandUtils = this.givenACommandUtils(configuration);
    final Command command = new ArbitraryCommand(configuration, commandUtils, this.arbitraryDataGenerator);
    final CommandRegistry commandRegistry = this.givenACommandRegistry(
        new ArbitraryCommand(configuration, commandUtils, this.arbitraryDataGenerator));
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest(configuration, commandUtils, commandRegistry);
    messageEvent.setMessage(Message.of(String.format("%s %s", subjectUnderTest.getTrigger(), command.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result.value).isEqualTo(String.format(customFormat, messageEvent.getChatUser().getTwitchLogin(),
        command.getTrigger()));
  }

  @Test
  public final void handleMessageEvent_forNonTriggerableCommand_sendsDefaultMissingMessage() {
    final Configuration configuration = this.givenAnAppConfiguration();
    final CommandUtils commandUtils = this.givenACommandUtils(configuration);
    final Command command = new ArbitraryCommand(configuration, commandUtils, this.arbitraryDataGenerator, false);
    final CommandRegistry commandRegistry = this.givenACommandRegistry(command);
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest(configuration, commandUtils, commandRegistry);
    messageEvent.setMessage(Message.of(String.format("%s %s", subjectUnderTest.getTrigger(), command.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result.value).isEqualTo(String.format(COMMAND_MISSING_MESSAGE_FORMAT_DEFAULT,
        messageEvent.getChatUser().getTwitchLogin(), command.getTrigger()));
  }

}
