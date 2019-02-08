package tv.mechjack.mechjackbot.chatbot.kicl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static tv.mechjack.mechjackbot.chatbot.kicl.KiclChatBot.KEY_CHAT_BOT_MESSAGE_FORMAT;
import static tv.mechjack.mechjackbot.chatbot.kicl.KiclChatBot.KEY_SHUTDOWN_MESSAGE;

import org.junit.Rule;
import org.junit.Test;
import org.kitteh.irc.client.library.Client;

import tv.mechjack.mechjackbot.api.ChatChannel;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.TestCommandModule;
import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.configuration.MapConfiguration;
import tv.mechjack.platform.configuration.TestConfigurationModule;
import tv.mechjack.platform.utils.ExecutionUtils;
import tv.mechjack.platform.utils.TestUtilsModule;
import tv.mechjack.testframework.TestFrameworkRule;
import tv.mechjack.testframework.fake.FakeBuilder;
import tv.mechjack.testframework.fake.methodhandler.CapturingMethodInvocationHandler;
import tv.mechjack.testframework.fake.methodhandler.CountingMethodInvocationHandler;

public class KiclChatBotUnitTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  private void installModules() {
    this.testFrameworkRule.installModule(new TestCommandModule());
    this.testFrameworkRule.installModule(new TestConfigurationModule());
    this.testFrameworkRule.installModule(new TestKiclChatBotModule());
    this.testFrameworkRule.installModule(new TestUtilsModule());
  }

  private KiclChatBot givenASubjectToTest() {
    return this.givenASubjectToTest(this.testFrameworkRule.getInstance(Client.class));
  }

  private KiclChatBot givenASubjectToTest(final Client client) {
    return new KiclChatBot(this.testFrameworkRule.getInstance(Configuration.class),
        this.testFrameworkRule.getInstance(ExecutionUtils.class), client);
  }

  @Test
  public final void start_whenCalled_callsConnectOnClient() {
    this.installModules();
    final FakeBuilder<Client> fakeBuilder = this.testFrameworkRule.fakeBuilder(Client.class);
    final CountingMethodInvocationHandler countingHandler = new CountingMethodInvocationHandler();
    fakeBuilder.forMethod("connect").addHandler(countingHandler);
    final KiclChatBot subjectUnderTest = this.givenASubjectToTest(fakeBuilder.build());

    subjectUnderTest.start();

    assertThat(countingHandler.getCallCount()).isOne();
  }

  @Test
  public final void sendMessage_nullChannel_throwsNullPointerExceptionWithMessage() {
    this.installModules();
    final KiclChatBot subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.sendMessage(null,
        ChatMessage.of(this.testFrameworkRule.getArbitraryString())));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatChannel");
  }

  @Test
  public final void sendMessage_nullMessage_throwsNullPointerExceptionWithMessage() {
    this.installModules();
    final KiclChatBot subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.sendMessage(
        ChatChannel.of(this.testFrameworkRule.getArbitraryString()), null));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatMessage");
  }

  @Test
  public final void sendMessage_isCalled_resultIsTheExpectChannel() {
    this.installModules();
    final String messageFormat = this.testFrameworkRule.getArbitraryString() + "%s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(KEY_CHAT_BOT_MESSAGE_FORMAT, messageFormat);
    final FakeBuilder<Client> fakeBuilder = this.testFrameworkRule.fakeBuilder(Client.class);
    final CapturingMethodInvocationHandler capturingHandler = new CapturingMethodInvocationHandler(0);
    fakeBuilder.forMethod("sendMessage", new Class[] { String.class, String.class })
        .addHandler(capturingHandler);
    final KiclChatBot subjectUnderTest = this.givenASubjectToTest(fakeBuilder.build());

    final ChatChannel chatChannel = ChatChannel.of(this.testFrameworkRule.getArbitraryString());
    subjectUnderTest.sendMessage(chatChannel, ChatMessage.of(this.testFrameworkRule.getArbitraryString()));

    final String actual = capturingHandler.getValue();
    assertThat(actual).isEqualTo(chatChannel.value);
  }

  @Test
  public final void sendMessage_isCalled_resultIsTheExpectMessage() {
    this.installModules();
    final String messageFormat = this.testFrameworkRule.getArbitraryString() + "%s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(KEY_CHAT_BOT_MESSAGE_FORMAT, messageFormat);
    final FakeBuilder<Client> fakeBuilder = this.testFrameworkRule.fakeBuilder(Client.class);
    final CapturingMethodInvocationHandler capturingHandler = new CapturingMethodInvocationHandler(1);
    fakeBuilder.forMethod("sendMessage", new Class[] { String.class, String.class })
        .addHandler(capturingHandler);
    final KiclChatBot subjectUnderTest = this.givenASubjectToTest(fakeBuilder.build());

    final ChatMessage chatMessage = ChatMessage.of(this.testFrameworkRule.getArbitraryString());
    subjectUnderTest.sendMessage(ChatChannel.of(this.testFrameworkRule.getArbitraryString()), chatMessage);

    final String actual = capturingHandler.getValue();
    assertThat(actual).isEqualTo(String.format(messageFormat, chatMessage));
  }

  @Test
  public final void stop_whenCalled_callsStopOnKicl() {
    this.installModules();
    final String shutdownMessage = this.testFrameworkRule.getArbitraryString();
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(KEY_SHUTDOWN_MESSAGE, shutdownMessage);
    final FakeBuilder<Client> fakeBuilder = this.testFrameworkRule.fakeBuilder(Client.class);
    final CapturingMethodInvocationHandler capturingHandler = new CapturingMethodInvocationHandler(0);
    fakeBuilder.forMethod("shutdown", new Class[] { String.class }).addHandler(capturingHandler);
    final KiclChatBot subjectUnderTest = this.givenASubjectToTest(fakeBuilder.build());

    subjectUnderTest.stop();

    final String actual = capturingHandler.getValue();
    assertThat(actual).isEqualTo(shutdownMessage);
  }

}
