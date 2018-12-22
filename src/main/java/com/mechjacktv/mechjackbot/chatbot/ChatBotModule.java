package com.mechjacktv.mechjackbot.chatbot;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.twitchclient.TwitchClientConfiguration;

public class ChatBotModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(PropertiesChatBotConfiguration.class).in(Scopes.SINGLETON);
    this.bind(ChatBotConfiguration.class).to(PropertiesChatBotConfiguration.class);
    this.bind(TwitchClientConfiguration.class).to(PropertiesChatBotConfiguration.class);
    this.bind(Configuration.class).to(DefaultPropertiesConfiguration.class).in(Scopes.SINGLETON);
  }

}
