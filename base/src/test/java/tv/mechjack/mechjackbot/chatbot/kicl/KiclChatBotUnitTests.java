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
import tv.mechjack.testframework.TestFramework;
import tv.mechjack.testframework.fake.ArgumentCaptor;
import tv.mechjack.testframework.fake.FakeBuilder;
import tv.mechjack.testframework.fake.InvocationCounter;

public class KiclChatBotUnitTests {

  @Rule
  public final TestFramework testFrameworkRule = new TestFramework();

  private void installModules() {
    this.testFrameworkRule.registerModule(new TestCommandModule());
    this.testFrameworkRule.registerModule(new TestConfigurationModule());
    this.testFrameworkRule.registerModule(new TestKiclChatBotModule());
    this.testFrameworkRule.registerModule(new TestUtilsModule());
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
    final InvocationCounter countingHandler = new InvocationCounter();
    fakeBuilder.forMethod("connect").setHandler(countingHandler);
    final KiclChatBot subjectUnderTest = this.givenASubjectToTest(fakeBuilder.build());

    subjectUnderTest.start();

    assertThat(countingHandler.getCallCount()).isOne();
  }

  @Test
  public final void sendMessage_nullChannel_throwsNullPointerExceptionWithMessage() {
    this.installModules();
    final KiclChatBot subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.sendMessage(null,
        ChatMessage.of(this.testFrameworkRule.arbitraryData().getString())));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.testFrameworkRule.getInstance(ExecutionUtils.class)
            .nullMessageForName("chatChannel"));
  }

  @Test
  public final void sendMessage_nullMessage_throwsNullPointerExceptionWithMessage() {
    this.installModules();
    final KiclChatBot subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.sendMessage(
        ChatChannel.of(this.testFrameworkRule.arbitraryData().getString()), null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.testFrameworkRule.getInstance(ExecutionUtils.class)
            .nullMessageForName("chatMessage"));
  }

  @Test
  public final void sendMessage_isCalled_resultIsTheExpectChannel() {
    this.installModules();
    final String messageFormat = this.testFrameworkRule.arbitraryData().getString() + "%s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(KEY_CHAT_BOT_MESSAGE_FORMAT, messageFormat);
    final FakeBuilder<Client> fakeBuilder = this.testFrameworkRule.fakeBuilder(Client.class);
    final ArgumentCaptor capturingHandler = new ArgumentCaptor(0);
    fakeBuilder.forMethod("sendMessage", new Class[] { String.class, String.class })
        .setHandler(capturingHandler);
    final KiclChatBot subjectUnderTest = this.givenASubjectToTest(fakeBuilder.build());

    final ChatChannel chatChannel = ChatChannel.of(this.testFrameworkRule.arbitraryData().getString());
    subjectUnderTest.sendMessage(chatChannel, ChatMessage.of(this.testFrameworkRule.arbitraryData().getString()));

    final String actual = capturingHandler.getValue();
    assertThat(actual).isEqualTo(chatChannel.value);
  }

  @Test
  public final void sendMessage_isCalled_resultIsTheExpectMessage() {
    this.installModules();
    final String messageFormat = this.testFrameworkRule.arbitraryData().getString() + "%s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(KEY_CHAT_BOT_MESSAGE_FORMAT, messageFormat);
    final FakeBuilder<Client> fakeBuilder = this.testFrameworkRule.fakeBuilder(Client.class);
    final ArgumentCaptor capturingHandler = new ArgumentCaptor(1);
    fakeBuilder.forMethod("sendMessage", new Class[] { String.class, String.class })
        .setHandler(capturingHandler);
    final KiclChatBot subjectUnderTest = this.givenASubjectToTest(fakeBuilder.build());

    final ChatMessage chatMessage = ChatMessage.of(this.testFrameworkRule.arbitraryData().getString());
    subjectUnderTest.sendMessage(ChatChannel.of(this.testFrameworkRule.arbitraryData().getString()), chatMessage);

    final String actual = capturingHandler.getValue();
    assertThat(actual).isEqualTo(String.format(messageFormat, chatMessage));
  }

  @Test
  public final void stop_whenCalled_callsStopOnKicl() {
    this.installModules();
    final String shutdownMessage = this.testFrameworkRule.arbitraryData().getString();
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(KEY_SHUTDOWN_MESSAGE, shutdownMessage);
    final FakeBuilder<Client> fakeBuilder = this.testFrameworkRule.fakeBuilder(Client.class);
    final ArgumentCaptor capturingHandler = new ArgumentCaptor(0);
    fakeBuilder.forMethod("shutdown", new Class[] { String.class }).setHandler(capturingHandler);
    final KiclChatBot subjectUnderTest = this.givenASubjectToTest(fakeBuilder.build());

    subjectUnderTest.stop();

    final String actual = capturingHandler.getValue();
    assertThat(actual).isEqualTo(shutdownMessage);
  }

}
