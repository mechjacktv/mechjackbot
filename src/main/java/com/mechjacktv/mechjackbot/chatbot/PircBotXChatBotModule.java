package com.mechjacktv.mechjackbot.chatbot;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.twitchclient.TwitchClientConfiguration;

public final class PircBotXChatBotModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(PropertiesChatBotConfiguration.class).asEagerSingleton();
    this.bind(ChatBotConfiguration.class).to(PropertiesChatBotConfiguration.class);
    this.bind(TwitchClientConfiguration.class).to(PropertiesChatBotConfiguration.class);

    this.bind(Configuration.class).to(DefaultPropertiesConfiguration.class).asEagerSingleton();

    this.bind(ChatBot.class).to(PircBotXChatBot.class).asEagerSingleton();

    this.bind(new TypeLiteral<ChatBotFactory<PircBotX>>() {
    }).to(PircBotXChatBotFactory.class).asEagerSingleton();

    this.bind(new TypeLiteral<ChatUserFactory<User>>() {
    }).to(PircBotXChatUserFactory.class).asEagerSingleton();

    this.bind(Listener.class).to(PircBotXListener.class).asEagerSingleton();

    this.bind(new TypeLiteral<MessageEventFactory<GenericMessageEvent>>() {
    }).to(PircBotXMessageEventFactory.class).asEagerSingleton();
  }

}
