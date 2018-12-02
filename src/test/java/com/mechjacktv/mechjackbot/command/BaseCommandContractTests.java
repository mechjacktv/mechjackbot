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

public abstract class BaseCommandContractTests extends CommandContractTests {

  protected abstract CommandDescription getDescriptionDefault();

  protected abstract ConfigurationKey getDescriptionKey();

  protected abstract CommandTrigger getTriggerDefault();

  protected abstract ConfigurationKey getTriggerKey();

  @Test
  public final void getDescription_defaultDescription_returnsDefaultDescription() {
    final Command subjectUnderTest = this.givenASubjectToTest();

    final CommandDescription result = subjectUnderTest.getDescription();

    assertThat(result).isEqualTo(this.getDescriptionDefault());
  }

  @Test
  public final void getTrigger_defaultTrigger_returnsDefaultTrigger() {
    final Command subjectUnderTest = this.givenASubjectToTest();
    assumeTrue(subjectUnderTest.isTriggerable());

    final CommandTrigger result = subjectUnderTest.getTrigger();

    assertThat(result).isEqualTo(this.getTriggerDefault());
  }

  @Test
  public final void getDescription_customDescription_returnsCustomDescription() {
    final CommandDescription commandDescription = CommandDescription.of(this.arbitraryDataGenerator.getString());
    final MapConfiguration configuration = this.givenAConfiguration();
    configuration.set(this.getDescriptionKey().value, commandDescription.value);
    final Command subjectUnderTest = this.givenASubjectToTest(configuration);

    final CommandDescription result = subjectUnderTest.getDescription();

    assertThat(result.value).isEqualTo(commandDescription.value);
  }

  @Test
  public final void getTrigger_customTrigger_returnsCustomTrigger() {
    final CommandTrigger commandTrigger = CommandTrigger.of(this.arbitraryDataGenerator.getString());
    final MapConfiguration configuration = this.givenAConfiguration();
    configuration.set(this.getTriggerKey().value, commandTrigger.value);
    final Command subjectUnderTest = this.givenASubjectToTest(configuration);
    assumeTrue(subjectUnderTest.isTriggerable());

    final CommandTrigger result = subjectUnderTest.getTrigger();

    assertThat(result.value).isEqualTo(commandTrigger.value);
  }

}
