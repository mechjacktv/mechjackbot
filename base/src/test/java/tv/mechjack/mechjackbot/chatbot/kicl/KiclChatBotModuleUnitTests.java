package tv.mechjack.mechjackbot.chatbot.kicl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.kitteh.irc.client.library.Client;

import tv.mechjack.mechjackbot.api.ChatBotConfiguration;
import tv.mechjack.mechjackbot.api.TestCommandModule;
import tv.mechjack.platform.configuration.TestConfigurationModule;
import tv.mechjack.platform.utils.TestUtilsModule;
import tv.mechjack.testframework.TestFrameworkRule;

public class KiclChatBotModuleUnitTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  private void installModules() {
    this.testFrameworkRule.installModule(new TestCommandModule());
    this.testFrameworkRule.installModule(new TestConfigurationModule());
    this.testFrameworkRule.installModule(new TestKiclChatBotModule());
    this.testFrameworkRule.installModule(new TestUtilsModule());
  }

  private KiclChatBotModule givenASubjectToTest() {
    return new KiclChatBotModule();
  }

  @Test
  public final void createIrcClient_whenCalled_resultIsNotNull() {
    this.installModules();
    final KiclChatBotModule subjectUnderTest = this.givenASubjectToTest();

    final Client result = subjectUnderTest.createIrcClient(
        this.testFrameworkRule.getInstance(ChatBotConfiguration.class),
        this.testFrameworkRule.getInstance(KiclChatBotListener.class));

    assertThat(result).isNotNull();
  }

}