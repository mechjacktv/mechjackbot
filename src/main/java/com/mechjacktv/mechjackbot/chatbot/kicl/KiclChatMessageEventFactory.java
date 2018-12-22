package com.mechjacktv.mechjackbot.chatbot.kicl;

import javax.inject.Inject;
import javax.inject.Provider;

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.ChatMessageEvent;
import com.mechjacktv.util.ExecutionUtils;

public class KiclChatMessageEventFactory {

  private final Provider<ChatBot> chatBotProvider;
  private final KiclChatUserFactory chatUserFactory;
  private final Configuration configuration;
  private final ExecutionUtils executionUtils;

  @Inject
  KiclChatMessageEventFactory(final Provider<ChatBot> chatBotProvider, final KiclChatUserFactory chatUserFactory,
      final Configuration configuration, final ExecutionUtils executionUtils) {
    this.chatBotProvider = chatBotProvider;
    this.chatUserFactory = chatUserFactory;
    this.configuration = configuration;
    this.executionUtils = executionUtils;
  }

  public ChatMessageEvent create(final ChannelMessageEvent event) {
    return new KiclChatMessageEvent(event, this.chatBotProvider.get(), this.chatUserFactory, this.configuration,
        this.executionUtils);
  }

}
