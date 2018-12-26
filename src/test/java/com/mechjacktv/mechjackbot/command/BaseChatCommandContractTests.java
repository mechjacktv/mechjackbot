package com.mechjacktv.mechjackbot.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;

import org.junit.Test;

import com.mechjacktv.configuration.ConfigurationKey;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.mechjackbot.ChatCommand;
import com.mechjacktv.mechjackbot.ChatCommandContractTests;
import com.mechjacktv.mechjackbot.ChatCommandDescription;
import com.mechjacktv.mechjackbot.ChatCommandTrigger;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.mechjackbot.ChatMessageEvent;

public abstract class BaseChatCommandContractTests extends ChatCommandContractTests {

  protected abstract ChatCommandDescription getDescriptionDefault();

  protected abstract ConfigurationKey getDescriptionKey();

  protected abstract ChatCommandTrigger getTriggerDefault();

  protected abstract ConfigurationKey getTriggerKey();

  protected final void assertUsageMessageForCommand(final ChatMessage result, final ChatCommand subjectUnderTest,
      final ChatMessageEvent chatMessageEvent) {
    final CommandAssertionsUtils commandAssertionsUtils = this.testFrameworkRule
        .getInstance(CommandAssertionsUtils.class);

    commandAssertionsUtils.assertUsageMessageForCommand(result, subjectUnderTest, chatMessageEvent);
  }

  @Test
  public final void getDescription_noDescriptionConfigured_resultIsEqualToDefaultDescription() {
    this.installModules();
    final ChatCommand subjectUnderTest = this.givenASubjectToTest();

    final ChatCommandDescription result = subjectUnderTest.getDescription();

    assertThat(result).isEqualTo(this.getDescriptionDefault());
  }

  @Test
  public final void getDescription_customDescriptionConfigured_resultIsEqualToCustomDescription() {
    this.installModules();
    final ChatCommandDescription chatCommandDescription = ChatCommandDescription
        .of(this.testFrameworkRule.getArbitraryString());
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getDescriptionKey(), chatCommandDescription.value);
    final ChatCommand subjectUnderTest = this.givenASubjectToTest();

    final ChatCommandDescription result = subjectUnderTest.getDescription();

    assertThat(result).isEqualTo(chatCommandDescription);
  }

  @Test
  public final void getTrigger_noTriggerConfigured_resultIsEqualToDefaultTrigger() {
    this.installModules();
    final ChatCommand subjectUnderTest = this.givenASubjectToTest();
    assumeTrue(subjectUnderTest.isTriggerable());

    final ChatCommandTrigger result = subjectUnderTest.getTrigger();

    assertThat(result).isEqualTo(this.getTriggerDefault());
  }

  @Test
  public final void getTrigger_customTriggerConfigured_resultIsEqualToCustomTrigger() {
    this.installModules();
    final ChatCommandTrigger chatCommandTrigger = ChatCommandTrigger.of(this.testFrameworkRule.getArbitraryString());
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(this.getTriggerKey(), chatCommandTrigger.value);
    final ChatCommand subjectUnderTest = this.givenASubjectToTest();
    assumeTrue(subjectUnderTest.isTriggerable());

    final ChatCommandTrigger result = subjectUnderTest.getTrigger();

    assertThat(result).isEqualTo(chatCommandTrigger);
  }

}
