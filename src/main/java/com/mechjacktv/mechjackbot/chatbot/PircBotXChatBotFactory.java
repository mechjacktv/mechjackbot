package com.mechjacktv.mechjackbot.chatbot;

import javax.inject.Inject;

import org.pircbotx.PircBotX;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.util.ExecutionUtils;

class PircBotXChatBotFactory implements ChatBotFactory<PircBotX> {

  private final Configuration configuration;
  private final ExecutionUtils executionUtils;

  @Inject
  PircBotXChatBotFactory(final Configuration configuration, final ExecutionUtils executionUtils) {
    this.configuration = configuration;
    this.executionUtils = executionUtils;
  }

  @Override
  public final ChatBot create(final PircBotX pircBotX) {
    return new PircBotXChatBot(this.configuration, this.executionUtils, pircBotX);
  }
}
