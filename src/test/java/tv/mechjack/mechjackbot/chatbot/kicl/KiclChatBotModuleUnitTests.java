package tv.mechjack.mechjackbot.chatbot.kicl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.kitteh.irc.client.library.Client;

import tv.mechjack.configuration.ConfigurationTestModule;
import tv.mechjack.mechjackbot.ChatBotConfiguration;
import tv.mechjack.mechjackbot.command.CommandTestModule;
import tv.mechjack.testframework.TestFrameworkRule;
import tv.mechjack.util.UtilTestModule;

public class KiclChatBotModuleUnitTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  private void installModules() {
    this.testFrameworkRule.installModule(new CommandTestModule());
    this.testFrameworkRule.installModule(new ConfigurationTestModule());
    this.testFrameworkRule.installModule(new KiclChatBotTestModule());
    this.testFrameworkRule.installModule(new UtilTestModule());
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