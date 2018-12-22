package com.mechjacktv.mechjackbot.chatbot.pircbotx;

import javax.inject.Inject;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.PingEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatChannel;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.mechjackbot.ChatMessageEventHandler;
import com.mechjacktv.mechjackbot.chatbot.ChatBotFactory;
import com.mechjacktv.mechjackbot.chatbot.ChatMessageEventFactory;

public final class PircBotXListener extends ListenerAdapter {

  static final String JOIN_EVENT_MESSAGE_KEY = "chat_bot.join_event.message";
  static final String JOIN_EVENT_MESSAGE_DEFAULT = "Present and ready for action";

  private final Configuration configuration;
  private final ChatBotFactory<PircBotX> chatBotFactory;
  private final ChatMessageEventFactory<GenericMessageEvent> messageEventFactory;
  private final ChatMessageEventHandler chatMessageEventHandler;

  @Inject
  PircBotXListener(final Configuration configuration, final ChatBotFactory<PircBotX> chatBotFactory,
      final ChatMessageEventFactory<GenericMessageEvent> messageEventFactory,
      final ChatMessageEventHandler chatMessageEventHandler) {
    this.configuration = configuration;
    this.chatBotFactory = chatBotFactory;
    this.messageEventFactory = messageEventFactory;
    this.chatMessageEventHandler = chatMessageEventHandler;
  }

  @Override
  public final void onPing(final PingEvent event) {
    event.respond(String.format("PONG %s", event.getPingValue()));
  }

  @Override
  public final void onGenericMessage(final GenericMessageEvent event) {
    this.chatMessageEventHandler.handleMessageEvent(this.messageEventFactory.create(event));
  }

  @Override
  public void onJoin(final JoinEvent event) {
    this.chatBotFactory.create(event.getBot()).sendMessage(ChatChannel.of(event.getChannel().getName()),
        ChatMessage.of(this.configuration.get(JOIN_EVENT_MESSAGE_KEY, JOIN_EVENT_MESSAGE_DEFAULT)));
  }
}
