package tv.mechjack.mechjackbot.chatbot.kicl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import tv.mechjack.mechjackbot.api.ChatMessageEvent;
import tv.mechjack.mechjackbot.api.ChatMessageEventHandler;
import tv.mechjack.mechjackbot.api.TestCommandModule;
import tv.mechjack.platform.configuration.TestConfigurationModule;
import tv.mechjack.platform.utils.TestUtilsModule;
import tv.mechjack.testframework.TestFramework;
import tv.mechjack.testframework.fake.FakeBuilder;
import tv.mechjack.testframework.fake.InvocationCounter;

public class KiclChatBotListenerUnitTests {

  @Rule
  public final TestFramework testFrameworkRule = new TestFramework();

  private void installModules() {
    this.testFrameworkRule.registerModule(new TestCommandModule());
    this.testFrameworkRule.registerModule(new TestConfigurationModule());
    this.testFrameworkRule.registerModule(new TestKiclChatBotModule());
    this.testFrameworkRule.registerModule(new TestUtilsModule());
  }

  private KiclChatBotListener givenASubjectToTest(final ChatMessageEventHandler chatMessageEventHandler) {
    return new KiclChatBotListener(this.testFrameworkRule.getInstance(KiclChatMessageEventFactory.class),
        chatMessageEventHandler);
  }

  @Test
  public final void onChannelMessageEvent_isCalled_resultIsForwardsCallToChatMessageEventHandler() {
    this.installModules();
    final FakeBuilder<ChatMessageEventHandler> fakeBuilder = this.testFrameworkRule
        .fakeBuilder(ChatMessageEventHandler.class);
    final InvocationCounter countingHandler = new InvocationCounter();
    fakeBuilder.forMethod("handleMessageEvent", new Class[] { ChatMessageEvent.class })
        .addHandler(countingHandler);
    final ChatMessageEventHandler chatMessageEventHandler = fakeBuilder.build();
    final KiclChatBotListener subjectUnderTest = this.givenASubjectToTest(chatMessageEventHandler);

    subjectUnderTest.onChannelMessageEvent(this.testFrameworkRule.getInstance(ChannelMessageEvent.class));

    assertThat(countingHandler.getCallCount()).isOne();
  }

}
