package com.mechjacktv.mechjackbot.chatbot.pircbotx;

import javax.inject.Inject;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatMessageEvent;
import com.mechjacktv.mechjackbot.chatbot.ChatBotFactory;
import com.mechjacktv.mechjackbot.chatbot.ChatMessageEventFactory;
import com.mechjacktv.mechjackbot.chatbot.ChatUserFactory;
import com.mechjacktv.util.ExecutionUtils;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.types.GenericMessageEvent;

class PircBotXMessageEventFactory implements ChatMessageEventFactory<GenericMessageEvent> {

  private final Configuration configuration;
  private final ChatBotFactory<PircBotX> chatBotFactory;
  private final ChatUserFactory<User> chatUserFactory;
  private final ExecutionUtils executionUtils;

  @Inject
  PircBotXMessageEventFactory(final Configuration configuration, final ChatBotFactory<PircBotX> chatBotFactory,
      final ChatUserFactory<User> chatUserFactory, final ExecutionUtils executionUtils) {
    this.configuration = configuration;
    this.chatBotFactory = chatBotFactory;
    this.chatUserFactory = chatUserFactory;
    this.executionUtils = executionUtils;
  }

  @Override
  public final ChatMessageEvent create(final GenericMessageEvent event) {
    return new PircBotXChatMessageEvent(this.configuration, this.chatBotFactory, this.chatUserFactory,
        this.executionUtils, event);
  }

}
