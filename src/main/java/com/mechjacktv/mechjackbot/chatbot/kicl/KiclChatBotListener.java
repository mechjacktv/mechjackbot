package com.mechjacktv.mechjackbot.chatbot.kicl;

import javax.inject.Inject;

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import net.engio.mbassy.listener.Handler;

import com.mechjacktv.mechjackbot.ChatMessageEventHandler;

final class KiclChatBotListener {

  private final KiclChatMessageEventFactory chatMessageEventFactory;
  private final ChatMessageEventHandler chatMessageEventHandler;

  @Inject
  KiclChatBotListener(final KiclChatMessageEventFactory chatMessageEventFactory,
      final ChatMessageEventHandler chatMessageEventHandler) {
    this.chatMessageEventFactory = chatMessageEventFactory;
    this.chatMessageEventHandler = chatMessageEventHandler;
  }

  @Handler
  public final void onChannelMessageEvent(final ChannelMessageEvent event) {
    this.chatMessageEventHandler.handleMessageEvent(this.chatMessageEventFactory.create(event));
  }

}
