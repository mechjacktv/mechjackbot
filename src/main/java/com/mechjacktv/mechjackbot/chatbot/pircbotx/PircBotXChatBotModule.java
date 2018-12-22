package com.mechjacktv.mechjackbot.chatbot.pircbotx;

import com.google.inject.Scopes;
import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.chatbot.ChatBotModule;

import org.pircbotx.hooks.Listener;

public final class PircBotXChatBotModule extends ChatBotModule {

  @Override
  protected void configure() {
    System.setProperty("org.slf4j.simpleLogger.log.org.pircbotx", "warn");
    System.setProperty("org.slf4j.simpleLogger.log.org.pircbotx.PircBotX", "info");

    super.configure();
    this.bind(ChatBot.class).to(PircBotXChatBot.class).in(Scopes.SINGLETON);
    this.bind(Listener.class).to(PircBotXListener.class).in(Scopes.SINGLETON);
    this.bind(PircBotXChatBotFactory.class).in(Scopes.SINGLETON);
    this.bind(PircBotXChatUserFactory.class).in(Scopes.SINGLETON);
    this.bind(PircBotXChatMessageEventFactory.class).in(Scopes.SINGLETON);
  }

}
