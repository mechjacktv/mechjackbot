package com.mechjacktv.mechjackbot.command.core;

import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.configuration.ConfigurationTestModule;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.ArbitraryMessageEvent;
import com.mechjacktv.mechjackbot.command.BaseCommand;
import com.mechjacktv.mechjackbot.command.CommandConfigurationBuilder;
import com.mechjacktv.mechjackbot.command.CommandTestModule;
import com.mechjacktv.testframework.ArbitraryDataGenerator;
import com.mechjacktv.testframework.TestFrameworkRule;
import com.mechjacktv.util.UtilTestModule;

import org.junit.Rule;
import org.junit.Test;

import static com.mechjacktv.mechjackbot.command.core.TestCommand.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;

public class TestCommandUnitTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  public final void installModules() {
    this.testFrameworkRule.installModule(new CommandTestModule());
    this.testFrameworkRule.installModule(new ConfigurationTestModule());
    this.testFrameworkRule.installModule(new UtilTestModule());
  }

  private TestCommand givenASubjectToTest() {
    return new TestCommand(this.testFrameworkRule.getInstance(CommandConfigurationBuilder.class));
  }

  @Test
  public final void getDescription_whenCalled_resultIsNotNull() {
    this.installModules();
    final Command subjectUnderTest = this.givenASubjectToTest();

    final CommandDescription result = subjectUnderTest.getDescription();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getName_whenCalled_resultIsNotNull() {
    this.installModules();
    final Command subjectUnderTest = this.givenASubjectToTest();

    final CommandName result = subjectUnderTest.getName();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getTrigger_whenCalled_resultIsNotNull() {
    this.installModules();
    final Command subjectUnderTest = this.givenASubjectToTest();

    final CommandTrigger result = subjectUnderTest.getTrigger();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getUsage_whenCalled_resultIsNotNull() {
    this.installModules();
    final Command subjectUnderTest = this.givenASubjectToTest();

    final CommandUsage result = subjectUnderTest.getUsage();

    assertThat(result).isNotNull();
  }

  @Test
  public final void isTriggered_messageContainsTrigger_returnsTrue() {
    this.installModules();
    final Command subjectUnderTest = this.givenASubjectToTest();
    assumeTrue(subjectUnderTest.isTriggerable());
    final ArbitraryDataGenerator arbitraryDataGenerator = this.testFrameworkRule
        .getInstance(ArbitraryDataGenerator.class);
    final ArbitraryMessageEvent messageEvent = this.testFrameworkRule.getInstance(ArbitraryMessageEvent.class);
    messageEvent.setMessage(Message.of(String.format("%s %s", subjectUnderTest.getTrigger(),
        arbitraryDataGenerator.getString())));

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isTriggered_messageDoesNotContainTrigger_returnsFalse() {
    this.installModules();
    final Command subjectUnderTest = this.givenASubjectToTest();
    assumeTrue(subjectUnderTest.isTriggerable());

    final boolean result = subjectUnderTest
        .isTriggered(this.testFrameworkRule.getInstance(ArbitraryMessageEvent.class));

    assertThat(result).isFalse();
  }

  @Test
  public final void getDescription_defaultDescription_returnsDefaultDescription() {
    this.installModules();
    final Command subjectUnderTest = this.givenASubjectToTest();

    final CommandDescription result = subjectUnderTest.getDescription();

    assertThat(result).isEqualTo(CommandDescription.of(DEFAULT_DESCRIPTION));
  }

  @Test
  public final void getTrigger_defaultTrigger_returnsDefaultTrigger() {
    this.installModules();
    final Command subjectUnderTest = this.givenASubjectToTest();
    assumeTrue(subjectUnderTest.isTriggerable());

    final CommandTrigger result = subjectUnderTest.getTrigger();

    assertThat(result).isEqualTo(CommandTrigger.of(DEFAULT_TRIGGER));
  }

  @Test
  public final void getDescription_customDescription_returnsCustomDescription() {
    this.installModules();
    final CommandDescription commandDescription = CommandDescription.of(this.testFrameworkRule.getArbitraryString());
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(ConfigurationKey.of(BaseCommand.DESCRIPTION_KEY, TestCommand.class), commandDescription.value);
    final Command subjectUnderTest = this.givenASubjectToTest();

    final CommandDescription result = subjectUnderTest.getDescription();

    assertThat(result).isEqualTo(commandDescription);
  }

  @Test
  public final void getTrigger_customTrigger_returnsCustomTrigger() {
    this.installModules();
    final CommandTrigger commandTrigger = CommandTrigger.of(this.testFrameworkRule.getArbitraryString());
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(ConfigurationKey.of(BaseCommand.TRIGGER_KEY, TestCommand.class), commandTrigger.value);
    final Command subjectUnderTest = this.givenASubjectToTest();
    assumeTrue(subjectUnderTest.isTriggerable());

    final CommandTrigger result = subjectUnderTest.getTrigger();

    assertThat(result).isEqualTo(commandTrigger);
  }

  @Test
  public final void handleMessageEvent_defaultMessageFormat_sendsDefaultMessage() {
    this.installModules();
    final ArbitraryMessageEvent messageEvent = this.testFrameworkRule.getInstance(ArbitraryMessageEvent.class);
    final Command subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result).isEqualTo(Message.of(String.format(DEFAULT_MESSAGE_FORMAT,
        messageEvent.getChatUser().getTwitchLogin(), subjectUnderTest.getTrigger())));
  }

  @Test
  public final void handleMessageEvent_customMessageFormat_sendsCustomMessage() {
    this.installModules();
    final String customMessageFormat = this.testFrameworkRule.getArbitraryString() + "%s %s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(ConfigurationKey.of(BaseCommand.MESSAGE_FORMAT_KEY, TestCommand.class), customMessageFormat);
    final ArbitraryMessageEvent messageEvent = this.testFrameworkRule.getInstance(ArbitraryMessageEvent.class);
    final Command subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.handleMessageEvent(messageEvent);
    final Message result = messageEvent.getResponseMessage();

    assertThat(result).isEqualTo(Message.of(String.format(customMessageFormat,
        messageEvent.getChatUser().getTwitchLogin(), subjectUnderTest.getTrigger())));
  }

}
