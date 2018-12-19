package com.mechjacktv.mechjackbot.command.core;

import static com.mechjacktv.mechjackbot.command.core.HelpCommand.DEFAULT_DESCRIPTION;
import static com.mechjacktv.mechjackbot.command.core.HelpCommand.DEFAULT_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.command.core.HelpCommand.DEFAULT_MISSING_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.command.core.HelpCommand.DEFAULT_TRIGGER;
import static com.mechjacktv.mechjackbot.command.core.HelpCommand.KEY_DESCRIPTION;
import static com.mechjacktv.mechjackbot.command.core.HelpCommand.KEY_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.command.core.HelpCommand.KEY_MISSING_MESSAGE_FORMAT;
import static com.mechjacktv.mechjackbot.command.core.HelpCommand.KEY_TRIGGER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.mechjackbot.CommandDescription;
import com.mechjacktv.mechjackbot.CommandRegistry;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.mechjackbot.CommandUtils;
import com.mechjacktv.mechjackbot.Message;
import com.mechjacktv.mechjackbot.TestCommand;
import com.mechjacktv.mechjackbot.TestMessageEvent;
import com.mechjacktv.mechjackbot.command.BaseCommandContractTests;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandMessageFormat;

public class HelpCommandUnitTests extends BaseCommandContractTests {

  protected final HelpCommand givenASubjectToTest() {
    return new HelpCommand(this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class),
        this.testFrameworkRule.getInstance(CommandRegistry.class),
        this.testFrameworkRule.getInstance(CommandUtils.class),
        this.testFrameworkRule.getInstance(Configuration.class));
  }

  @Override
  protected CommandDescription getDescriptionDefault() {
    return CommandDescription.of(DEFAULT_DESCRIPTION);
  }

  @Override
  protected ConfigurationKey getDescriptionKey() {
    return ConfigurationKey.of(KEY_DESCRIPTION, HelpCommand.class);
  }

  @Override
  protected ConfigurationKey getTriggerKey() {
    return ConfigurationKey.of(KEY_TRIGGER, HelpCommand.class);
  }

  @Override
  protected CommandTrigger getTriggerDefault() {
    return CommandTrigger.of(DEFAULT_TRIGGER);
  }

  private CommandMessageFormat getMessageFormatDefault() {
    return CommandMessageFormat.of(DEFAULT_MESSAGE_FORMAT);
  }

  private ConfigurationKey getMessageFormatKey() {
    return ConfigurationKey.of(KEY_MESSAGE_FORMAT, HelpCommand.class);
  }

  private CommandMessageFormat getMissingMessageFormatDefault() {
    return CommandMessageFormat.of(DEFAULT_MISSING_MESSAGE_FORMAT);
  }

  private ConfigurationKey getMissingMessageFormatKey() {
    return ConfigurationKey.of(KEY_MISSING_MESSAGE_FORMAT, HelpCommand.class);
  }

  @Test
  public final void handleMessageEvent_messageNotProperlyFormatted_resultIsUsageMessage() {
    this.installModules();
    final TestCommand command = this.testFrameworkRule.getInstance(TestCommand.class);
    final CommandRegistry commandRegistry = this.testFrameworkRule.getInstance(CommandRegistry.class);
    commandRegistry.addCommand(command);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final HelpCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setMessage(Message.of(subjectUnderTest.getTrigger().value));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    this.assertUsageMessageForCommand(result, subjectUnderTest, messageEvent);
  }

  @Test
  public final void handleMessageEvent_noMessageFormatConfiguredWithTriggerableCommand_resultIsDefaultMessage() {
    this.installModules();
    final TestCommand command = this.testFrameworkRule.getInstance(TestCommand.class);
    final CommandRegistry commandRegistry = this.testFrameworkRule.getInstance(CommandRegistry.class);
    commandRegistry.addCommand(command);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final HelpCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setMessage(Message.of(String.format("%s %s", subjectUnderTest.getTrigger(), command.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result.value).isEqualTo(String.format(this.getMessageFormatDefault().value,
        messageEvent.getChatUser().getTwitchLogin(), command.getTrigger(), command.getDescription()));
  }

  @Test
  public final void handleMessageEvent_customMessageFormatConfiguredWithTriggerableCommand_resultIsCustomMessage() {
    this.installModules();
    final String customMessageFormat = this.testFrameworkRule.getArbitraryString() + " %s %s %s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getMessageFormatKey(), customMessageFormat);
    final TestCommand command = this.testFrameworkRule.getInstance(TestCommand.class);
    final CommandRegistry commandRegistry = this.testFrameworkRule.getInstance(CommandRegistry.class);
    commandRegistry.addCommand(command);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final HelpCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setMessage(Message.of(String.format("%s %s", subjectUnderTest.getTrigger(), command.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result.value).isEqualTo(String.format(customMessageFormat,
        messageEvent.getChatUser().getTwitchLogin(), command.getTrigger(), command.getDescription()));
  }

  @Test
  public final void handleMessageEvent_noMissingMessageFormatConfigured_resultIsDefaultMissingMessage() {
    this.installModules();
    final TestCommand command = this.testFrameworkRule.getInstance(TestCommand.class);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final HelpCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setMessage(Message.of(String.format("%s %s", subjectUnderTest.getTrigger(), command.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result.value).isEqualTo(String.format(this.getMissingMessageFormatDefault().value,
        messageEvent.getChatUser().getTwitchLogin(), command.getTrigger()));
  }

  @Test
  public final void handleMessageEvent_customMissingMessageFormatConfigured_resultIsCustomMissingMessage() {
    this.installModules();
    final String customMessageFormat = this.testFrameworkRule.getArbitraryString() + " %s %s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getMissingMessageFormatKey(), customMessageFormat);
    final TestCommand command = this.testFrameworkRule.getInstance(TestCommand.class);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final HelpCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setMessage(Message.of(String.format("%s %s", subjectUnderTest.getTrigger(), command.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result.value).isEqualTo(String.format(customMessageFormat,
        messageEvent.getChatUser().getTwitchLogin(), command.getTrigger()));
  }

  @Test
  public final void handleMessageEvent_noMissingMessageFormatConfiguredWithNonTriggerableCommand_resultIsDefaultMissingMessage() {
    this.installModules();
    final TestCommand command = this.testFrameworkRule.getInstance(TestCommand.class);
    command.setTriggerable(false);
    final CommandRegistry commandRegistry = this.testFrameworkRule.getInstance(CommandRegistry.class);
    commandRegistry.addCommand(command);
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    final HelpCommand subjectUnderTest = this.givenASubjectToTest();
    messageEvent.setMessage(Message.of(String.format("%s %s", subjectUnderTest.getTrigger(), command.getTrigger())));

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result.value).isEqualTo(String.format(this.getMissingMessageFormatDefault().value,
        messageEvent.getChatUser().getTwitchLogin(), command.getTrigger()));
  }

}
