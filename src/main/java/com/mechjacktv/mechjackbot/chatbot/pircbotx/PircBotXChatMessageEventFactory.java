package com.mechjacktv.mechjackbot.chatbot.pircbotx;

import javax.inject.Inject;

import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatMessageEvent;
import com.mechjacktv.util.ExecutionUtils;

class PircBotXChatMessageEventFactory {

  private final Configuration configuration;
  private final PircBotXChatBotFactory chatBotFactory;
  private final PircBotXChatUserFactory chatUserFactory;
  private final ExecutionUtils executionUtils;

  @Inject
  PircBotXChatMessageEventFactory(final Configuration configuration, final PircBotXChatBotFactory chatBotFactory,
      final PircBotXChatUserFactory chatUserFactory, final ExecutionUtils executionUtils) {
    this.configuration = configuration;
    this.chatBotFactory = chatBotFactory;
    this.chatUserFactory = chatUserFactory;
    this.executionUtils = executionUtils;
  }

  public final ChatMessageEvent create(final GenericMessageEvent event) {
    return new PircBotXChatMessageEvent(this.configuration, this.chatBotFactory, this.chatUserFactory,
        this.executionUtils, event);
  }

}
