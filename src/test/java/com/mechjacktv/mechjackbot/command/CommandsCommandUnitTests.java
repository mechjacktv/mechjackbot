package com.mechjacktv.mechjackbot.command;

import static com.mechjacktv.mechjackbot.command.CommandsCommand.COMMAND_MESSAGE_FORMAT_DEFAULT;
import static com.mechjacktv.mechjackbot.command.CommandsCommand.COMMAND_MESSAGE_FORMAT_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.chatbot.DefaultCommandRegistry;
import com.mechjacktv.mechjackbot.configuration.MapAppConfiguration;
import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultTimeUtils;

public class CommandsCommandUnitTests extends CommandContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private final ArbitraryCommandTestUtils commandTestUtils = new ArbitraryCommandTestUtils(this.arbitraryDataGenerator);

  @Override
  protected Command givenASubjectToTest(final AppConfiguration appConfiguration) {
    return this.givenASubjectToTest(appConfiguration, mock(CommandRegistry.class));
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

  private CommandUtils givenACommandUtils(final AppConfiguration appConfiguration) {
    return new DefaultCommandUtils(appConfiguration, this.executionUtils, new DefaultTimeUtils());
  }

  private Set<Command> givenASetOfCommands(final AppConfiguration appConfiguration,
      final CommandUtils commandUtils) {
    final Set<Command> commands = new HashSet<>();

    for (int i = 0; i < 3; i++) {
      commands.add(new ArbitraryCommand(appConfiguration, commandUtils, this.arbitraryDataGenerator));
    }
    return commands;
  }

  private CommandRegistry givenACommandRegistry(final Set<Command> commands) {
    final CommandRegistry commandRegistry = new DefaultCommandRegistry(this.executionUtils);

    for (final Command command : commands) {
      commandRegistry.addCommand(command);
    }
    return commandRegistry;
  }

  @Test
  public final void handleMessageEvent_defaultFormat_sendsDefaultListOfCommands() {
    final MapAppConfiguration appConfiguration = this.givenAnAppConfiguration();
    final CommandUtils commandUtils = this.givenACommandUtils(appConfiguration);
    final Set<Command> commands = this.givenASetOfCommands(appConfiguration, commandUtils);
    final CommandRegistry commandRegistry = this.givenACommandRegistry(commands);
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, commandRegistry);

    subjectUnderTest.handleMessageEvent(messageEvent);

    final SoftAssertions softly = new SoftAssertions();
    final Message message = messageEvent.getResponseMessage();
    softly.assertThat(message.value).contains(this.stripFormat(COMMAND_MESSAGE_FORMAT_DEFAULT));
    for (final Command command : commands) {
      softly.assertThat(message.value).contains(command.getTrigger().value);
    }
    softly.assertAll();
  }

  @Test
  public final void handleMessageEvent_customFormat_sendsCustomListOfCommands() {
    final String messageFormat = this.arbitraryDataGenerator.getString() + ": %s";
    final MapAppConfiguration appConfiguration = this.givenAnAppConfiguration();
    final CommandUtils commandUtils = this.givenACommandUtils(appConfiguration);
    final Set<Command> commands = this.givenASetOfCommands(appConfiguration, commandUtils);
    final CommandRegistry commandRegistry = this.givenACommandRegistry(commands);
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, commandRegistry);
    appConfiguration.set(COMMAND_MESSAGE_FORMAT_KEY, messageFormat);

    subjectUnderTest.handleMessageEvent(messageEvent);

    final SoftAssertions softly = new SoftAssertions();
    final Message message = messageEvent.getResponseMessage();
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
    final MapAppConfiguration appConfiguration = this.givenAnAppConfiguration();
    final CommandUtils commandUtils = this.givenACommandUtils(appConfiguration);
    final Command nonTriggerableCommand = new ArbitraryCommand(appConfiguration, commandUtils,
        this.arbitraryDataGenerator, false);
    final Set<Command> commands = Sets.newHashSet(nonTriggerableCommand,
        new ArbitraryCommand(appConfiguration, commandUtils, this.arbitraryDataGenerator),
        new ArbitraryCommand(appConfiguration, commandUtils, this.arbitraryDataGenerator));
    final CommandRegistry commandRegistry = this.givenACommandRegistry(commands);
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, commandRegistry);

    subjectUnderTest.handleMessageEvent(messageEvent);

    assertThat(messageEvent.getResponseMessage().value).doesNotContain(nonTriggerableCommand.getTrigger().value);
  }

}
