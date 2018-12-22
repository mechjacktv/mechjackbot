package com.mechjacktv.mechjackbot.chatbot.kicl;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import com.mechjacktv.mechjackbot.ChatBot;

public class KiclChatBotModule extends AbstractModule {

  @Override
  protected void configure() {
    super.configure();

    this.bind(ChatBot.class).to(KiclChatBot.class).in(Scopes.SINGLETON);
  }

}
