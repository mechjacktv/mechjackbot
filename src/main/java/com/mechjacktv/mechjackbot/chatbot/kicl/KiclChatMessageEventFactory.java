package com.mechjacktv.mechjackbot.chatbot.kicl;

import javax.inject.Inject;
import javax.inject.Provider;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.ChatMessageEvent;
import com.mechjacktv.mechjackbot.chatbot.ChatMessageEventFactory;
import com.mechjacktv.mechjackbot.chatbot.ChatUserFactory;
import com.mechjacktv.util.ExecutionUtils;

import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

public class KiclChatMessageEventFactory implements ChatMessageEventFactory<ChannelMessageEvent> {

  private final Provider<ChatBot> chatBotProvider;
  private final ChatUserFactory<User> chatUserFactory;
  private final Configuration configuration;
  private final ExecutionUtils executionUtils;

  @Inject
  KiclChatMessageEventFactory(final Provider<ChatBot> chatBotProvider, final ChatUserFactory<User> chatUserFactory,
      final Configuration configuration, final ExecutionUtils executionUtils) {
    this.chatBotProvider = chatBotProvider;
    this.chatUserFactory = chatUserFactory;
    this.configuration = configuration;
    this.executionUtils = executionUtils;
  }

  @Override
  public ChatMessageEvent create(final ChannelMessageEvent event) {
    return new KiclChatMessageEvent(event, this.chatBotProvider.get(), this.chatUserFactory, this.configuration,
        this.executionUtils);
  }

}
