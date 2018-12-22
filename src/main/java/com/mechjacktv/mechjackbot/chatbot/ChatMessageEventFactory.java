package com.mechjacktv.mechjackbot.chatbot;

import com.mechjacktv.mechjackbot.ChatMessageEvent;

@FunctionalInterface
public interface ChatMessageEventFactory<E> {

  ChatMessageEvent create(E event);

}
