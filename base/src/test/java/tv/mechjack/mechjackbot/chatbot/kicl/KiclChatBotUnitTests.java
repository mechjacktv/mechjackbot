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
import tv.mechjack.testframework.fake.methodhandler.CountingMethodInvocationHandler;
import tv.mechjack.testframework.fake.methodhandler.ValidatingMethodInvocationHandler;

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
  public final void sendMessage_isCalled_sendsTheFormattedMessage() {
    this.installModules();
    final String messageFormat = this.testFrameworkRule.getArbitraryString() + "%s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(KEY_CHAT_BOT_MESSAGE_FORMAT, messageFormat);
    final ChatChannel chatChannel = ChatChannel.of(this.testFrameworkRule.getArbitraryString());
    final ChatMessage chatMessage = ChatMessage.of(this.testFrameworkRule.getArbitraryString());
    final FakeBuilder<Client> fakeBuilder = this.testFrameworkRule.fakeBuilder(Client.class);
    final ValidatingMethodInvocationHandler validatingHandler = new ValidatingMethodInvocationHandler(
        invocation -> invocation.getArgument(0).equals(chatChannel.value)
            && invocation.getArgument(1).equals(String.format(messageFormat, chatMessage)));
    fakeBuilder.forMethod("sendMessage", new Class[] { String.class, String.class })
        .addHandler(validatingHandler);
    final KiclChatBot subjectUnderTest = this.givenASubjectToTest(fakeBuilder.build());

    subjectUnderTest.sendMessage(chatChannel, chatMessage);

    assertThat(validatingHandler.isValid()).isTrue();
  }

  @Test
  public final void stop_whenCalled_callsStopOnKicl() {
    this.installModules();
    final String shutdownMessage = this.testFrameworkRule.getArbitraryString();
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(KEY_SHUTDOWN_MESSAGE, shutdownMessage);
    final FakeBuilder<Client> fakeBuilder = this.testFrameworkRule.fakeBuilder(Client.class);
    final ValidatingMethodInvocationHandler validatingHandler = new ValidatingMethodInvocationHandler(
        invocation -> invocation.getArgument(0).equals(shutdownMessage));
    fakeBuilder.forMethod("shutdown", new Class[] { String.class })
        .addHandler(validatingHandler);
    final KiclChatBot subjectUnderTest = this.givenASubjectToTest(fakeBuilder.build());

    subjectUnderTest.stop();

    assertThat(validatingHandler.isValid()).isTrue();
  }

}
