package com.mechjacktv.mechjackbot.chatbot.pircbotx;

import com.google.inject.Scopes;

import com.mechjacktv.mechjackbot.chatbot.ChatBotTestModule;

public class PircBotXChatBotTestModule extends ChatBotTestModule {

  @Override
  protected void configure() {
    super.configure();
    this.bind(PircBotXChatBotFactory.class).in(Scopes.SINGLETON);
    this.bind(PircBotXChatMessageEventFactory.class).in(Scopes.SINGLETON);
    this.bind(PircBotXChatUserFactory.class).in(Scopes.SINGLETON);
  }

}
