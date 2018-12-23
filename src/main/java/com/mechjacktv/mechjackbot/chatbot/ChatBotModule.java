package com.mechjacktv.mechjackbot.chatbot;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.twitchclient.TwitchClientConfiguration;

public class ChatBotModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(DefaultChatBotConfiguration.class).in(Scopes.SINGLETON);
    this.bind(ChatBotConfiguration.class).to(DefaultChatBotConfiguration.class);
    this.bind(TwitchClientConfiguration.class).to(DefaultChatBotConfiguration.class);
    this.bind(Configuration.class).to(DefaultConfiguration.class).in(Scopes.SINGLETON);
  }

}
