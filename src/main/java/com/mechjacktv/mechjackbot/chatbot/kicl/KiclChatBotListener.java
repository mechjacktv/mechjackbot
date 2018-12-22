package com.mechjacktv.mechjackbot.chatbot.kicl;

import javax.inject.Inject;
import javax.inject.Provider;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.ChatChannel;
import com.mechjacktv.mechjackbot.ChatMessage;
import com.mechjacktv.mechjackbot.ChatMessageEventHandler;
import com.mechjacktv.mechjackbot.chatbot.ChatMessageEventFactory;

import org.kitteh.irc.client.library.event.channel.ChannelJoinEvent;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import net.engio.mbassy.listener.Handler;

final class KiclChatBotListener {

  static final String JOIN_EVENT_MESSAGE_KEY = "chat_bot.join_event.message";
  static final String JOIN_EVENT_MESSAGE_DEFAULT = "Present and ready for action";

  private final Provider<ChatBot> chatBotProvider;
  private final ChatMessageEventFactory<ChannelMessageEvent> chatMessageEventFactory;
  private final ChatMessageEventHandler chatMessageEventHandler;
  private final Configuration configuration;

  @Inject
  KiclChatBotListener(final Provider<ChatBot> chatBotProvider,
      final ChatMessageEventFactory<ChannelMessageEvent> chatMessageEventFactory,
      final ChatMessageEventHandler chatMessageEventHandler,
      final Configuration configuration) {
    this.chatBotProvider = chatBotProvider;
    this.chatMessageEventFactory = chatMessageEventFactory;
    this.chatMessageEventHandler = chatMessageEventHandler;
    this.configuration = configuration;
  }

  @Handler
  public final void onChannelJoinEventBySelf(final ChannelJoinEvent event) {
    final String channelName = event.getChannel().getName();
    final String userName = event.getUser().getNick();

    if (channelName.substring(1).equals(userName)) {
      this.chatBotProvider.get().sendMessage(ChatChannel.of(channelName),
          ChatMessage.of(this.configuration.get(JOIN_EVENT_MESSAGE_KEY, JOIN_EVENT_MESSAGE_DEFAULT)));
    }
  }

  @Handler
  public final void onChannelMessageEvent(final ChannelMessageEvent event) {
    this.chatMessageEventHandler.handleMessageEvent(this.chatMessageEventFactory.create(event));
  }

}
