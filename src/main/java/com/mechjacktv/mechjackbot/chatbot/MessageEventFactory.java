package com.mechjacktv.mechjackbot.chatbot;

import com.mechjacktv.mechjackbot.MessageEvent;

@FunctionalInterface
public interface MessageEventFactory<E> {

  MessageEvent create(E event);

}
