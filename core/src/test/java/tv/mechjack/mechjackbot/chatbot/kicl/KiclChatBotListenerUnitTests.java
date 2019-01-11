package tv.mechjack.mechjackbot.chatbot.kicl;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import tv.mechjack.configuration.TestConfigurationModule;
import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.ChatMessageEventHandler;
import tv.mechjack.mechjackbot.api.TestCommandModule;
import tv.mechjack.testframework.TestFrameworkRule;
import tv.mechjack.util.TestUtilModule;

public class KiclChatBotListenerUnitTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  private void installModules() {
    this.testFrameworkRule.installModule(new TestCommandModule());
    this.testFrameworkRule.installModule(new TestConfigurationModule());
    this.testFrameworkRule.installModule(new TestKiclChatBotModule());
    this.testFrameworkRule.installModule(new TestUtilModule());
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
