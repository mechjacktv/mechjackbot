package com.mechjacktv.mechjackbot.chatbot;

import com.google.inject.AbstractModule;

import org.pircbotx.hooks.Listener;

import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.CommandRegistry;

public final class PircBotXChatBotModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ChatBot.class).to(PircBotXChatBot.class).asEagerSingleton();
    this.bind(Listener.class).to(PircBotXListener.class).asEagerSingleton();
    this.bind(CommandRegistry.class).to(DefaultCommandRegistry.class).asEagerSingleton();
  }
}
