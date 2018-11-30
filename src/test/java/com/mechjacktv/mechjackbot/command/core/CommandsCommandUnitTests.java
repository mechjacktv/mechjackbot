package com.mechjacktv.mechjackbot.command.core;

import static com.mechjacktv.mechjackbot.command.BaseCommand.MESSAGE_FORMAT_KEY;
import static com.mechjacktv.mechjackbot.command.core.CommandsCommand.DESCRIPTION_DEFAULT;
import static com.mechjacktv.mechjackbot.command.core.CommandsCommand.MESSAGE_FORMAT_DEFAULT;
import static com.mechjacktv.mechjackbot.command.core.CommandsCommand.TRIGGER_DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.configuration.SettingKey;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.ArbitraryCommandTestUtils;
import com.mechjacktv.mechjackbot.command.BaseCommand;
import com.mechjacktv.mechjackbot.command.BaseCommandContractTests;
import com.mechjacktv.mechjackbot.command.DefaultCommandConfigurationBuilder;
import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.DefaultTimeUtils;
import com.mechjacktv.util.ExecutionUtils;

public class CommandsCommandUnitTests extends BaseCommandContractTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  private final ArbitraryCommandTestUtils commandTestUtils = new ArbitraryCommandTestUtils(this.arbitraryDataGenerator);

  private final ExecutionUtils executionUtils = new DefaultExecutionUtils();

  @Override
  protected Command givenASubjectToTest(final Configuration configuration) {
    return this.givenASubjectToTest(configuration, mock(CommandRegistry.class));
  }

  private Command givenASubjectToTest(final Configuration configuration, final CommandRegistry commandRegistry) {
    final DefaultCommandConfigurationBuilder builder = new DefaultCommandConfigurationBuilder(
        this.commandTestUtils.givenACommandUtils(configuration), configuration, this.executionUtils);

    return new CommandsCommand(builder, commandRegistry);
  }

  @Override
  protected CommandDescription getDescriptionDefault() {
    return CommandDescription.of(DESCRIPTION_DEFAULT);
  }

  @Override
  protected SettingKey getDescriptionKey() {
    return SettingKey.of(BaseCommand.DESCRIPTION_KEY, CommandsCommand.class);
  }

  @Override
  protected SettingKey getTriggerKey() {
    return SettingKey.of(BaseCommand.TRIGGER_KEY, CommandsCommand.class);
  }

  @Override
  protected CommandTrigger getTriggerDefault() {
    return CommandTrigger.of(TRIGGER_DEFAULT);
  }

  private CommandUtils givenACommandUtils(final Configuration configuration) {
    return new DefaultCommandUtils(configuration, this.executionUtils, new DefaultTimeUtils());
  }

  private Set<Command> givenASetOfCommands(final Configuration configuration,
      final CommandUtils commandUtils) {
    final Set<Command> commands = new HashSet<>();

    for (int i = 0; i < 3; i++) {
      commands.add(new ArbitraryCommand(configuration, commandUtils, this.arbitraryDataGenerator));
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
    final MapConfiguration appConfiguration = this.givenAConfiguration();
    final CommandUtils commandUtils = this.givenACommandUtils(appConfiguration);
    final Set<Command> commands = this.givenASetOfCommands(appConfiguration, commandUtils);
    final CommandRegistry commandRegistry = this.givenACommandRegistry(commands);
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, commandRegistry);

    subjectUnderTest.handleMessageEvent(messageEvent);

    final SoftAssertions softly = new SoftAssertions();
    final Message message = messageEvent.getResponseMessage();
    softly.assertThat(message.value).contains(this.stripFormat(MESSAGE_FORMAT_DEFAULT));
    for (final Command command : commands) {
      softly.assertThat(message.value).contains(command.getTrigger().value);
    }
    softly.assertAll();
  }

  @Test
  public final void handleMessageEvent_customFormat_sendsCustomListOfCommands() {
    final String messageFormat = this.arbitraryDataGenerator.getString() + ": %2$s";
    final MapConfiguration appConfiguration = this.givenAConfiguration();
    final CommandUtils commandUtils = this.givenACommandUtils(appConfiguration);
    final Set<Command> commands = this.givenASetOfCommands(appConfiguration, commandUtils);
    final CommandRegistry commandRegistry = this.givenACommandRegistry(commands);
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration, commandRegistry);
    appConfiguration.set(SettingKey.of(MESSAGE_FORMAT_KEY, CommandsCommand.class).value, messageFormat);

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
    return messageFormat.replace("%2$s", "").trim();
  }

  @Test
  public final void handleMessageEvent_withNonTriggerableCommands_doesNotListNonTriggerableCommands() {
    final MapConfiguration appConfiguration = this.givenAConfiguration();
    final CommandUtils commandUtils = this.givenACommandUtils(appConfiguration);
    final Command nonTriggerableCommand = new ArbitraryCommand(appConfiguration, commandUtils, this.executionUtils,
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
