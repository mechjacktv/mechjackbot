package com.mechjacktv.mechjackbot.chatbot;

import static com.mechjacktv.mechjackbot.chatbot.PircBotXChatBot.CHAT_BOT_MESSAGE_FORMAT_DEFAULT;
import static com.mechjacktv.mechjackbot.chatbot.PircBotXChatBot.CHAT_BOT_MESSAGE_FORMAT_KEY;
import static com.mechjacktv.mechjackbot.chatbot.PircBotXListener.JOIN_EVENT_MESSAGE_DEFAULT;
import static com.mechjacktv.mechjackbot.chatbot.PircBotXListener.JOIN_EVENT_MESSAGE_KEY;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.pircbotx.output.OutputIRC;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.DefaultCommandUtils;
import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.DefaultTimeUtils;
import com.mechjacktv.util.ExecutionUtils;

public class PircBotXListenerUnitTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();
  private final ExecutionUtils executionUtils = new DefaultExecutionUtils();

  private PircBotXListener givenASubjectToTest() {
    return this.givenASubjectToTest(mock(Configuration.class), mock(MessageEventHandler.class));
  }

  private PircBotXListener givenASubjectToTest(final MessageEventHandler messageEventHandler) {
    return this.givenASubjectToTest(mock(Configuration.class), messageEventHandler);
  }

  private PircBotXListener givenASubjectToTest(final Configuration configuration) {
    return this.givenASubjectToTest(configuration, mock(MessageEventHandler.class));
  }

  private PircBotXListener givenASubjectToTest(final Configuration configuration,
      final MessageEventHandler messageEventHandler) {
    final PircBotXChatBotFactory chatBotFactory = new PircBotXChatBotFactory(configuration, this.executionUtils);
    final ChatBotConfiguration chatBotConfiguration = new ArbitraryChatBotConfiguration(this.arbitraryDataGenerator);
    final CommandUtils commandUtils = new DefaultCommandUtils(configuration, this.executionUtils,
        new DefaultTimeUtils());
    final PircBotXMessageEventFactory messageEventFactory = new PircBotXMessageEventFactory(configuration,
        chatBotConfiguration, chatBotFactory, commandUtils, this.executionUtils);

    return new PircBotXListener(configuration, chatBotFactory, messageEventFactory, messageEventHandler);
  }

  @Test
  public final void onPing_isCalled_respondsWithExpectedPingValue() {
    final PircBotXListener subjectUnderTest = this.givenASubjectToTest();
    final PingEvent pingEvent = mock(PingEvent.class);
    final String pingValue = this.arbitraryDataGenerator.getString();
    when(pingEvent.getPingValue()).thenReturn(pingValue);

    subjectUnderTest.onPing(pingEvent);

    verify(pingEvent).respond(eq(String.format("PONG %s", pingValue)));
  }

  @Test
  public final void onGenericMessageEvent_forEvent_callsMessageEventHandler() {
    final MessageEventHandler messageEventHandler = mock(MessageEventHandler.class);
    final PircBotXListener subjectUnderTest = this.givenASubjectToTest(messageEventHandler);

    subjectUnderTest.onGenericMessage(mock(GenericMessageEvent.class));

    verify(messageEventHandler).handleMessageEvent(isA(MessageEvent.class));
  }

  @Test
  public final void onJoin_isCalled_sendsWithExpectedJoinMessage() {
    final Configuration configuration = mock(Configuration.class);
    final String joinMessage = this.arbitraryDataGenerator.getString();
    when(configuration.get(eq(JOIN_EVENT_MESSAGE_KEY), eq(JOIN_EVENT_MESSAGE_DEFAULT)))
        .thenReturn(joinMessage);
    when(configuration.get(eq(CHAT_BOT_MESSAGE_FORMAT_KEY), eq(CHAT_BOT_MESSAGE_FORMAT_DEFAULT))).thenReturn("%s");
    final PircBotXListener subjectUnderTest = this.givenASubjectToTest(configuration);
    final JoinEvent joinEvent = mock(JoinEvent.class);
    final Channel channel = mock(Channel.class);
    final String channelName = this.arbitraryDataGenerator.getString();
    when(joinEvent.getChannel()).thenReturn(channel);
    when(channel.getName()).thenReturn(channelName);
    final PircBotX pircBotX = mock(PircBotX.class);
    final OutputIRC outputIRC = mock(OutputIRC.class);
    when(joinEvent.getBot()).thenReturn(pircBotX);
    when(pircBotX.sendIRC()).thenReturn(outputIRC);

    subjectUnderTest.onJoin(joinEvent);

    verify(outputIRC).message(eq(channelName), eq(joinMessage));
  }

}
