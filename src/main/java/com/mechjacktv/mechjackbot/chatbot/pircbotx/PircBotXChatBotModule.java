package com.mechjacktv.mechjackbot.chatbot.pircbotx;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.configuration.Configuration;
import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.ChatBotConfiguration;
import com.mechjacktv.mechjackbot.chatbot.ChatBotFactory;
import com.mechjacktv.mechjackbot.chatbot.ChatUserFactory;
import com.mechjacktv.mechjackbot.chatbot.DefaultPropertiesConfiguration;
import com.mechjacktv.mechjackbot.chatbot.MessageEventFactory;
import com.mechjacktv.mechjackbot.chatbot.PropertiesChatBotConfiguration;
import com.mechjacktv.twitchclient.TwitchClientConfiguration;

public final class PircBotXChatBotModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(PropertiesChatBotConfiguration.class).in(Scopes.SINGLETON);
    this.bind(ChatBotConfiguration.class).to(PropertiesChatBotConfiguration.class);
    this.bind(TwitchClientConfiguration.class).to(PropertiesChatBotConfiguration.class);

    this.bind(Configuration.class).to(DefaultPropertiesConfiguration.class).in(Scopes.SINGLETON);

    this.bind(ChatBot.class).to(PircBotXChatBot.class).in(Scopes.SINGLETON);

    this.bind(new TypeLiteral<ChatBotFactory<PircBotX>>() {
    }).to(PircBotXChatBotFactory.class).in(Scopes.SINGLETON);

    this.bind(new TypeLiteral<ChatUserFactory<User>>() {
    }).to(PircBotXChatUserFactory.class).in(Scopes.SINGLETON);

    this.bind(Listener.class).to(PircBotXListener.class).in(Scopes.SINGLETON);

    this.bind(new TypeLiteral<MessageEventFactory<GenericMessageEvent>>() {
    }).to(PircBotXMessageEventFactory.class).in(Scopes.SINGLETON);
  }

}
