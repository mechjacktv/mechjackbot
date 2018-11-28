package com.mechjacktv.mechjackbot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;

import org.junit.Test;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.configuration.SettingKey;
import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.ExecutionUtils;

public abstract class CommandContractTests {

  protected final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  protected final ExecutionUtils executionUtils = new DefaultExecutionUtils();

  protected final Command givenASubjectToTest() {
    return this.givenASubjectToTest(this.givenAnAppConfiguration());
  }

  protected abstract Command givenASubjectToTest(final Configuration configuration);

  protected abstract SettingKey getCommandTriggerKey();

  protected abstract CommandTrigger getCommandTriggerDefault();

  protected final MapConfiguration givenAnAppConfiguration() {
    return new MapConfiguration(this.executionUtils);
  }

  private MapConfiguration givenAnAppConfiguration(final CommandTrigger commandTrigger) {
    final MapConfiguration appConfiguration = this.givenAnAppConfiguration();

    appConfiguration.set(this.getCommandTriggerKey().value, commandTrigger.value);
    return appConfiguration;
  }

  @Test
  public final void getName_whenCalled_returnIsNotNull() {
    final Command subjectUnderTest = this.givenASubjectToTest();

    final CommandName result = subjectUnderTest.getName();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getDescription_whenCalled_returnIsNotNull() {
    final Command subjectUnderTest = this.givenASubjectToTest();

    final CommandDescription result = subjectUnderTest.getDescription();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getTrigger_nothingConfigured_returnsDefaultTrigger() {
    final CommandTrigger commandTrigger = this.getCommandTriggerDefault();
    final Configuration configuration = this.givenAnAppConfiguration(commandTrigger);
    final Command subjectUnderTest = this.givenASubjectToTest(configuration);

    final CommandTrigger result = subjectUnderTest.getTrigger();

    assertThat(result.value).isEqualTo(commandTrigger.value);
  }

  @Test
  public final void getTrigger_overrideConfigured_returnsOverrideTrigger() {
    final CommandTrigger commandTrigger = CommandTrigger.of(this.arbitraryDataGenerator.getString());
    final Configuration configuration = this.givenAnAppConfiguration(commandTrigger);
    final Command subjectUnderTest = this.givenASubjectToTest(configuration);

    final CommandTrigger result = subjectUnderTest.getTrigger();

    assertThat(result.value).isEqualTo(commandTrigger.value);
  }

  @Test
  public final void isTriggered_messageContainsTrigger_returnsTrue() {
    final ArbitraryMessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest();
    assumeTrue(subjectUnderTest.isTriggerable());
    messageEvent.setMessage(Message.of(String.format("%s %s", subjectUnderTest.getTrigger(),
        this.arbitraryDataGenerator.getString())));

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isTriggered_messageDoesNotContainTrigger_returnsFalse() {
    final MessageEvent messageEvent = new ArbitraryMessageEvent(this.arbitraryDataGenerator);
    final Command subjectUnderTest = this.givenASubjectToTest();
    assumeTrue(subjectUnderTest.isTriggerable());

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isFalse();
  }

}
