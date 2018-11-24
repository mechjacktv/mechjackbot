package com.mechjacktv.mechjackbot.chatbot;

import java.util.Set;

import com.google.common.collect.Sets;
import com.mechjacktv.mechjackbot.*;
import com.mechjacktv.mechjackbot.command.DefaultCommandUtils;
import com.mechjacktv.mechjackbot.configuration.ArbitraryChatBotConfiguration;
import com.mechjacktv.util.ArbitraryDataGenerator;
import com.mechjacktv.util.DefaultExecutionUtils;
import com.mechjacktv.util.DefaultTimeUtils;
import com.mechjacktv.util.ExecutionUtils;

import org.junit.Test;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.pircbotx.output.OutputIRC;

import static com.mechjacktv.mechjackbot.chatbot.PircBotXChatBot.CHAT_BOT_MESSAGE_FORMAT_DEFAULT;
import static com.mechjacktv.mechjackbot.chatbot.PircBotXChatBot.CHAT_BOT_MESSAGE_FORMAT_KEY;
import static com.mechjacktv.mechjackbot.chatbot.PircBotXListener.JOIN_EVENT_MESSAGE_DEFAULT;
import static com.mechjacktv.mechjackbot.chatbot.PircBotXListener.JOIN_EVENT_MESSAGE_KEY;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PircBotXListenerUnitTests {

  private final ArbitraryDataGenerator arbitraryDataGenerator = new ArbitraryDataGenerator();
  private final ExecutionUtils executionUtils = new DefaultExecutionUtils();

  private PircBotXListener givenASubjectToTest() {
    return this.givenASubjectToTest(Sets.newHashSet(), mock(AppConfiguration.class));
  }

  private PircBotXListener givenASubjectToTest(final Set<Command> commands) {
    return this.givenASubjectToTest(commands, mock(AppConfiguration.class));
  }

  private PircBotXListener givenASubjectToTest(final AppConfiguration appConfiguration) {
    return this.givenASubjectToTest(Sets.newHashSet(), appConfiguration);
  }

  private PircBotXListener givenASubjectToTest(final Set<Command> commands, final AppConfiguration appConfiguration) {
    final PircBotXChatBotFactory chatBotFactory = new PircBotXChatBotFactory(appConfiguration, this.executionUtils);
    final ChatBotConfiguration chatBotConfiguration = new ArbitraryChatBotConfiguration(this.arbitraryDataGenerator);
    final CommandUtils commandUtils = new DefaultCommandUtils(appConfiguration, this.executionUtils,
        new DefaultTimeUtils());
    final PircBotXMessageEventFactory messageEventFactory = new PircBotXMessageEventFactory(appConfiguration,
        chatBotConfiguration, chatBotFactory,
        commandUtils, this.executionUtils);

    return new PircBotXListener(commands, appConfiguration, new DefaultCommandRegistry(this.executionUtils),
        chatBotFactory, messageEventFactory);
  }

  private Command givenAFakeCommand() {
    final Command command = mock(Command.class);

    when(command.getTrigger()).thenReturn(CommandTrigger.of(this.arbitraryDataGenerator.getString()));
    return command;
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
  public final void onGenericMessage_noCommandIsTriggered_noCommandHandlesMessageEvent() {
    final Command command = this.givenAFakeCommand();
    when(command.isTriggered(isA(MessageEvent.class))).thenReturn(false);
    final PircBotXListener subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command));
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);

    subjectUnderTest.onGenericMessage(genericMessageEvent);

    verify(command, never()).handleMessageEvent(isA(MessageEvent.class));
  }

  @Test
  public final void onGenericMessage_commandIsTriggered_commandHandlesMessageEvent() {
    final Command command = this.givenAFakeCommand();
    when(command.isTriggered(isA(MessageEvent.class))).thenReturn(true);
    final PircBotXListener subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(command));
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);

    subjectUnderTest.onGenericMessage(genericMessageEvent);

    verify(command).handleMessageEvent(isA(MessageEvent.class));
  }

  @Test
  public final void onGenericMessage_oneCommandIsTriggered_triggeredCommandHandlesMessageEvent() {
    final Command goodCommand = this.givenAFakeCommand();
    when(goodCommand.isTriggered(isA(MessageEvent.class))).thenReturn(true);
    final Command badCommand = this.givenAFakeCommand();
    when(badCommand.isTriggered(isA(MessageEvent.class))).thenReturn(false);
    final PircBotXListener subjectUnderTest = this.givenASubjectToTest(Sets.newHashSet(goodCommand, badCommand));
    final GenericMessageEvent genericMessageEvent = mock(GenericMessageEvent.class);

    subjectUnderTest.onGenericMessage(genericMessageEvent);

    verify(goodCommand).handleMessageEvent(isA(MessageEvent.class));
    verify(badCommand, never()).handleMessageEvent(isA(MessageEvent.class));
  }

  @Test
  public final void onJoin_isCalled_sendsWithExpectedJoinMessage() {
    final AppConfiguration appConfiguration = mock(AppConfiguration.class);
    final String joinMessage = this.arbitraryDataGenerator.getString();
    when(appConfiguration.get(eq(JOIN_EVENT_MESSAGE_KEY), eq(JOIN_EVENT_MESSAGE_DEFAULT)))
        .thenReturn(joinMessage);
    when(appConfiguration.get(eq(CHAT_BOT_MESSAGE_FORMAT_KEY), eq(CHAT_BOT_MESSAGE_FORMAT_DEFAULT))).thenReturn("%s");
    final PircBotXListener subjectUnderTest = this.givenASubjectToTest(appConfiguration);
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
