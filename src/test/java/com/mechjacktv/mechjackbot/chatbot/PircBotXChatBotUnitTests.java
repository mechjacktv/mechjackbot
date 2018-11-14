package com.mechjacktv.mechjackbot.chatbot;

import java.io.IOException;
import java.util.function.Function;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.Listener;
import org.pircbotx.output.OutputIRC;

import com.mechjacktv.ArbitraryDataGenerator;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.ExecutionUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public class PircBotXChatBotUnitTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();
  private final ExecutionUtils executionUtils = new DefaultExecutionUtils();

  private PircBotXChatBot givenIHaveASubjectToTest(final PircBotX pircBotX) {
    return this.givenIHaveASubjectToTest(mock(AppConfiguration.class), pircBotX);
  }

  @SuppressWarnings("unchecked")
  private PircBotXChatBot givenIHaveASubjectToTest(final AppConfiguration appConfiguration, final PircBotX pircBotX) {
    final Function<Configuration, PircBotX> botFactory = mock(Function.class);

    when(botFactory.apply(isA(Configuration.class))).thenReturn(pircBotX);
    return this.givenIHaveASubjectToTest(appConfiguration, this.givenIHaveAFakeChatBotConfiguration(),
        mock(Listener.class), botFactory);
  }

  private PircBotXChatBot givenIHaveASubjectToTest(final AppConfiguration appConfiguration,
      final ChatBotConfiguration chatBotConfiguration, final Listener listener,
      final Function<Configuration, PircBotX> botFactory) {
    return new PircBotXChatBot(appConfiguration, chatBotConfiguration, this.executionUtils, listener,
        botFactory);
  }

  private ChatBotConfiguration givenIHaveAFakeChatBotConfiguration() {
    final ChatBotConfiguration chatBotConfiguration = mock(ChatBotConfiguration.class);
    final TwitchUsername twitchUsername = TwitchUsername.of(this.arbitraryDataGenerator.getString());
    final TwitchPassword twitchPassword = TwitchPassword.of(this.arbitraryDataGenerator.getString());
    final TwitchChannel twitchChannel = TwitchChannel.of(this.arbitraryDataGenerator.getString());

    when(chatBotConfiguration.getTwitchUsername()).thenReturn(twitchUsername);
    when(chatBotConfiguration.getTwitchPassword()).thenReturn(twitchPassword);
    when(chatBotConfiguration.getTwitchChannel()).thenReturn(twitchChannel);
    return chatBotConfiguration;
  }

  @Test
  @SuppressWarnings("unchecked")
  public final void new_withConfiguration_configuresPircBotXCorrectly() {
    final ChatBotConfiguration chatBotConfiguration = givenIHaveAFakeChatBotConfiguration();
    final Listener listener = mock(Listener.class);
    final Function<Configuration, PircBotX> botFactory = mock(Function.class);
    final ArgumentCaptor<Configuration> configurationArgumentCaptor = ArgumentCaptor.forClass(Configuration.class);
    when(botFactory.apply(configurationArgumentCaptor.capture())).thenReturn(mock(PircBotX.class));
    this.givenIHaveASubjectToTest(mock(AppConfiguration.class), chatBotConfiguration, listener, botFactory);

    final Configuration result = configurationArgumentCaptor.getValue();

    final SoftAssertions softly = new SoftAssertions();
    final Configuration.ServerEntry serverEntry = result.getServers().get(0);
    softly.assertThat(result.getName()).isEqualTo(chatBotConfiguration.getTwitchUsername().value);
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
  public final void start_startBotThrowsIOException_throwsPircBotXStartupException() throws IOException, IrcException {
    final PircBotX pircBotX = mock(PircBotX.class);
    doThrow(IOException.class).when(pircBotX).startBot();
    final PircBotXChatBot subjectUnderTest = this.givenIHaveASubjectToTest(pircBotX);

    final Throwable thrown = catchThrowable(subjectUnderTest::start);

    assertThat(thrown).isInstanceOf(PircBotXStartupException.class).hasCauseInstanceOf(IOException.class);
  }

  @Test
  public final void start_startBotThrowsIrcException_throwsPircBotXStartupException() throws IOException, IrcException {
    final PircBotX pircBotX = mock(PircBotX.class);
    doThrow(IrcException.class).when(pircBotX).startBot();
    final PircBotXChatBot subjectUnderTest = this.givenIHaveASubjectToTest(pircBotX);

    final Throwable thrown = catchThrowable(subjectUnderTest::start);

    assertThat(thrown).isInstanceOf(PircBotXStartupException.class).hasCauseInstanceOf(IrcException.class);
  }

  @Test
  public final void stop_whenCalled_callsStopOnPircBotX() {
    final String shutdownMessage = this.arbitraryDataGenerator.getString();
    final AppConfiguration appConfiguration = mock(AppConfiguration.class);
    final PircBotX pircBotX = mock(PircBotX.class);
    final OutputIRC outputIRC = mock(OutputIRC.class);
    when(pircBotX.sendIRC()).thenReturn(outputIRC);
    when(appConfiguration.get(eq(PircBotXChatBot.SHUTDOWN_MESSAGE_KEY), isA(String.class))).thenReturn(shutdownMessage);
    final PircBotXChatBot subjectUnderTest = this.givenIHaveASubjectToTest(appConfiguration, pircBotX);

    subjectUnderTest.stop();

    verify(pircBotX).stopBotReconnect();
    verify(outputIRC).quitServer(eq(shutdownMessage));
  }

}
