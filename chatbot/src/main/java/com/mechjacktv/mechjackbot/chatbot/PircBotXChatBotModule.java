package com.mechjacktv.mechjackbot.chatbot;

import com.google.inject.AbstractModule;
import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.MessageEventHandler;
import org.pircbotx.hooks.Listener;

public final class PircBotXChatBotModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ChatBot.class).to(PircBotXChatBot.class).asEagerSingleton();
    this.bind(Listener.class).to(PircBotXListener.class).asEagerSingleton();
    this.bind(MessageEventHandler.class).to(PircBotXMessageEventHandler.class).asEagerSingleton();
  }
}
