package com.mechjacktv.mechjackbot.chatbot;

import org.pircbotx.PircBotX;

import com.mechjacktv.mechjackbot.AppConfiguration;
import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.util.ExecutionUtils;

class PircBotXChatBotFactory implements ChatBotFactory<PircBotX> {

  private final AppConfiguration appConfiguration;
  private final ExecutionUtils executionUtils;

  PircBotXChatBotFactory(final AppConfiguration appConfiguration, final ExecutionUtils executionUtils) {
    this.appConfiguration = appConfiguration;
    this.executionUtils = executionUtils;
  }

  @Override
  public final ChatBot create(final PircBotX pircBotX) {
    return new PircBotXChatBot(this.appConfiguration, this.executionUtils, pircBotX);
  }
}
