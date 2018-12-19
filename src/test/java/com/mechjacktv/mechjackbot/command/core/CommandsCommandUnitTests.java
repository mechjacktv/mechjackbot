package com.mechjacktv.mechjackbot.command.core;

import static com.mechjacktv.mechjackbot.command.core.CommandsCommand.DEFAULT_DESCRIPTION;
import static com.mechjacktv.mechjackbot.command.core.CommandsCommand.DEFAULT_TRIGGER;
import static com.mechjacktv.mechjackbot.command.core.CommandsCommand.KEY_DESCRIPTION;
import static com.mechjacktv.mechjackbot.command.core.CommandsCommand.KEY_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.command.core.CommandsCommand.KEY_TRIGGER;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Test;

import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandDescription;
import com.mechjacktv.mechjackbot.CommandRegistry;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.mechjackbot.Message;
import com.mechjacktv.mechjackbot.TestCommand;
import com.mechjacktv.mechjackbot.TestCommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.TestMessageEvent;
import com.mechjacktv.mechjackbot.command.BaseCommandContractTests;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandMessageFormat;

public class CommandsCommandUnitTests extends BaseCommandContractTests {

  protected final CommandsCommand givenASubjectToTest() {
    return new CommandsCommand(this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class),
        this.testFrameworkRule.getInstance(CommandRegistry.class));
  }

  @Override
  protected CommandDescription getDescriptionDefault() {
    return CommandDescription.of(DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(KEY_DESCRIPTION, CommandsCommand.class);
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, CommandsCommand.class);
  }

  @Override
  protected CommandTrigger getTriggerDefault() {
    return CommandTrigger.of(DEFAULT_TRIGGER);
  }

  private CommandMessageFormat getMessageFormatDefault() {
    return CommandMessageFormat.of(CommandsCommand.DEFAULT_MESSAGE_FORMAT);
  }

  private ConfigurationKey getMessageFormatKey() {
    return ConfigurationKey.of(KEY_MESSAGE_FORMAT, CommandsCommand.class);
  }

  private Set<Command> givenASetOfCommands() {
    final Set<Command> commands = new HashSet<>();

    for (int i = 0; i < 3; i++) {
      commands.add(new TestCommand(
          new TestCommandConfigurationBuilder(this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class))
              .setDefaultTrigger(UUID.randomUUID().toString())));
    }
    return commands;
  }

  @Test
  public final void handleMessageEvent_noMessageFormatConfigured_resultIsDefaultMessage() {
    this.installModules();
    final Set<Command> commands = this.givenASetOfCommands();
    final CommandRegistry commandRegistry = this.testFrameworkRule.getInstance(CommandRegistry.class);
    commands.forEach(commandRegistry::addCommand);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final CommandsCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result).isEqualTo(Message.of(String.format(this.getMessageFormatDefault().value,
        messageEvent.getChatUser().getTwitchLogin(), commands
            .stream().map(command -> command.getTrigger().value)
            .sorted().collect(Collectors.joining(" ")))));
  }

  @Test
  public final void handleMessageEvent_customMessageFormatConfigured_resultIsCustomMessage() {
    this.installModules();
    final String customMessageFormat = this.testFrameworkRule.getArbitraryString() + ": %2$s %1$s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getMessageFormatKey(), customMessageFormat);
    final Set<Command> commands = this.givenASetOfCommands();
    final CommandRegistry commandRegistry = this.testFrameworkRule.getInstance(CommandRegistry.class);
    commands.forEach(commandRegistry::addCommand);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final CommandsCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result).isEqualTo(Message.of(String.format(customMessageFormat,
        messageEvent.getChatUser().getTwitchLogin(), commands
            .stream().map(command -> command.getTrigger().value)
            .sorted().collect(Collectors.joining(" ")))));
  }

  @Test
  public final void handleMessageEvent_noMessageFormatConfiguredWithNonTriggerableCommand_resultIsDefaultMessageWithoutNonTriggerableCommand() {
    this.installModules();
    final TestCommand nonTriggerableCommand = this.testFrameworkRule.getInstance(TestCommand.class);
    nonTriggerableCommand.setTriggerable(false);
    final Set<Command> commands = this.givenASetOfCommands();
    final CommandRegistry commandRegistry = this.testFrameworkRule.getInstance(CommandRegistry.class);
    commandRegistry.addCommand(nonTriggerableCommand);
    commands.forEach(commandRegistry::addCommand);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final CommandsCommand subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    System.out.println(commands
        .stream().map(command -> command.getTrigger().value)
        .sorted().collect(Collectors.joining(" ")));
    assertThat(result).isEqualTo(Message.of(String.format(this.getMessageFormatDefault().value,
        messageEvent.getChatUser().getTwitchLogin(), commands
            .stream().map(command -> command.getTrigger().value)
            .sorted().collect(Collectors.joining(" ")))));
  }

}
