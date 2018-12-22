package com.mechjacktv.mechjackbot.chatbot.pircbotx;

import java.io.IOException;
import java.util.function.Function;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.ConfigurationTestModule;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.ChatChannel;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.mechjackbot.chatbot.ChatBotStartupException;
import com.mechjacktv.mechjackbot.chatbot.ChatBotTestModule;
import com.mechjacktv.mechjackbot.command.CommandTestModule;
import com.mechjacktv.testframework.TestFrameworkRule;
import com.mechjacktv.util.ExecutionUtils;
import com.mechjacktv.util.UtilTestModule;

import org.assertj.core.api.SoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.Listener;
import org.pircbotx.output.OutputIRC;

import static com.mechjacktv.mechjackbot.chatbot.pircbotx.PircBotXChatBot.CHAT_BOT_MESSAGE_FORMAT_KEY;
import static com.mechjacktv.mechjackbot.chatbot.pircbotx.PircBotXChatBot.SHUTDOWN_MESSAGE_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PircBotXChatBotUnitTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  private void installModules() {
    this.testFrameworkRule.installModule(new ChatBotTestModule());
    this.testFrameworkRule.installModule(new CommandTestModule());
    this.testFrameworkRule.installModule(new ConfigurationTestModule());
    this.testFrameworkRule.installModule(new PircBotXChatBotTestModule());
    this.testFrameworkRule.installModule(new UtilTestModule());
  }

  private PircBotXChatBot givenASubjectToTest() {
    return this.givenASubjectToTest(this.givenAFakePircBotX());
  }

  private PircBotXChatBot givenASubjectToTest(final PircBotX pircBotX) {
    return this.givenASubjectToTest(mock(Listener.class), pircBotX);
  }

  private PircBotXChatBot givenASubjectToTest(final Listener listener, final PircBotX pircBotX) {
    return this.givenASubjectToTest(listener, configuration -> pircBotX);
  }

  private PircBotXChatBot givenASubjectToTest(final Listener listener,
      final Function<org.pircbotx.Configuration, PircBotX> botFactory) {
    return new PircBotXChatBot(this.testFrameworkRule.getInstance(Configuration.class),
        this.testFrameworkRule.getInstance(ChatBotConfiguration.class),
        this.testFrameworkRule.getInstance(ExecutionUtils.class),
        listener, botFactory);
  }

  private PircBotX givenAFakePircBotX() {
    return this.givenAFakePircBotX(mock(OutputIRC.class));
  }

  private PircBotX givenAFakePircBotX(final OutputIRC outputIRC) {
    final PircBotX pircBotX = mock(PircBotX.class);

    when(pircBotX.sendIRC()).thenReturn(outputIRC);
    return pircBotX;
  }

  @Test
  @SuppressWarnings("unchecked")
  public final void new_withConfiguration_configuresPircBotXCorrectly() {
    this.installModules();
    final Listener listener = mock(Listener.class);
    final PircBotX pircBotX = this.givenAFakePircBotX();
    final Function<org.pircbotx.Configuration, PircBotX> botFactory = mock(Function.class);
    final ArgumentCaptor<org.pircbotx.Configuration> configurationArgumentCaptor = ArgumentCaptor.forClass(
        org.pircbotx.Configuration.class);
    when(botFactory.apply(configurationArgumentCaptor.capture())).thenReturn(pircBotX);
    this.givenASubjectToTest(listener, botFactory);

    final org.pircbotx.Configuration result = configurationArgumentCaptor.getValue();

    final ChatBotConfiguration chatBotConfiguration = this.testFrameworkRule.getInstance(ChatBotConfiguration.class);
    final SoftAssertions softly = new SoftAssertions();
    final org.pircbotx.Configuration.ServerEntry serverEntry = result.getServers().get(0);
    softly.assertThat(result.getName()).isEqualTo(chatBotConfiguration.getTwitchLogin().value);
    softly.assertThat(serverEntry.getHostname()).isEqualTo(PircBotXChatBot.TWITCH_IRC_SERVER_HOST);
    softly.assertThat(serverEntry.getPort()).isEqualTo(PircBotXChatBot.TWITCH_IRC_SERVER_PORT);
    softly.assertThat(result.getServerPassword()).isEqualTo(chatBotConfiguration.getUserPassword().value);
    softly.assertThat(result.getListenerManager().getListeners()).contains(listener);
    softly.assertThat(result.getAutoJoinChannels()).containsKey("#" + chatBotConfiguration.getChatChannel().value);
    softly.assertAll();
  }

  @Test
  public final void start_whenCalled_callsStartOnPircBotX() throws IOException, IrcException {
    this.installModules();
    final PircBotX pircBotX = this.givenAFakePircBotX();
    final PircBotXChatBot subjectUnderTest = this.givenASubjectToTest(pircBotX);

    subjectUnderTest.start();

    verify(pircBotX).startBot();
  }

  @Test
  public final void start_startBotThrowsIOException_throwsChatBotStartupException() throws IOException, IrcException {
    this.installModules();
    final PircBotX pircBotX = this.givenAFakePircBotX();
    doThrow(IOException.class).when(pircBotX).startBot();
    final PircBotXChatBot subjectUnderTest = this.givenASubjectToTest(pircBotX);

    final Throwable thrown = catchThrowable(subjectUnderTest::start);

    assertThat(thrown).isInstanceOf(ChatBotStartupException.class).hasCauseInstanceOf(IOException.class);
  }

  @Test
  public final void start_startBotThrowsIrcException_throwsChatBotStartupException() throws IOException, IrcException {
    this.installModules();
    final PircBotX pircBotX = this.givenAFakePircBotX();
    doThrow(IrcException.class).when(pircBotX).startBot();
    final PircBotXChatBot subjectUnderTest = this.givenASubjectToTest(pircBotX);

    final Throwable thrown = catchThrowable(subjectUnderTest::start);

    assertThat(thrown).isInstanceOf(ChatBotStartupException.class).hasCauseInstanceOf(IrcException.class);
  }

  @Test
  public final void sendMessage_nullChannel_throwsNullPointerExceptionWithMessage() {
    this.installModules();
    final PircBotXChatBot subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.sendMessage(null,
        ChatMessage.of(this.testFrameworkRule.getArbitraryString())));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatChannel");
  }

  @Test
  public final void sendMessage_nullMessage_throwsNullPointerExceptionWithMessage() {
    this.installModules();
    final PircBotXChatBot subjectUnderTest = this.givenASubjectToTest();

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.sendMessage(
        ChatChannel.of(this.testFrameworkRule.getArbitraryString()), null));

    this.testFrameworkRule.assertNullPointerException(thrown, "chatMessage");
  }

  @Test
  public final void sendMessage_isCalled_sendsTheFormattedMessage() {
    this.installModules();
    final String messageFormat = this.testFrameworkRule.getArbitraryString() + "%s";
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(CHAT_BOT_MESSAGE_FORMAT_KEY, messageFormat);
    final OutputIRC outputIRC = mock(OutputIRC.class);
    final ChatChannel chatChannel = ChatChannel.of(this.testFrameworkRule.getArbitraryString());
    final ChatMessage chatMessage = ChatMessage.of(this.testFrameworkRule.getArbitraryString());
    final PircBotXChatBot subjectUnderTest = this.givenASubjectToTest(this.givenAFakePircBotX(outputIRC));

    subjectUnderTest.sendMessage(chatChannel, chatMessage);

    verify(outputIRC).message(eq(chatChannel.value), eq(String.format(messageFormat, chatMessage)));
  }

  @Test
  public final void stop_whenCalled_callsStopOnPircBotX() {
    this.installModules();
    final String shutdownMessage = this.testFrameworkRule.getArbitraryString();
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    configuration.set(SHUTDOWN_MESSAGE_KEY, shutdownMessage);
    final OutputIRC outputIRC = mock(OutputIRC.class);
    final PircBotX pircBotX = this.givenAFakePircBotX(outputIRC);
    final PircBotXChatBot subjectUnderTest = this.givenASubjectToTest(pircBotX);

    subjectUnderTest.stop();

    verify(pircBotX).stopBotReconnect();
    verify(outputIRC).quitServer(eq(shutdownMessage));
  }

}
