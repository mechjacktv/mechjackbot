package com.mechjacktv.mechjackbot.chatbot.pircbotx;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class PircBotXChatBotTestModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(PircBotXChatBotFactory.class).in(Scopes.SINGLETON);
    this.bind(PircBotXChatMessageEventFactory.class).in(Scopes.SINGLETON);
    this.bind(PircBotXChatUserFactory.class).in(Scopes.SINGLETON);
  }

}
