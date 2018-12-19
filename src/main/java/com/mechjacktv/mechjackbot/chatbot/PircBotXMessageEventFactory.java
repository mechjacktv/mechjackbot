package com.mechjacktv.mechjackbot.chatbot;

import javax.inject.Inject;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.MessageEvent;
import com.mechjacktv.util.ExecutionUtils;

class PircBotXMessageEventFactory implements MessageEventFactory<GenericMessageEvent> {

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
  public final MessageEvent create(final GenericMessageEvent event) {
    return new PircBotXMessageEvent(this.configuration, this.chatBotFactory, this.chatUserFactory,
        this.executionUtils, event);
  }

}
