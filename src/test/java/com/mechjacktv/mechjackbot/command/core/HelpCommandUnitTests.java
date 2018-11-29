package com.mechjacktv.mechjackbot.command.core;

import static com.mechjacktv.mechjackbot.CommandUtils.COMMAND_USAGE_MESSAGE_FORMAT_DEFAULT;
import static com.mechjacktv.mechjackbot.command.BaseCommand.MESSAGE_FORMAT_KEY;
import static com.mechjacktv.mechjackbot.command.core.HelpCommand.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.configuration.SettingKey;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.ArbitraryCommandTestUtils;
import com.mechjacktv.mechjackbot.command.BaseCommand;
import com.mechjacktv.mechjackbot.command.DefaultCommandConfigurationBuilder;
import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultTimeUtils;

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
    final DefaultCommandConfigurationBuilder builder = new DefaultCommandConfigurationBuilder(commandUtils,
        configuration);

    return new HelpCommand(builder, commandRegistry, commandUtils, configuration);
  }

  @Override
  protected SettingKey getCommandTriggerKey() {
    return SettingKey.of(BaseCommand.TRIGGER_KEY, HelpCommand.class);
  }

  @Override
  protected CommandTrigger getCommandTriggerDefault() {
    return CommandTrigger.of(TRIGGER_DEFAULT);
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
        messageEvent.getChatUser().getTwitchLogin(), subjectUnderTest.getTrigger(), USAGE));
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

    assertThat(result.value).isEqualTo(String.format(MESSAGE_FORMAT_DEFAULT,
        messageEvent.getChatUser().getTwitchLogin(), command.getTrigger(), command.getDescription()));
  }

  @Test
  public final void handleMessageEvent_forTriggerableCommandAndCustomFormat_sendsCustomMessage() {
    final String customFormat = this.arbitraryDataGenerator.getString() + " %s %s %s";
    final MapConfiguration configuration = this.givenAnAppConfiguration();
    configuration.set(SettingKey.of(MESSAGE_FORMAT_KEY, HelpCommand.class).value, customFormat);
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

    assertThat(result.value).isEqualTo(String.format(MISSING_MESSAGE_FORMAT_DEFAULT,
        messageEvent.getChatUser().getTwitchLogin(), command.getTrigger()));
  }

  @Test
  public final void handleMessageEvent_missingCommandAndCustomFormat_sendsCustomMissingMessage() {
    final String customFormat = this.arbitraryDataGenerator.getString() + " %s %s";
    final MapConfiguration configuration = this.givenAnAppConfiguration();
    configuration.set(SettingKey.of(MISSING_MESSAGE_FORMAT_KEY, HelpCommand.class).value, customFormat);
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

    assertThat(result.value).isEqualTo(String.format(MISSING_MESSAGE_FORMAT_DEFAULT,
        messageEvent.getChatUser().getTwitchLogin(), command.getTrigger()));
  }

}
