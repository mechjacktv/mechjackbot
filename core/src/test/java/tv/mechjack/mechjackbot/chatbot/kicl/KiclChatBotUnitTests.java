package tv.mechjack.mechjackbot.chatbot.kicl;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static tv.mechjack.mechjackbot.chatbot.kicl.KiclChatBot.KEY_CHAT_BOT_MESSAGE_FORMAT;
import static tv.mechjack.mechjackbot.chatbot.kicl.KiclChatBot.KEY_SHUTDOWN_MESSAGE;

import org.junit.Rule;
import org.junit.Test;
import org.kitteh.irc.client.library.Client;

import tv.mechjack.mechjackbot.api.ChatChannel;
import tv.mechjack.mechjackbot.api.ChatMessage;
import tv.mechjack.mechjackbot.api.TestCommandModule;
import tv.mechjack.platform.configuration.MapConfiguration;
import tv.mechjack.platform.configuration.TestConfigurationModule;
import tv.mechjack.platform.util.TestUtilModule;
import tv.mechjack.testframework.TestFrameworkRule;

public class KiclChatBotUnitTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  private void installModules() {
    this.testFrameworkRule.installModule(new TestCommandModule());
    this.testFrameworkRule.installModule(new TestConfigurationModule());
    this.testFrameworkRule.installModule(new TestKiclChatBotModule());
    this.testFrameworkRule.installModule(new TestUtilModule());
  }

  private KiclChatBot givenASubjectToTest() {
    return this.testFrameworkRule.getInstance(KiclChatBot.class);
  }

  @Test
  public final void start_whenCalled_callsConnectOnClient() {
    this.installModules();
    final KiclChatBot subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.start();

    verify(this.testFrameworkRule.getInstance(Client.class)).connect();
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
    final KiclChatBot subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.sendMessage(chatChannel, chatMessage);

    verify(this.testFrameworkRule.getInstance(Client.class))
        .sendMessage(eq(chatChannel.value), eq(String.format(messageFormat, chatMessage)));
  }

  @Test
  public final void stop_whenCalled_callsStopOnKicl() {
    this.installModules();
    final String shutdownMessage = this.testFrameworkRule.getArbitraryString();
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(KEY_SHUTDOWN_MESSAGE, shutdownMessage);
    final KiclChatBot subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.stop();

    verify(this.testFrameworkRule.getInstance(Client.class)).shutdown(eq(shutdownMessage));
  }

}
