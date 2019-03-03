package tv.mechjack.mechjackbot.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;

import org.junit.Rule;
import org.junit.Test;

import tv.mechjack.platform.configuration.TestConfigurationModule;
import tv.mechjack.platform.utils.TestUtilsModule;
import tv.mechjack.testframework.TestFramework;

public abstract class ChatCommandContractTests {

  @Rule
  public final TestFramework testFramework = new TestFramework();

  protected void registerModules() {
    this.testFramework.registerModule(new TestConfigurationModule());
    this.testFramework.registerModule(new TestCommandModule());
    this.testFramework.registerModule(new TestUtilsModule());
  }

  protected abstract ChatCommand givenASubjectToTest();

  @Test
  public final void getDescription_whenCalled_resultIsNotNull() {
    this.registerModules();
    final ChatCommand subjectUnderTest = this.givenASubjectToTest();

    final ChatCommandDescription result = subjectUnderTest.getDescription();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getName_whenCalled_resultIsNotNull() {
    this.registerModules();
    final ChatCommand subjectUnderTest = this.givenASubjectToTest();

    final ChatCommandName result = subjectUnderTest.getName();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getTrigger_whenCalled_resultIsNotNull() {
    this.registerModules();
    final ChatCommand subjectUnderTest = this.givenASubjectToTest();

    final ChatCommandTrigger result = subjectUnderTest.getTrigger();

    assertThat(result).isNotNull();
  }

  @Test
  public final void getUsage_whenCalled_resultIsNotNull() {
    this.registerModules();
    final ChatCommand subjectUnderTest = this.givenASubjectToTest();

    final ChatCommandUsage result = subjectUnderTest.getUsage();

    assertThat(result).isNotNull();
  }

  @Test
  public final void isTriggered_messageContainsTrigger_resultIsTrue() {
    this.registerModules();
    final ChatCommand subjectUnderTest = this.givenASubjectToTest();
    assumeTrue(subjectUnderTest.isTriggerable());
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);
    messageEvent.setChatMessage(ChatMessage.of(String.format("%s %s", subjectUnderTest.getTrigger(),
        this.testFramework.arbitraryData().getString())));

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isTrue();
  }

  @Test
  public final void isTriggered_messageDoesNotContainTrigger_resultIsFalse() {
    this.registerModules();
    final ChatCommand subjectUnderTest = this.givenASubjectToTest();
    assumeTrue(subjectUnderTest.isTriggerable());
    final TestChatMessageEvent messageEvent = this.testFramework.getInstance(TestChatMessageEvent.class);

    final boolean result = subjectUnderTest.isTriggered(messageEvent);

    assertThat(result).isFalse();
  }

}
