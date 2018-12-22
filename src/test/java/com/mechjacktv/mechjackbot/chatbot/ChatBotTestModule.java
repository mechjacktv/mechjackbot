package com.mechjacktv.mechjackbot.chatbot;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import com.mechjacktv.mechjackbot.ChatBotConfiguration;

import com.mechjacktv.mechjackbot.ChatBotConfiguration;

public class ChatBotTestModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ChatBotConfiguration.class).to(TestChatBotConfiguration.class).in(Scopes.SINGLETON);
  }

}
