package com.mechjacktv.mechjackbot.chatbot.pircbotx;

import javax.inject.Inject;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.Message;
import com.mechjacktv.mechjackbot.MessageEventHandler;
import com.mechjacktv.mechjackbot.TwitchChannel;
import com.mechjacktv.mechjackbot.chatbot.ChatBotFactory;
import com.mechjacktv.mechjackbot.chatbot.MessageEventFactory;

public final class PircBotXListener extends ListenerAdapter {

  static final String JOIN_EVENT_MESSAGE_KEY = "chat_bot.join_event.message";
  static final String JOIN_EVENT_MESSAGE_DEFAULT = "Present and ready for action";

  private final Configuration configuration;
  private final ChatBotFactory<PircBotX> chatBotFactory;
  private final MessageEventFactory<GenericMessageEvent> messageEventFactory;
  private final MessageEventHandler messageEventHandler;

  @Inject
  PircBotXListener(final Configuration configuration, final ChatBotFactory<PircBotX> chatBotFactory,
      final MessageEventFactory<GenericMessageEvent> messageEventFactory,
      final MessageEventHandler messageEventHandler) {
    this.configuration = configuration;
    this.chatBotFactory = chatBotFactory;
    this.messageEventFactory = messageEventFactory;
    this.messageEventHandler = messageEventHandler;
  }

  @Override
  public final void onPing(final PingEvent event) {
    event.respond(String.format("PONG %s", event.getPingValue()));
  }

  @Override
  public final void onGenericMessage(final GenericMessageEvent event) {
    this.messageEventHandler.handleMessageEvent(this.messageEventFactory.create(event));
  }

  @Override
  public void onJoin(final JoinEvent event) {
    this.chatBotFactory.create(event.getBot()).sendMessage(TwitchChannel.of(event.getChannel().getName()),
        Message.of(this.configuration.get(JOIN_EVENT_MESSAGE_KEY, JOIN_EVENT_MESSAGE_DEFAULT)));
  }
}
