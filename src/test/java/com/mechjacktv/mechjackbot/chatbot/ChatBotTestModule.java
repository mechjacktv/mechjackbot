package com.mechjacktv.mechjackbot.chatbot;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.mechjackbot.ChatBotConfiguration;

public class ChatBotTestModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ChatBotConfiguration.class).to(TestChatBotConfiguration.class).in(Scopes.SINGLETON);

    this.bind(new TypeLiteral<ChatBotFactory<PircBotX>>() {
    }).to(PircBotXChatBotFactory.class).in(Scopes.SINGLETON);

    this.bind(new TypeLiteral<MessageEventFactory<GenericMessageEvent>>() {
    }).to(PircBotXMessageEventFactory.class).in(Scopes.SINGLETON);
  }

}
