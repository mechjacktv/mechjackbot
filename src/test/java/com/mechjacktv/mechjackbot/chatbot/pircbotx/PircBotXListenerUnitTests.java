package com.mechjacktv.mechjackbot.chatbot.pircbotx;

import static com.mechjacktv.mechjackbot.chatbot.pircbotx.PircBotXChatBot.CHAT_BOT_MESSAGE_FORMAT_KEY;
import static com.mechjacktv.mechjackbot.chatbot.pircbotx.PircBotXListener.JOIN_EVENT_MESSAGE_KEY;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;

import org.junit.Rule;
import org.junit.Test;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.pircbotx.output.OutputIRC;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.configuration.ConfigurationTestModule;
import com.mechjacktv.configuration.MapConfiguration;
import com.mechjacktv.mechjackbot.ChatChannel;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.mechjackbot.ChatMessageEvent;
import com.mechjacktv.mechjackbot.ChatMessageEventHandler;
import com.mechjacktv.mechjackbot.chatbot.ChatBotFactory;
import com.mechjacktv.mechjackbot.chatbot.ChatBotTestModule;
import com.mechjacktv.mechjackbot.chatbot.MessageEventFactory;
import com.mechjacktv.mechjackbot.command.CommandTestModule;
import com.mechjacktv.testframework.TestFrameworkRule;
import com.mechjacktv.util.UtilTestModule;

public class PircBotXListenerUnitTests {

  @Rule
  public final TestFrameworkRule testFrameworkRule = new TestFrameworkRule();

  private void installModules() {
    this.testFrameworkRule.installModule(new ChatBotTestModule());
    this.testFrameworkRule.installModule(new CommandTestModule());
    this.testFrameworkRule.installModule(new ConfigurationTestModule());
    this.testFrameworkRule.installModule(new PircBotXChatBotTestModule());
    this.testFrameworkRule.installModule(new UtilTestModule());
  }

  private PircBotXListener givenASubjectToTest() {
    return this.givenASubjectToTest(mock(ChatMessageEventHandler.class));
  }

  private PircBotXListener givenASubjectToTest(final ChatMessageEventHandler chatMessageEventHandler) {
    return new PircBotXListener(this.testFrameworkRule.getInstance(Configuration.class),
        this.testFrameworkRule.getInstance(Key.get(new TypeLiteral<ChatBotFactory<PircBotX>>() {
        })), this.testFrameworkRule.getInstance(Key.get(new TypeLiteral<MessageEventFactory<GenericMessageEvent>>() {
        })), chatMessageEventHandler);
  }

  @Test
  public final void onPing_isCalled_respondsWithExpectedPingValue() {
    this.installModules();
    final PircBotXListener subjectUnderTest = this.givenASubjectToTest();
    final PingEvent pingEvent = mock(PingEvent.class);
    final String pingValue = this.testFrameworkRule.getArbitraryString();
    when(pingEvent.getPingValue()).thenReturn(pingValue);

    subjectUnderTest.onPing(pingEvent);

    verify(pingEvent).respond(eq(String.format("PONG %s", pingValue)));
  }

  @Test
  public final void onGenericMessageEvent_forEvent_callsMessageEventHandler() {
    this.installModules();
    final ChatMessageEventHandler chatMessageEventHandler = mock(ChatMessageEventHandler.class);
    final PircBotXListener subjectUnderTest = this.givenASubjectToTest(chatMessageEventHandler);

    subjectUnderTest.onGenericMessage(mock(GenericMessageEvent.class));

    verify(chatMessageEventHandler).handleMessageEvent(isA(ChatMessageEvent.class));
  }

  @Test
  public final void onJoin_isCalled_sendsWithExpectedJoinMessage() {
    this.installModules();
    final MapConfiguration configuration = this.testFrameworkRule.getInstance(MapConfiguration.class);
    final String joinMessage = this.testFrameworkRule.getArbitraryString();
    configuration.set(JOIN_EVENT_MESSAGE_KEY, joinMessage);
    configuration.set(CHAT_BOT_MESSAGE_FORMAT_KEY, "%s");
    final JoinEvent joinEvent = mock(JoinEvent.class);
    final org.pircbotx.Channel channel = mock(org.pircbotx.Channel.class);
    final String channelName = this.testFrameworkRule.getArbitraryString();
    when(joinEvent.getChannel()).thenReturn(channel);
    when(channel.getName()).thenReturn(channelName);
    final PircBotX pircBotX = mock(PircBotX.class);
    final OutputIRC outputIRC = mock(OutputIRC.class);
    when(joinEvent.getBot()).thenReturn(pircBotX);
    when(pircBotX.sendIRC()).thenReturn(outputIRC);
    final PircBotXListener subjectUnderTest = this.givenASubjectToTest();

    subjectUnderTest.onJoin(joinEvent);

    verify(outputIRC).message(eq(ChatChannel.of(channelName).value), eq(ChatMessage.of(joinMessage).value));
  }

}
