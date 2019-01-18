package tv.mechjack.mechjackbot.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;

import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.platform.configuration.TestConfigurationModule;
import tv.mechjack.platform.utils.TestUtilsModule;
import tv.mechjack.testframework.TestFrameworkRule;

public abstract class ChatCommandContractTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  protected void installModules() {
    this.testFrameworkRule.installModule(new TestConfigurationModule());
    this.testFrameworkRule.installModule(new TestCommandModule());
    this.testFrameworkRule.installModule(new TestUtilsModule());
  }

  protected abstract ChatCommand givenASubjectToTest();

  @Test
  public final void getDescription_whenCalled_resultIsNotNull() {
    this.installModules();
    final ChatCommand subjectUnderTest = this.givenASubjectToTest();

    final ChatCommandDescription result = subjectUnderTest.getDescription();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getName_whenCalled_resultIsNotNull() {
    this.installModules();
    final ChatCommand subjectUnderTest = this.givenASubjectToTest();

    final ChatCommandName result = subjectUnderTest.getName();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getTrigger_whenCalled_resultIsNotNull() {
    this.installModules();
    final ChatCommand subjectUnderTest = this.givenASubjectToTest();

    final ChatCommandTrigger result = subjectUnderTest.getTrigger();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getUsage_whenCalled_resultIsNotNull() {
    this.installModules();
    final ChatCommand subjectUnderTest = this.givenASubjectToTest();

    final ChatCommandUsage result = subjectUnderTest.getUsage();

    assertThat(result).isNotNull();
  }

  @Test
  public final void isTriggered_messageContainsTrigger_resultIsTrue() {
    this.installModules();
    final ChatCommand subjectUnderTest = this.givenASubjectToTest();
    assumeTrue(subjectUnderTest.isTriggerable());
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(),
        this.testFrameworkRule.getArbitraryString())));

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isTriggered_messageDoesNotContainTrigger_resultIsFalse() {
    this.installModules();
    final ChatCommand subjectUnderTest = this.givenASubjectToTest();
    assumeTrue(subjectUnderTest.isTriggerable());
    final TestChatMessageEvent messageEvent = this.testFrameworkRule.getInstance(TestChatMessageEvent.class);

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isFalse();
  }

}
