package com.mechjacktv.mechjackbot.chatbot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.function.Function;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.Listener;
import org.pircbotx.output.OutputIRC;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.Message;
import com.mechjacktv.mechjackbot.TwitchChannel;
import com.mechjacktv.mechjackbot.TwitchPassword;
import com.mechjacktv.testframework.ArbitraryDataGenerator;
import com.mechjacktv.twitchclient.TwitchLogin;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.ExecutionUtils;

public class PircBotXChatBotUnitTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();
  private final ExecutionUtils executionUtils = new DefaultExecutionUtils();

  private PircBotXChatBot givenIHaveASubjectToTest(final PircBotX pircBotX) {
    return this.givenIHaveASubjectToTest(mock(Configuration.class), pircBotX);
  }

  @SuppressWarnings("unchecked")
  private PircBotXChatBot givenIHaveASubjectToTest(final Configuration configuration, final PircBotX pircBotX) {
    final Function<org.pircbotx.Configuration, PircBotX> botFactory = mock(Function.class);

    when(botFactory.apply(isA(org.pircbotx.Configuration.class))).thenReturn(pircBotX);
    return this.givenIHaveASubjectToTest(configuration, this.givenIHaveAFakeChatBotConfiguration(),
        mock(Listener.class), botFactory);
  }

  private PircBotXChatBot givenIHaveASubjectToTest(final Configuration configuration,
      final ChatBotConfiguration chatBotConfiguration, final Listener listener,
      final Function<org.pircbotx.Configuration, PircBotX> botFactory) {
    return new PircBotXChatBot(configuration, chatBotConfiguration, this.executionUtils, listener,
        botFactory);
  }

  private ChatBotConfiguration givenIHaveAFakeChatBotConfiguration() {
    final ChatBotConfiguration chatBotConfiguration = mock(ChatBotConfiguration.class);
    final TwitchLogin twitchUsername = TwitchLogin.of(this.arbitraryDataGenerator.getString());
    final TwitchPassword twitchPassword = TwitchPassword.of(this.arbitraryDataGenerator.getString());
    final TwitchChannel twitchChannel = TwitchChannel.of(this.arbitraryDataGenerator.getString());

    when(chatBotConfiguration.getTwitchLogin()).thenReturn(twitchUsername);
    when(chatBotConfiguration.getTwitchPassword()).thenReturn(twitchPassword);
    when(chatBotConfiguration.getTwitchChannel()).thenReturn(twitchChannel);
    return chatBotConfiguration;
  }

  @Test
  @SuppressWarnings("unchecked")
  public final void new_withConfiguration_configuresPircBotXCorrectly() {
    final ChatBotConfiguration chatBotConfiguration = this.givenIHaveAFakeChatBotConfiguration();
    final Listener listener = mock(Listener.class);
    final Function<org.pircbotx.Configuration, PircBotX> botFactory = mock(Function.class);
    final ArgumentCaptor<org.pircbotx.Configuration> configurationArgumentCaptor = ArgumentCaptor.forClass(
        org.pircbotx.Configuration.class);
    when(botFactory.apply(configurationArgumentCaptor.capture())).thenReturn(mock(PircBotX.class));
    this.givenIHaveASubjectToTest(mock(Configuration.class), chatBotConfiguration, listener, botFactory);

    final org.pircbotx.Configuration result = configurationArgumentCaptor.getValue();

    final SoftAssertions softly = new SoftAssertions();
    final org.pircbotx.Configuration.ServerEntry serverEntry = result.getServers().get(0);
    softly.assertThat(result.getName()).isEqualTo(chatBotConfiguration.getTwitchLogin().value);
    softly.assertThat(serverEntry.getHostname()).isEqualTo(PircBotXChatBot.TWITCH_IRC_SERVER_HOST);
    softly.assertThat(serverEntry.getPort()).isEqualTo(PircBotXChatBot.TWITCH_IRC_SERVER_PORT);
    softly.assertThat(result.getServerPassword()).isEqualTo(chatBotConfiguration.getTwitchPassword().value);
    softly.assertThat(result.getListenerManager().getListeners()).contains(listener);
    softly.assertThat(result.getAutoJoinChannels()).containsKey("#" + chatBotConfiguration.getTwitchChannel().value);
    softly.assertAll();
  }

  @Test
  public final void start_whenCalled_callsStartOnPircBotX() throws IOException, IrcException {
    final PircBotX pircBotX = mock(PircBotX.class);
    final PircBotXChatBot subjectUnderTest = this.givenIHaveASubjectToTest(pircBotX);

    subjectUnderTest.start();

    verify(pircBotX).startBot();
  }

  @Test
  public final void start_startBotThrowsIOException_throwsChatBotStartupException() throws IOException, IrcException {
    final PircBotX pircBotX = mock(PircBotX.class);
    doThrow(IOException.class).when(pircBotX).startBot();
    final PircBotXChatBot subjectUnderTest = this.givenIHaveASubjectToTest(pircBotX);

    final Throwable thrown = catchThrowable(subjectUnderTest::start);

    assertThat(thrown).isInstanceOf(ChatBotStartupException.class).hasCauseInstanceOf(IOException.class);
  }

  @Test
  public final void start_startBotThrowsIrcException_throwsChatBotStartupException() throws IOException, IrcException {
    final PircBotX pircBotX = mock(PircBotX.class);
    doThrow(IrcException.class).when(pircBotX).startBot();
    final PircBotXChatBot subjectUnderTest = this.givenIHaveASubjectToTest(pircBotX);

    final Throwable thrown = catchThrowable(subjectUnderTest::start);

    assertThat(thrown).isInstanceOf(ChatBotStartupException.class).hasCauseInstanceOf(IrcException.class);
  }

  @Test
  public final void sendMessage_nullChannel_throwsNullPointerExceptionWithMessage() {
    final PircBotXChatBot subjectUnderTest = this.givenIHaveASubjectToTest(mock(PircBotX.class));

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.sendMessage(null,
        Message.of(this.arbitraryDataGenerator.getString())));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("channel"));
  }

  @Test
  public final void sendMessage_nullMessage_throwsNullPointerExceptionWithMessage() {
    final PircBotXChatBot subjectUnderTest = this.givenIHaveASubjectToTest(mock(PircBotX.class));

    final Throwable thrown = catchThrowable(() -> subjectUnderTest.sendMessage(
        TwitchChannel.of(this.arbitraryDataGenerator.getString()), null));

    assertThat(thrown).isInstanceOf(NullPointerException.class)
        .hasMessage(this.executionUtils.nullMessageForName("message"));
  }

  @Test
  public final void sendMessage_isCalled_sendsTheFormattedMessage() {
    final String messageFormat = "Formatted %s";
    final Configuration configuration = mock(Configuration.class);
    when(configuration.get(eq(PircBotXChatBot.CHAT_BOT_MESSAGE_FORMAT_KEY),
        eq(PircBotXChatBot.CHAT_BOT_MESSAGE_FORMAT_DEFAULT))).thenReturn(messageFormat);
    final PircBotX pircBotX = mock(PircBotX.class);
    final OutputIRC outputIRC = mock(OutputIRC.class);
    when(pircBotX.sendIRC()).thenReturn(outputIRC);
    final PircBotXChatBot subjectUnderTest = this.givenIHaveASubjectToTest(configuration, pircBotX);
    final TwitchChannel channel = TwitchChannel.of(this.arbitraryDataGenerator.getString());
    final Message message = Message.of(this.arbitraryDataGenerator.getString());

    subjectUnderTest.sendMessage(channel, message);

    verify(outputIRC).message(eq(channel.value), eq(String.format(messageFormat, message)));
  }

  @Test
  public final void stop_whenCalled_callsStopOnPircBotX() {
    final String shutdownMessage = this.arbitraryDataGenerator.getString();
    final Configuration configuration = mock(Configuration.class);
    final PircBotX pircBotX = mock(PircBotX.class);
    final OutputIRC outputIRC = mock(OutputIRC.class);
    when(pircBotX.sendIRC()).thenReturn(outputIRC);
    when(configuration.get(eq(PircBotXChatBot.SHUTDOWN_MESSAGE_KEY), isA(String.class))).thenReturn(shutdownMessage);
    final PircBotXChatBot subjectUnderTest = this.givenIHaveASubjectToTest(configuration, pircBotX);

    subjectUnderTest.stop();

    verify(pircBotX).stopBotReconnect();
    verify(outputIRC).quitServer(eq(shutdownMessage));
  }

}
