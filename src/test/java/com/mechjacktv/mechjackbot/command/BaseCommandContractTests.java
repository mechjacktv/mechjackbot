package com.mechjacktv.mechjackbot.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;

import org.junit.Test;

import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.mechjackbot.Command;
import com.mechjacktv.mechjackbot.CommandContractTests;
import com.mechjacktv.mechjackbot.CommandDescription;
import com.mechjacktv.mechjackbot.CommandTrigger;
import com.mechjacktv.mechjackbot.Message;
import com.mechjacktv.mechjackbot.MessageEvent;

public abstract class BaseCommandContractTests extends CommandContractTests {

  @Override
  protected void installModules() {
    super.installModules();
    this.testFrameworkRule.installModule(new CommandTestModule());
  }

  protected abstract CommandDescription getDescriptionDefault();

  protected abstract ConfigurationKey getDescriptionKey();

  protected abstract CommandTrigger getTriggerDefault();

  protected abstract ConfigurationKey getTriggerKey();

  protected final void assertUsageMessageForCommand(final Message result, final Command subjectUnderTest,
      final MessageEvent messageEvent) {
    final CommandAssertionsUtils commandAssertionsUtils = this.testFrameworkRule
        .getInstance(CommandAssertionsUtils.class);

    commandAssertionsUtils.assertUsageMessageForCommand(result, subjectUnderTest, messageEvent);
  }

  @Test
  public final void getDescription_noDescriptionConfigured_resultIsEqualToDefaultDescription() {
    this.installModules();
    final Command subjectUnderTest = this.givenASubjectToTest();

    final CommandDescription result = subjectUnderTest.getDescription();

    assertThat(result).isEqualTo(this.getDescriptionDefault());
  }

  @Test
  public final void getTrigger_noTriggerConfigured_resultIsEqualToDefaultTrigger() {
    this.installModules();
    final Command subjectUnderTest = this.givenASubjectToTest();
    assumeTrue(subjectUnderTest.isTriggerable());

    final CommandTrigger result = subjectUnderTest.getTrigger();

    assertThat(result).isEqualTo(this.getTriggerDefault());
  }

  @Test
  public final void getDescription_customDescriptionConfigured_resultIsEqualToCustomDescription() {
    this.installModules();
    final CommandDescription commandDescription = CommandDescription.of(this.testFrameworkRule.getArbitraryString());
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getDescriptionKey(), commandDescription.value);
    final Command subjectUnderTest = this.givenASubjectToTest();

    final CommandDescription result = subjectUnderTest.getDescription();

    assertThat(result).isEqualTo(commandDescription);
  }

  @Test
  public final void getTrigger_customTriggerConfigured_resultIsEqualToCustomTrigger() {
    this.installModules();
    final CommandTrigger commandTrigger = CommandTrigger.of(this.testFrameworkRule.getArbitraryString());
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getTriggerKey(), commandTrigger.value);
    final Command subjectUnderTest = this.givenASubjectToTest();
    assumeTrue(subjectUnderTest.isTriggerable());

    final CommandTrigger result = subjectUnderTest.getTrigger();

    assertThat(result).isEqualTo(commandTrigger);
  }

}
