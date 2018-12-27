package tv.mechjack.mechjackbot.chatbot.kicl;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import tv.mechjack.configuration.ConfigurationTestModule;
import tv.mechjack.mechjackbot.ChatMessageEvent;
import tv.mechjack.mechjackbot.ChatMessageEventHandler;
import tv.mechjack.mechjackbot.command.CommandTestModule;
import tv.mechjack.testframework.TestFrameworkRule;
import tv.mechjack.util.UtilTestModule;

public class KiclChatBotListenerUnitTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  private void installModules() {
    this.testFrameworkRule.installModule(new CommandTestModule());
    this.testFrameworkRule.installModule(new ConfigurationTestModule());
    this.testFrameworkRule.installModule(new KiclChatBotTestModule());
    this.testFrameworkRule.installModule(new UtilTestModule());
  }

  private KiclChatBotListener givenASubjectToTest() {
    return this.testFrameworkRule.getInstance(KiclChatBotListener.class);
  }

  @Test
  public final void onChannelMessageEvent_isCalled_resultIsForwardsCallToChatMessageEventHandler() {
    this.installModules();
    final KiclChatBotListener subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.onChannelMessageEvent(this.testFrameworkRule.getInstance(ChannelMessageEvent.class));

    verify(this.testFrameworkRule.getInstance(ChatMessageEventHandler.class))
        .handleMessageEvent(isA(ChatMessageEvent.class));
  }

}
