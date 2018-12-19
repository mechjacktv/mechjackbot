package com.mechjacktv.mechjackbot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;

import org.junit.Rule;
import org.junit.Test;

import com.mechjacktv.configuration.ConfigurationTestModule;
import com.mechjacktv.mechjackbot.command.CommandTestModule;
import com.mechjacktv.testframework.TestFrameworkRule;
import com.mechjacktv.util.UtilTestModule;

public abstract class CommandContractTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  protected void installModules() {
    this.testFrameworkRule.installModule(new ConfigurationTestModule());
    this.testFrameworkRule.installModule(new CommandTestModule());
    this.testFrameworkRule.installModule(new UtilTestModule());
  }

  protected abstract Command givenASubjectToTest();

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
  public final void isTriggered_messageContainsTrigger_resultIsTrue() {
    this.installModules();
    final Command subjectUnderTest = this.givenASubjectToTest();
    assumeTrue(subjectUnderTest.isTriggerable());
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);
    messageEvent.setMessage(Message.of(String.format("%s %s", subjectUnderTest.getTrigger(),
        this.testFrameworkRule.getArbitraryString())));

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isTriggered_messageDoesNotContainTrigger_resultIsFalse() {
    this.installModules();
    final Command subjectUnderTest = this.givenASubjectToTest();
    assumeTrue(subjectUnderTest.isTriggerable());
    final TestMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestMessageEvent.class);

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isFalse();
  }

}
