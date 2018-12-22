package com.mechjacktv.mechjackbot.chatbot.pircbotx;

import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.types.GenericMessageEvent;

import com.mechjacktv.mechjackbot.ChatBot;
import com.mechjacktv.mechjackbot.chatbot.ChatBotFactory;
import com.mechjacktv.mechjackbot.chatbot.ChatBotModule;
import com.mechjacktv.mechjackbot.chatbot.ChatMessageEventFactory;
import com.mechjacktv.mechjackbot.chatbot.ChatUserFactory;

public final class PircBotXChatBotModule extends ChatBotModule {

  @Override
  protected void configure() {
    super.configure();

    this.bind(ChatBot.class).to(PircBotXChatBot.class).in(Scopes.SINGLETON);
    this.bind(Listener.class).to(PircBotXListener.class).in(Scopes.SINGLETON);

    this.bind(new TypeLiteral<ChatBotFactory<PircBotX>>() {
    }).to(PircBotXChatBotFactory.class).in(Scopes.SINGLETON);

    this.bind(new TypeLiteral<ChatUserFactory<User>>() {
    }).to(PircBotXChatUserFactory.class).in(Scopes.SINGLETON);

    this.bind(new TypeLiteral<ChatMessageEventFactory<GenericMessageEvent>>() {
    }).to(PircBotXMessageEventFactory.class).in(Scopes.SINGLETON);
  }

}
