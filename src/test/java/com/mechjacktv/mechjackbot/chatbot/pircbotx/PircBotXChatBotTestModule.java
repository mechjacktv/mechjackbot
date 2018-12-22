package com.mechjacktv.mechjackbot.chatbot.pircbotx;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.mechjackbot.chatbot.ChatBotFactory;
import com.mechjacktv.mechjackbot.chatbot.ChatUserFactory;
import com.mechjacktv.mechjackbot.chatbot.MessageEventFactory;

public class PircBotXChatBotTestModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(new TypeLiteral<ChatBotFactory<PircBotX>>() {
    }).to(PircBotXChatBotFactory.class).in(Scopes.SINGLETON);

    this.bind(new TypeLiteral<ChatUserFactory<User>>() {
    }).to(PircBotXChatUserFactory.class).in(Scopes.SINGLETON);

    this.bind(new TypeLiteral<MessageEventFactory<GenericMessageEvent>>() {
    }).to(PircBotXMessageEventFactory.class).in(Scopes.SINGLETON);
  }

}
