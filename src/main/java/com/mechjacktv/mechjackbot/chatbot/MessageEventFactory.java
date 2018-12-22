package com.mechjacktv.mechjackbot.chatbot;

import com.mechjacktv.mechjackbot.ChatMessageEvent;

@FunctionalInterface
public interface MessageEventFactory<E> {

  ChatMessageEvent create(E event);

}
