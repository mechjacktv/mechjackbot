package com.mechjacktv.mechjackbot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.mechjacktv.mechjackbot.configuration.MapAppConfiguration;
import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.ExecutionUtils;

public abstract class CommandContractTests {

  protected final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();

  protected final ExecutionUtils executionUtils = new DefaultExecutionUtils();

  protected final Command givenASubjectToTest() {
    return this.givenASubjectToTest(this.givenAnAppConfiguration());
  }

  protected abstract Command givenASubjectToTest(final AppConfiguration appConfiguration);

  protected abstract CommandTriggerKey getCommandTriggerKey();

  protected abstract CommandTrigger getCommandTriggerDefault();

  protected final MapAppConfiguration givenAnAppConfiguration() {
    return new MapAppConfiguration(this.executionUtils);
  }

  private MapAppConfiguration givenAnAppConfiguration(final CommandTrigger commandTrigger) {
    final MapAppConfiguration appConfiguration = this.givenAnAppConfiguration();

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
    final AppConfiguration appConfiguration = this.givenAnAppConfiguration(commandTrigger);
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration);

    final CommandTrigger result = subjectUnderTest.getTrigger();

    assertThat(result.value).isEqualTo(commandTrigger.value);
  }

  @Test
  public final void getTrigger_overrideConfigured_returnsOverrideTrigger() {
    final CommandTrigger commandTrigger = CommandTrigger.of(this.arbitraryDataGenerator.getString());
    final AppConfiguration appConfiguration = this.givenAnAppConfiguration(commandTrigger);
    final Command subjectUnderTest = this.givenASubjectToTest(appConfiguration);

    final CommandTrigger result = subjectUnderTest.getTrigger();

    assertThat(result.value).isEqualTo(commandTrigger.value);
  }

  @Test
  public final void isTriggered_messageContainsTrigger_returnsTrue() {
    final Command subjectUnderTest = this.givenASubjectToTest();
    assumeTrue(subjectUnderTest.isTriggerable());
    final CommandTrigger commandTrigger = this.getCommandTriggerDefault();
    final MessageEvent messageEvent = mock(MessageEvent.class);
    final Message message = Message.of(String.format("%s %s", commandTrigger.value,
        this.arbitraryDataGenerator.getString()));
    when(messageEvent.getMessage()).thenReturn(message);

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isTriggered_messageDoesNotContainTrigger_returnsFalse() {
    final Command subjectUnderTest = this.givenASubjectToTest();
    assumeTrue(subjectUnderTest.isTriggerable());
    final MessageEvent messageEvent = mock(MessageEvent.class);
    final ChatUser chatUser = mock(ChatUser.class);
    final Message message = Message.of(this.arbitraryDataGenerator.getString());
    when(messageEvent.getChatUser()).thenReturn(chatUser);
    when(chatUser.getUsername()).thenReturn(ChatUsername.of(this.arbitraryDataGenerator.getString()));
    when(messageEvent.getMessage()).thenReturn(message);

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isFalse();
  }

}
